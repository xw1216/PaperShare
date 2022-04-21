package tech.outspace.papershare.model.entity.rels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import tech.outspace.papershare.model.entity.objs.Area;
import tech.outspace.papershare.model.entity.objs.Paper;
import tech.outspace.papershare.model.unionkey.PaperAreaRelPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "rel_paper_area")
@IdClass(PaperAreaRelPK.class)
public class PaperAreaRel implements Serializable {
    @Id
    @Column(name = "paper_id", nullable = false)
    private Long paper_id;

    @Id
    @Column(name = "area_id", nullable = false)
    private Long area_id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "paper_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_paper_area_paper"))
    private Paper paper;

    @ManyToOne
    @JoinColumn(name = "area_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_paper_area_area"))
    private Area area;

    public PaperAreaRel(Long paper_id, Long area_id) {
        this.paper_id = paper_id;
        this.area_id = area_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperAreaRel that = (PaperAreaRel) o;
        return Objects.equals(paper_id, that.paper_id) && Objects.equals(area_id, that.area_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paper_id, area_id);
    }

    @Override
    public String toString() {
        return "PaperAreaRel{" +
                "paper_id=" + paper_id +
                ", area_id=" + area_id +
                '}';
    }
}
