package tech.outspace.papershare.model.entity.rels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import tech.outspace.papershare.model.entity.objs.Paper;
import tech.outspace.papershare.model.unionkey.PaperCitedRelPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "rel_paper_cited")
@IdClass(PaperCitedRelPK.class)
public class PaperCitedRel implements Serializable {
    @Id
    @Column(name = "source_id", nullable = false)
    private Long sourceId;

    @Id
    @Column(name = "cited_id", nullable = false)
    private Long citedId;


    @ManyToOne
    @JoinColumn(name = "source_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_paper_cited_source"))
    private Paper source;

    @ManyToOne
    @JoinColumn(name = "cited_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_paper_cited_cited"))
    private Paper cited;

    public PaperCitedRel(Long source_id, Long cited_id) {
        this.sourceId = source_id;
        this.citedId = cited_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperCitedRel that = (PaperCitedRel) o;
        return Objects.equals(sourceId, that.sourceId) && Objects.equals(citedId, that.citedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceId, citedId);
    }

    @Override
    public String toString() {
        return "PaperCitedRel{" +
                "source_id=" + sourceId +
                ", cited_id=" + citedId +
                '}';
    }
}
