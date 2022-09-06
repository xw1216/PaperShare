package tech.outspace.papershare.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepoEditVo {
    private String id;
    private String name;
    private boolean visible;
    private String cont;
}
