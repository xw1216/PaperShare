package tech.outspace.papershare.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.outspace.papershare.model.entity.objs.User;
import tech.outspace.papershare.model.enumerate.ERole;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private String id;
    private String email;
    private String name;
    private ERole role;
    private String motto;
    private List<AreaNameDto> areas;

    public UserInfoDto(User user, List<AreaNameDto> areaNameList) {
        id = user.getId();
        email = user.getEmail();
        name = user.getName();
        role = user.getRole();
        motto = user.getMotto();
        areas = areaNameList;
    }
}
