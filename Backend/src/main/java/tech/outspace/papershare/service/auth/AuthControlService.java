package tech.outspace.papershare.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
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
import tech.outspace.papershare.service.auth.token.JwtVerifyToken;
import tech.outspace.papershare.service.mail.MailSender;
import tech.outspace.papershare.utils.encrypt.JwtUtil;
import tech.outspace.papershare.utils.encrypt.PassEncoder;
import tech.outspace.papershare.utils.network.EmailFormat;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;
import tech.outspace.papershare.utils.time.TimeUtil;

import java.time.LocalDateTime;

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
            String wrongMsg = "错误的邮箱格式";
            log.debug(wrongMsg);
            throw new IllegalArgumentException(wrongMsg);
        }
        return userRepo.existsByEmail(email);
    }

    public void emailCheckService(String email) {
        if (!(EmailFormat.checkEmailFormat(email))) {
            String wrongMsg = "错误的邮箱格式";
            log.debug(wrongMsg);
            throw new IllegalArgumentException(wrongMsg);
        }

        User user = userRepo.findByEmail(email);
        if (user != null) {
            String wrongMsg = "邮箱已被注册";
            log.debug(wrongMsg);
            throw new IllegalArgumentException(wrongMsg);
        }

        CheckCode checkCode = checkCodeRepo.findByEmail(email);
        if (checkCode != null) {
            if (TimeUtil.isCheckCodeExpired(checkCode)) {
                checkCodeRepo.deleteById(checkCode.getId());
            } else {
                return;
            }
        }

        CheckCode newCode = new CheckCode(email, EmailFormat.genCheckCode(), TimeUtil.getEmailCodeExpireTime());
        emailSendService(newCode, email);
    }

    private void emailSendService(CheckCode checkCode, String email) {
        if (mailSender.sendCheckCodeEmail(email, checkCode.getCode(), checkCode.getId())) {
            checkCodeRepo.save(checkCode);
        } else {
            throw new MailSendException("验证邮件发送失败");
        }
    }

    public Result<UserDto> registerService(RegisterVo registerVo) {
        User user = userRepo.findByEmail(registerVo.getEmail());
        CheckCode checkCode = checkCodeRepo.findByEmail(registerVo.getEmail());

        checkCodeVerify(user, checkCode, registerVo.getCheckCode());
        clearCheckCode(checkCode);

        String pass = PassEncoder.instance().encode(registerVo.getPass());

        LocalDateTime time = TimeUtil.getUTC();
        User newUser = new User(
                registerVo.getEmail(), registerVo.getName(),
                pass, time, time);
        userRepo.save(newUser);

        return Result.factory(EResult.SUCCESS, loginService(newUser.getEmail()));
    }

    private void checkCodeVerify(User user, CheckCode checkCode, String inputCheckCode) {
        String errMsg = "注册失败：输入不合法";
        if (user != null) {
            log.debug(errMsg);
            throw new DuplicateKeyException(errMsg);
        } else if (checkCode == null || TimeUtil.isCheckCodeExpired(checkCode)) {
            errMsg = "注册失败：没有验证码发送记录";
            log.debug(errMsg);
            throw new BadCredentialsException(errMsg);
        } else if (!(checkCode.getCode().equals(inputCheckCode))) {
            errMsg = "注册失败：验证码错误";
            log.debug(errMsg);
            throw new AuthenticationServiceException(errMsg);
        }
    }

    private void clearCheckCode(CheckCode checkCode) {
        checkCodeRepo.deleteByIdEquals(checkCode.getId());
        checkCodeRepo.deleteByExpireTimeGreaterThan(TimeUtil.getUTC());
    }


    public UserDto loginService(String email) {
        LocalDateTime timestamp = TimeUtil.getUTC();
        User user = userRepo.findByEmail(email);

        if (user == null) {
            String msg = "邮箱没有关联的用户";
            log.debug(msg);
            throw new UsernameNotFoundException(msg);
        }

        UserDto userDto = userMapper.toDto(user);
        String jwt = loginSessionService(user, timestamp);
        userDto.setJwt(jwt);

        return userDto;
    }

    private String loginSessionService(User user, LocalDateTime timestamp) {
        Session session = sessionRepo.findByUserId(user.getId());

        if (session != null && !canRefreshJwt(user, session, timestamp)) {
            // Keep same jwt
            return createJwt(user.getId(), session.getId(), session.getSecret(), user.getSignInTime());
        } else {
            // Assign new jwt
            String secret = PassEncoder.genSalt();
            userRepo.updateSignInTimeById(timestamp, user.getId());
            String sessionId = updateSession(user, session, secret, timestamp);
            return createJwt(user.getId(), sessionId, secret, timestamp);
        }
    }

    private boolean canRefreshJwt(User user, Session session, LocalDateTime timestamp) {
        if (session == null || session.getClose()) {
            return true;
        } else {
            return TimeUtil.isSessionExpired(session, timestamp) ||
                    TimeUtil.getJwtExpireTime(user.getSignInTime()).isBefore(timestamp);
        }
    }

    private String createJwt(String userId, String sessionId, String secret, LocalDateTime timestamp) {
        String jwt = JwtUtil.sign(new TokenVo(userId, sessionId), secret, TimeUtil.getJwtExpireTime(timestamp));
        if (jwt == null) {
            String jwtCreateMsg = "JWT 创建失败";
            log.debug(jwtCreateMsg);
            throw new AuthenticationServiceException(jwtCreateMsg);
        }
        return jwt;
    }

    private String updateSession(User user, Session session, String secret, LocalDateTime timestamp) {
        if (session != null) {
            sessionRepo.deleteByIdEquals(session.getId());
        }
        session = new Session(user.getId(), secret, TimeUtil.getSessionExpireTime(timestamp), false);
        sessionRepo.save(session);
        return session.getId();
    }

    public String GetUserIdFromAuth() {
        JwtVerifyToken token;
        try {
            token = (JwtVerifyToken) SecurityContextHolder.getContext().getAuthentication();
            return token.getTokenVo().getUserId();
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("");
        }
    }


}
