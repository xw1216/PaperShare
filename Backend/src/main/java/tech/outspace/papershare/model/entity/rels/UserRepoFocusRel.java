package tech.outspace.papershare.model.entity.rels;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import tech.outspace.papershare.model.entity.objs.Repo;
import tech.outspace.papershare.model.entity.objs.User;
import tech.outspace.papershare.model.unionkey.UserRepoRelPK;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "rel_user_repo_focus")
@IdClass(UserRepoRelPK.class)
public class UserRepoFocusRel implements Serializable {
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Id
    @Column(name = "repo_id", nullable = false)
    private String repoId;

    @CreatedDate
    @Column(name = "focus_time", nullable = false)
    private LocalDateTime focusTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_focus_user_id"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "repo_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_focus_repo_id"))
    private Repo repo;

    public UserRepoFocusRel(String userId, String repoId, LocalDateTime focusTime) {
        this.userId = userId;
        this.repoId = repoId;
        this.focusTime = focusTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRepoFocusRel that = (UserRepoFocusRel) o;
        return Objects.equals(userId, that.userId) && Objects.equals(repoId, that.repoId) && Objects.equals(focusTime, that.focusTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, repoId, focusTime);
    }

    @Override
    public String toString() {
        return "UserRepoFocusRel{" +
                "userId='" + userId + '\'' +
                ", repoId='" + repoId + '\'' +
                ", focusTime=" + focusTime +
                '}';
    }
}
