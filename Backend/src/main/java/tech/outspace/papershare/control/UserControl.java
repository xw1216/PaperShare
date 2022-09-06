package tech.outspace.papershare.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tech.outspace.papershare.model.dto.UserDisplayDto;
import tech.outspace.papershare.model.dto.UserInfoDto;
import tech.outspace.papershare.service.auth.AuthControlService;
import tech.outspace.papershare.service.user.UserControlService;
import tech.outspace.papershare.utils.network.HttpFormat;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserControl {
    private final AuthControlService authService;

    private final UserControlService userService;

    public UserControl(AuthControlService authService, UserControlService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/repos")
    public Result<UserDisplayDto> getUserRepoInfo(@RequestParam String id, HttpServletResponse response) {
        try {
            if (Objects.equals(id, "")) {
                id = authService.GetUserIdFromAuth();
            }
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return userService.getUserDisplayInfo(id);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "无法获取用户仓库信息");
        }
    }

    @GetMapping("/get")
    public Result<UserInfoDto> getUserInfo(HttpServletResponse response) {
        try {
            String userid = authService.GetUserIdFromAuth();
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return userService.getUserInfo(userid);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "无法获取用户信息");
        }
    }

    @PostMapping("/edit")
    public Result<String> editUserInfo(@RequestBody UserInfoDto dto, HttpServletResponse response) {
        try {
            String userid = authService.GetUserIdFromAuth();
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return userService.editUserInfo(dto);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "无法获取用户信息");
        }
    }


}
