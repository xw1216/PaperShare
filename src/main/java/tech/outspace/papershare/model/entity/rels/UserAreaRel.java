package tech.outspace.papershare.model.entity.rels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import tech.outspace.papershare.model.entity.objs.Area;
import tech.outspace.papershare.model.entity.objs.User;
import tech.outspace.papershare.model.unionkey.UserAreaRelPK;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "rel_user_area")
@IdClass(UserAreaRelPK.class)
public class UserAreaRel {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long user_id;

    @Id
    @Column(name = "area_id", nullable = false)
    private Long area_id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_user_area_user"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "area_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_user_area_area"))
    private Area area;

    public UserAreaRel(Long user_id, Long area_id) {
        this.user_id = user_id;
        this.area_id = area_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAreaRel that = (UserAreaRel) o;
        return Objects.equals(user_id, that.user_id) && Objects.equals(area_id, that.area_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, area_id);
    }

    @Override
    public String toString() {
        return "UserAreaRel{" +
                "user_id=" + user_id +
                ", area_id=" + area_id +
                '}';
    }
}
