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
public class UserRepoRelPK implements Serializable {

    private String userId;

    private String repoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRepoRelPK that = (UserRepoRelPK) o;
        return Objects.equals(userId, that.userId) && Objects.equals(repoId, that.repoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, repoId);
    }

    @Override
    public String toString() {
        return "UserRepoRelPK{" +
                "userId='" + userId + '\'' +
                ", repoId='" + repoId + '\'' +
                '}';
    }
}
