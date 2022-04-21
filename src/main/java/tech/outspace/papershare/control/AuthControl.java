package tech.outspace.papershare.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.outspace.papershare.model.dto.UserDto;
import tech.outspace.papershare.model.vo.SignUpVo;
import tech.outspace.papershare.service.auth.AuthControlService;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class AuthControl {
    private final AuthControlService authService;

    public AuthControl(AuthControlService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/auth/register")
    public Result<UserDto> Register(@RequestBody SignUpVo signUpVo, HttpServletResponse response) {
        return null;
    }


    @PostMapping(value = "/auth/login")
    public Result<UserDto> Login(@RequestBody HttpServletResponse response) {
        if (response == null || response.getStatus() != EResult.SUCCESS.getCode()) {
            return Result.factory(EResult.AUTH_FAIL, null);
        }
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserDto userDto = authService.loginService(email);
            return Result.factory(EResult.SUCCESS, userDto);
        } catch (UsernameNotFoundException e) {
            return Result.factory(EResult.AUTH_FAIL, null);
        } catch (Exception e) {
            return Result.factory(EResult.UNKNOWN_ERROR, null);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @PostMapping(value = "/auth/logout")
    public Result<String> Logout(HttpServletResponse response) {
        return null;
    }
}
