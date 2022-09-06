package tech.outspace.papershare.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TokenVo implements Serializable {
    private String userId;
    private String sessionId;
}
