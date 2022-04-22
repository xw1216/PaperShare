package tech.outspace.papershare.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.outspace.papershare.model.dto.UserDto;
import tech.outspace.papershare.model.dto.UserMapper;
import tech.outspace.papershare.model.entity.objs.CheckCode;
import tech.outspace.papershare.model.entity.objs.Session;
import tech.outspace.papershare.model.entity.objs.User;
import tech.outspace.papershare.model.vo.RegisterVo;
import tech.outspace.papershare.model.vo.TokenVo;
import tech.outspace.papershare.repo.objs.CheckCodeRepo;
import tech.outspace.papershare.repo.objs.SessionRepo;
import tech.outspace.papershare.repo.objs.UserRepo;
import tech.outspace.papershare.service.mail.MailSender;
import tech.outspace.papershare.utils.encrypt.JwtUtil;
import tech.outspace.papershare.utils.encrypt.PassEncoder;
import tech.outspace.papershare.utils.network.EmailFormat;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;
import tech.outspace.papershare.utils.time.TimeUtil;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class AuthControlService {
    private final UserRepo userRepo;
    private final SessionRepo sessionRepo;
    private final CheckCodeRepo checkCodeRepo;
    private final MailSender mailSender;
    private final UserMapper userMapper;

    public AuthControlService(UserRepo userRepo, SessionRepo sessionRepo, CheckCodeRepo checkCodeRepo, MailSender mailSender, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
        this.checkCodeRepo = checkCodeRepo;
        this.mailSender = mailSender;
        this.userMapper = userMapper;
    }

    public boolean emailExistService(String email) {
        if (!(EmailFormat.checkEmailFormat(email))) {
            String wrongMsg = "Wrong email format";
            log.debug(wrongMsg);
            throw new IllegalArgumentException(wrongMsg);
        }
        return userRepo.existsByEmail(email);
    }

    public void emailCheckService(String email) {
        if (!(EmailFormat.checkEmailFormat(email))) {
            String wrongMsg = "Wrong email format";
            log.debug(wrongMsg);
            throw new IllegalArgumentException(wrongMsg);
        }

        Optional<CheckCode> optional = checkCodeRepo.findByEmail(email);
        if (optional.isPresent()) {
            CheckCode checkCode = optional.get();
            if (TimeUtil.isCheckCodeExpired(checkCode)) {
                checkCodeRepo.deleteById(checkCode.getId());
            } else {
                return;
            }
        }

        CheckCode newCode = new CheckCode(
                email, EmailFormat.genCheckCode(), TimeUtil.getEmailCodeExpireTime());
        emailSendService(newCode, email);
    }

    private void emailSendService(CheckCode checkCode, String email) {
        if (mailSender.sendCheckCodeEmail(email, checkCode.getCode(), checkCode.getId())) {
            checkCodeRepo.save(checkCode);
        } else {
            throw new MailSendException("Mail send failed");
        }
    }

    public Result<UserDto> registerService(RegisterVo registerVo) {
        Optional<User> optionalUser = userRepo.findByEmail(registerVo.getEmail());
        Optional<CheckCode> optionalCheckCode = checkCodeRepo.findByEmail(registerVo.getEmail());

        checkCodeVerify(optionalUser, optionalCheckCode, registerVo.getCheckCode());

        if (optionalCheckCode.isEmpty()) {
            return null;
        }
        CheckCode checkCode = optionalCheckCode.get();
        clearCheckCode(checkCode);

        LocalDateTime time = TimeUtil.getUTC();

        User user = new User(
                registerVo.getEmail(), registerVo.getName(),
                registerVo.getPass(), time, time);
        userRepo.save(user);

        return Result.factory(EResult.SUCCESS, loginService(user.getEmail()));
    }

    private void checkCodeVerify(Optional<User> optionalUser, Optional<CheckCode> optionalCheckCode, String checkCode) {
        String errMsg = "Register failed: Invalid argument";
        if (optionalUser.isPresent()) {
            log.debug(errMsg);
            throw new DuplicateKeyException(errMsg);
        } else if (optionalCheckCode.isEmpty() || TimeUtil.isCheckCodeExpired(optionalCheckCode.get())) {
            errMsg = "Register failed: No check code record";
            log.debug(errMsg);
            throw new BadCredentialsException(errMsg);
        } else if (!(optionalCheckCode.get().getCode().equals(checkCode))) {
            errMsg = "Register failed: Wrong check code";
            log.debug(errMsg);
            throw new AuthenticationServiceException(errMsg);
        }
    }

    private void clearCheckCode(CheckCode checkCode) {
        checkCodeRepo.deleteById(checkCode.getId());
        checkCodeRepo.deleteByExpireTimeGreaterThan(TimeUtil.getUTC());
    }


    public UserDto loginService(String email) {
        LocalDateTime timestamp = TimeUtil.getUTC();
        Optional<User> optional = userRepo.findByEmail(email);

        if (optional.isEmpty()) {
            String msg = "No user linked to this email";
            log.debug(msg);
            throw new UsernameNotFoundException(msg);
        }

        UserDto userDto = userMapper.toDto(optional.get());
        String jwt = loginSessionService(optional.get(), timestamp);
        userDto.setJwt(jwt);

        return userDto;
    }

    private String loginSessionService(User user, LocalDateTime timestamp) {
        Optional<Session> optional = sessionRepo.findByUserId(user.getId());

        if (optional.isPresent() && !canRefreshJwt(user, optional, timestamp)) {
            // Keep same jwt
            Session session = optional.get();
            return createJwt(user.getId(), session.getId(), session.getSecret(), user.getSignInTime());
        } else {
            // Assign new jwt
            String secret = PassEncoder.genSalt();
            userRepo.updateSignInTimeById(timestamp, user.getId());
            Long sessionId = updateSession(user, optional, secret, timestamp);
            return createJwt(user.getId(), sessionId, secret, timestamp);
        }
    }

    private boolean canRefreshJwt(User user, Optional<Session> optional, LocalDateTime timestamp) {
        if (optional.isEmpty() || optional.get().getClose()) {
            return true;
        } else {
            return TimeUtil.isSessionExpired(optional.get(), timestamp) ||
                    TimeUtil.getJwtExpireTime(user.getSignInTime()).isBefore(timestamp);
        }
    }

    private String createJwt(Long userId, Long sessionId, String secret, LocalDateTime timestamp) {
        String jwt = JwtUtil.sign(new TokenVo(userId, sessionId), secret, TimeUtil.getJwtExpireTime(timestamp));
        if (jwt == null) {
            String jwtCreateMsg = "Jwt creation failure";
            log.debug(jwtCreateMsg);
            throw new AuthenticationServiceException(jwtCreateMsg);
        }
        return jwt;
    }

    private Long updateSession(User user, Optional<Session> optional, String secret, LocalDateTime timestamp) {
        Session session;
        if (optional.isPresent()) {
            session = optional.get();
            sessionRepo.deleteById(session.getId());
        }
        session = new Session(user.getId(), secret, TimeUtil.getSessionExpireTime(timestamp), false);
        sessionRepo.save(session);
        return session.getId();
    }


}
