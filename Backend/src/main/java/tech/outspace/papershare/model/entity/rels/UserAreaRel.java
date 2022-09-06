package tech.outspace.papershare.model.entity.rels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import tech.outspace.papershare.model.entity.objs.Area;
import tech.outspace.papershare.model.entity.objs.User;
import tech.outspace.papershare.model.unionkey.UserAreaRelPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "rel_user_area")
@IdClass(UserAreaRelPK.class)
public class UserAreaRel implements Serializable {
    @Id
    @Column(name = "user_id", nullable = false, length = 127)
    private String userId;

    @Id
    @Column(name = "area_id", nullable = false, length = 127)
    private String areaId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_user_area_user"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "area_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_user_area_area"))
    private Area area;

    public UserAreaRel(String userId, String areaId) {
        this.userId = userId;
        this.areaId = areaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAreaRel that = (UserAreaRel) o;
        return Objects.equals(userId, that.userId) && Objects.equals(areaId, that.areaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, areaId);
    }

    @Override
    public String toString() {
        return "UserAreaRel{" +
                "userId='" + userId + '\'' +
                ", areaId='" + areaId + '\'' +
                '}';
    }
}
