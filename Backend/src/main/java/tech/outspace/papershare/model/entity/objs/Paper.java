package tech.outspace.papershare.model.entity.objs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import tech.outspace.papershare.model.entity.rels.PaperAreaRel;
import tech.outspace.papershare.model.entity.rels.RepoPaperRel;
import tech.outspace.papershare.model.vo.PaperVo;
import tech.outspace.papershare.utils.general.SnowFlake;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "paper", indexes = {
        @Index(name = "index_paper_title", columnList = "title")
})
public class Paper implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 127)
    private String id;

    @Column(name = "title", nullable = false, length = 511)
    private String title;

    @Column(name = "brief", nullable = false)
    private String brief;

    @Column(name = "author", nullable = false, length = 511)
    private String author;

    @Column(name = "year", nullable = false, length = 63)
    private Integer year;

    @Column(name = "journal", length = 511)
    private String journal;

    @Column(name = "doi", nullable = false, unique = true)
    private String doi;

    @Column(name = "path")
    private String path;

    @OneToMany(mappedBy = "paper", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "paper", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "paper", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RepoPaperRel> repoRels = new ArrayList<>();

    @OneToMany(mappedBy = "paper", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PaperAreaRel> areaRels = new ArrayList<>();

    @Transient
    private SnowFlake idGen = new SnowFlake(1, 1);

    public Paper(String title, String brief,
                 String author, Integer year,
                 String journal, String doi,
                 String path) {
        this.id = idGen.NextId();
        this.title = title;
        this.brief = brief;
        this.author = author;
        this.year = year;
        this.journal = journal;
        this.doi = doi;
        this.path = path;
    }

    public Paper(PaperVo vo) {
        this.id = idGen.NextId();
        this.title = vo.getTitle();
        this.brief = vo.getBrief();
        this.author = vo.getAuthor();
        this.year = vo.getYear();
        this.journal = vo.getJournal();
        this.doi = vo.getDoi();
        this.path = vo.getPath();
    }

    @Override
    public String toString() {
        return "Paper{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", brief='" + brief + '\'' +
                ", year=" + year +
                ", journal='" + journal + '\'' +
                ", doi='" + doi + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
