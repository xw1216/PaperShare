package tech.outspace.papershare.model.unionkey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAreaRelPK implements Serializable {
    private Long user_id;
    private Long area_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAreaRelPK that = (UserAreaRelPK) o;
        return Objects.equals(user_id, that.user_id) && Objects.equals(area_id, that.area_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, area_id);
    }

    @Override
    public String toString() {
        return "UserAreaRelPK{" +
                "user_id=" + user_id +
                ", area_id=" + area_id +
                '}';
    }
}
