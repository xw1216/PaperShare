package tech.outspace.papershare.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDisplayDto {
    private String id;
    private String name;
    private String motto;
    private List<AreaNameDto> areas;
    private List<RepoBriefDto> repos;
}
