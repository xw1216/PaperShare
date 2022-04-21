package tech.outspace.papershare.model.entity.objs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import tech.outspace.papershare.model.entity.rels.PaperAreaRel;
import tech.outspace.papershare.model.entity.rels.PaperAuthorRel;
import tech.outspace.papershare.model.entity.rels.PaperCitedRel;
import tech.outspace.papershare.model.entity.rels.RepoPaperRel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "paper", indexes = {
        @Index(name = "index_paper_doi", columnList = "doi")
})
public class Paper implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 511)
    private String title;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "journal", length = 511)
    private String journal;

    @Column(name = "publisher", length = 511)
    private String publisher;

    @Column(name = "doi", nullable = false, unique = true)
    private String doi;

    @Column(name = "path")
    private String path;

    @OneToMany(mappedBy = "paper", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "paper", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RepoPaperRel> repoRels = new ArrayList<>();

    @OneToMany(mappedBy = "source", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PaperCitedRel> citedRels = new ArrayList<>();

    @OneToMany(mappedBy = "cited", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PaperCitedRel> sourceRels = new ArrayList<>();

    @OneToMany(mappedBy = "paper", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PaperAuthorRel> authorRels = new ArrayList<>();

    @OneToMany(mappedBy = "paper", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PaperAreaRel> areaRels = new ArrayList<>();

    public Paper(Long id, String title, Integer year, String journal, String publisher, String doi) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.journal = journal;
        this.publisher = publisher;
        this.doi = doi;
    }

    @Override
    public String toString() {
        return "Paper{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", journal='" + journal + '\'' +
                ", publisher='" + publisher + '\'' +
                ", doi='" + doi + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
