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
import java.util.Optional;

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
        Optional<User> optional = userRepo.findByEmail(email);
        return userDetailsBuilder(optional);
    }

    public UserDetails loadUserById(Long id) {
        Optional<User> optional = userRepo.findById(id);
        return userDetailsBuilder(optional);
    }

    public Session loadSessionById(Long id) {
        Optional<Session> optional = sessionRepo.findById(id);
        if (optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    public void delaySessionExpireTimeById(Long id, LocalDateTime time) {
        sessionRepo.updateExpireTimeById(time, id);
    }

    public void deleteSessionById(Long id) {
        sessionRepo.deleteById(id);
    }

    public void logoutById(Long id) {
        Optional<Session> optional = sessionRepo.findById(id);
        if (optional.isEmpty()) {
            log.debug("Logout with no session before.");
        } else {
            Session session = optional.get();
            if (session.getClose() || TimeUtil.isSessionExpired(session)) {
                log.debug("Logout with session already expired or closed.");
            } else {
                log.debug("Logout normally, session close.");
                sessionRepo.updateCloseById(true, session.getId());
            }
        }
    }

    private UserDetails userDetailsBuilder(Optional<User> optional) {
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("User do not exist");
        }
        User user = optional.get();
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPass())
                .roles(user.getRole().getRoleStr())
                .accountLocked(user.getStatus().isBaned())
                .build();
    }
}
