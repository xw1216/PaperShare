package tech.outspace.papershare.model.entity.rels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import tech.outspace.papershare.model.entity.objs.Author;
import tech.outspace.papershare.model.entity.objs.Paper;
import tech.outspace.papershare.model.unionkey.PaperAuthorRelPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "rel_paper_author")
@IdClass(PaperAuthorRelPK.class)
public class PaperAuthorRel implements Serializable {
    @Id
    @Column(name = "paper_id", nullable = false)
    private Long paper_id;

    @Id
    @Column(name = "author_id", nullable = false)
    private Long author_id;

    @ManyToOne
    @JoinColumn(name = "paper_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_paper_author_paper"))
    private Paper paper;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_paper_author_author"))
    private Author author;

    public PaperAuthorRel(Long paper_id, Long author_id) {
        this.paper_id = paper_id;
        this.author_id = author_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperAuthorRel that = (PaperAuthorRel) o;
        return Objects.equals(paper_id, that.paper_id) && Objects.equals(author_id, that.author_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paper_id, author_id);
    }

    @Override
    public String toString() {
        return "PaperAuthorRel{" +
                "paper_id=" + paper_id +
                ", author_id=" + author_id +
                '}';
    }
}
