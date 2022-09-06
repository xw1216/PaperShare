package tech.outspace.papershare.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.outspace.papershare.model.entity.objs.Repo;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepoBriefDto {
    private String id;
    private String name;
    private boolean visible;
    private String cont;

    public RepoBriefDto(Repo repo) {
        id = repo.getId();
        name = repo.getName();
        visible = repo.getVisible();
        cont = repo.getCont();
    }
}
