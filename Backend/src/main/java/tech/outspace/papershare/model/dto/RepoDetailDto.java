package tech.outspace.papershare.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.outspace.papershare.model.entity.objs.Repo;
import tech.outspace.papershare.model.entity.objs.User;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepoDetailDto {
    private String id;
    private String name;
    private String cont;
    private String userId;
    private String userName;
    private boolean visible;
    private List<PaperInfoDto> papers;

    public RepoDetailDto(Repo repo, User user, List<PaperInfoDto> paperList) {
        id = repo.getId();
        name = repo.getName();
        cont = repo.getCont();
        userId = user.getId();
        userName = user.getName();
        visible = repo.getVisible();
        papers = paperList;
    }

}
