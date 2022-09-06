package tech.outspace.papershare.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.outspace.papershare.model.entity.objs.Paper;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaperInfoDto {
    private String id;
    private String title;
    private String brief;
    private String author;
    private Integer year;
    private String journal;
    private String doi;
    private List<AreaNameDto> areas;

    public PaperInfoDto(Paper paper, List<AreaNameDto> areaNameList) {
        id = paper.getId();
        title = paper.getTitle();
        brief = paper.getBrief();
        author = paper.getAuthor();
        year = paper.getYear();
        journal = paper.getJournal();
        doi = paper.getDoi();
        areas = areaNameList;
    }
}
