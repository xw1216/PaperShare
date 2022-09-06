package tech.outspace.papershare.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteNewVo {
    private String title;
    private String cont;
    private Integer page;
    private String repoId;
    private String paperId;
}
