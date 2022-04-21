package tech.outspace.papershare.model.unionkey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRepoRelPK implements Serializable {
    @Column(name = "user_id", nullable = false)
    private Long user_id;

    @Column(name = "repo_id", nullable = false)
    private Long repo_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRepoRelPK that = (UserRepoRelPK) o;
        return Objects.equals(user_id, that.user_id) && Objects.equals(repo_id, that.repo_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, repo_id);
    }

    @Override
    public String toString() {
        return "UserRepoRelPK{" +
                "user_id=" + user_id +
                ", repo_id=" + repo_id +
                '}';
    }
}
