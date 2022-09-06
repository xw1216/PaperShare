package tech.outspace.papershare.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RegisterVo implements Serializable {
    private String email;
    private String name;
    private String pass;
    private String checkCode;

    public boolean checkNull() {
        return this.email == null ||
                this.name == null ||
                this.pass == null ||
                this.checkCode == null;
    }
}
