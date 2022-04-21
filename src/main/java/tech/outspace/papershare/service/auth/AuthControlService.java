package tech.outspace.papershare.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.outspace.papershare.model.dto.UserDto;
import tech.outspace.papershare.model.dto.UserMapper;
import tech.outspace.papershare.model.entity.objs.Session;
import tech.outspace.papershare.model.entity.objs.User;
import tech.outspace.papershare.model.vo.TokenVo;
import tech.outspace.papershare.repo.objs.SessionRepo;
import tech.outspace.papershare.repo.objs.UserRepo;
import tech.outspace.papershare.utils.encrypt.JwtUtil;
import tech.outspace.papershare.utils.encrypt.PassEncoder;
import tech.outspace.papershare.utils.time.TimeUtil;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class AuthControlService {
    private final UserRepo userRepo;
    private final SessionRepo sessionRepo;

    public AuthControlService(UserRepo userRepo, SessionRepo sessionRepo) {
        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
    }

    public UserDto loginService(String email) {
        LocalDateTime timestamp = TimeUtil.getUTC();
        Optional<User> optional = userRepo.findByEmail(email);

        if (optional.isEmpty()) {
            String msg = "No user linked to this email";
            log.debug(msg);
            throw new UsernameNotFoundException(msg);
        }

        UserDto userDto = UserMapper.instance.toDto(optional.get());
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
