package tech.outspace.papershare.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpVo {
    private String email;
    private String name;
    private String pass;
    private String checkCode;
}
