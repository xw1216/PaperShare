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
    private String userId;
    private String areaId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAreaRelPK that = (UserAreaRelPK) o;
        return Objects.equals(userId, that.userId) && Objects.equals(areaId, that.areaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, areaId);
    }

    @Override
    public String toString() {
        return "UserAreaRelPK{" +
                "userId='" + userId + '\'' +
                ", areaId='" + areaId + '\'' +
                '}';
    }
}
