package tech.outspace.papershare.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Schema(description = "注册时发送请求体")
public class LoginVo implements Serializable {
    @NotNull(message = "必须具有登录邮箱")
    @Schema(description = "邮箱", example = "bilbo@gmail.com", minLength = 1, maxLength = 256, required = true)
    private String email;

    @NotNull(message = "必须具有密码")
    @Schema(description = "密码", example = "123456", minLength = 6, maxLength = 512, required = true)
    private String pass;
}
