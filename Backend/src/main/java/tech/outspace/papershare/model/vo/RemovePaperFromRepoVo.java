package tech.outspace.papershare.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemovePaperFromRepoVo {
    private String paperId;
    private String repoId;
}
