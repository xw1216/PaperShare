package tech.outspace.papershare.model.entity.objs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import tech.outspace.papershare.model.entity.rels.PaperAreaRel;
import tech.outspace.papershare.model.entity.rels.UserAreaRel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "area")
public class Area implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 127)
    private String name;

    @Column(name = "popular", nullable = false)
    private Long popular = 0L;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_area_id"))
    private Area parentArea;

    @OneToMany(mappedBy = "parentArea", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Area> childAreas = new ArrayList<>();

    @OneToMany(mappedBy = "area", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserAreaRel> userAreaRels = new ArrayList<>();

    @OneToMany(mappedBy = "area", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PaperAreaRel> paperAreaRels = new ArrayList<>();

    public Area(Long id, String name, Long popular, Long parentId) {
        this.id = id;
        this.name = name;
        this.popular = popular;
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Area{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", popular=" + popular +
                ", parentId=" + parentId +
                '}';
    }
}
