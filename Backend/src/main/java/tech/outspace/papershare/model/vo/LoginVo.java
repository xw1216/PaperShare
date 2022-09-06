package tech.outspace.papershare.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class LoginVo implements Serializable {
    private String email;
    private String pass;
}
