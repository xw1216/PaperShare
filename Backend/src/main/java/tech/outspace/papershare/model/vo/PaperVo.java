package tech.outspace.papershare.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.outspace.papershare.model.dto.AreaNameDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaperVo {
    private String title;
    private String brief;
    private String author;
    private Integer year;
    private String journal;
    private String doi;
    private String path;
    private List<AreaNameDto> areaList;
}
