package tech.outspace.papershare.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.outspace.papershare.model.entity.objs.Session;
import tech.outspace.papershare.model.entity.objs.User;
import tech.outspace.papershare.repo.objs.SessionRepo;
import tech.outspace.papershare.repo.objs.UserRepo;
import tech.outspace.papershare.utils.time.TimeUtil;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AuthProviderService implements UserDetailsService {

    private final UserRepo userRepo;
    private final SessionRepo sessionRepo;

    public AuthProviderService(UserRepo userRepo, SessionRepo sessionRepo) {
        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        return userDetailsBuilder(user);
    }

    public UserDetails loadUserById(String id) {
        User user = userRepo.findByIdEquals(id);
        return userDetailsBuilder(user);
    }

    public Session loadSessionById(String id) {
        return sessionRepo.findByIdEquals(id);
    }

    public void delaySessionExpireTimeById(String id, LocalDateTime time) {
        sessionRepo.updateExpireTimeById(time, id);
    }

    public void deleteSessionById(String id) {
        sessionRepo.deleteByIdEquals(id);
    }

    public void logoutById(String id) {
        Session session = sessionRepo.findByIdEquals(id);
        if (session == null) {
            log.debug("Logout with no session before.");
        } else {
            if (session.getClose() || TimeUtil.isSessionExpired(session)) {
                log.debug("Logout with session already expired or closed.");
            } else {
                log.debug("Logout normally, session close.");
                sessionRepo.updateCloseById(true, session.getId());
            }
        }
    }

    private UserDetails userDetailsBuilder(User user) {
        if (user == null) {
            throw new UsernameNotFoundException("User do not exist");
        }
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPass())
                .roles(user.getRole().getRoleStr())
                .accountLocked(user.getStatus().isBaned())
                .build();
    }
}
