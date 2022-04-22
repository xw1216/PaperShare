package tech.outspace.papershare.control;

import com.mongodb.DuplicateKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.outspace.papershare.model.dto.UserDto;
import tech.outspace.papershare.model.vo.EmailCheckVo;
import tech.outspace.papershare.model.vo.RegisterVo;
import tech.outspace.papershare.service.auth.AuthControlService;
import tech.outspace.papershare.utils.network.EmailFormat;
import tech.outspace.papershare.utils.network.HttpFormat;
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

    @PostMapping(value = "/auth/email/code")
    public Result<String> EmailCodeSend(@RequestBody EmailCheckVo emailVo, HttpServletResponse response) {
        try {
            authService.emailCheckService(emailVo.getEmail());
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return Result.factory(EResult.SUCCESS, "Email check code send successfully");
        } catch (IllegalArgumentException e) {
            return HttpFormat.reviseErrorResponse(response, EResult.BAD_REQUEST, "Invalid email address");
        } catch (MailSendException e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "Mail send failed");
        }
    }

    @PostMapping(value = "/auth/email/duplicate")
    public Result<Boolean> EmailDupCheck(@RequestBody EmailCheckVo emailVo, HttpServletResponse response) {
        try {
            Boolean duplicate = authService.emailExistService(emailVo.getEmail());
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return Result.factory(EResult.SUCCESS, duplicate);
        } catch (IllegalArgumentException e) {
            return HttpFormat.reviseErrorResponse(response, EResult.BAD_REQUEST);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR);
        }
    }


    @PostMapping(value = "/auth/register")
    public Result<UserDto> Register(@RequestBody RegisterVo registerVo, HttpServletResponse response) {
        if (registerVo == null || registerVo.checkNull() || !(EmailFormat.checkEmailFormat(registerVo.getEmail()))) {
            return HttpFormat.reviseErrorResponse(response, EResult.BAD_REQUEST);
        }
        try {
            Result<UserDto> result = authService.registerService(registerVo);
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return result;
        } catch (DuplicateKeyException e) {
            HttpFormat.reviseErrorResponse(response, EResult.DATA_DUPLICATE);
        } catch (BadCredentialsException e) {
            HttpFormat.reviseErrorResponse(response, EResult.REQUEST_REJECT);
        } catch (AuthenticationServiceException e) {
            HttpFormat.reviseErrorResponse(response, EResult.AUTH_FAIL);
        } catch (Exception e) {
            HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR);
        }
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
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return Result.factory(EResult.SUCCESS, userDto);
        } catch (UsernameNotFoundException e) {
            return HttpFormat.reviseErrorResponse(response, EResult.AUTH_FAIL);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @PostMapping(value = "/auth/logout")
    public Result<String> Logout(HttpServletResponse response) {
        return null;
    }

}
