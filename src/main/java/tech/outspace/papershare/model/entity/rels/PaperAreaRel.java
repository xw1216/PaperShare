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
    private Long paperId;

    @Id
    @Column(name = "area_id", nullable = false)
    private Long areaId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "paper_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_paper_area_paper"))
    private Paper paper;

    @ManyToOne
    @JoinColumn(name = "area_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_paper_area_area"))
    private Area area;

    public PaperAreaRel(Long paper_id, Long area_id) {
        this.paperId = paper_id;
        this.areaId = area_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperAreaRel that = (PaperAreaRel) o;
        return Objects.equals(paperId, that.paperId) && Objects.equals(areaId, that.areaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paperId, areaId);
    }

    @Override
    public String toString() {
        return "PaperAreaRel{" +
                "paper_id=" + paperId +
                ", area_id=" + areaId +
                '}';
    }
}
