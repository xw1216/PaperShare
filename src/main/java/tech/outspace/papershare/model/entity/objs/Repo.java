package tech.outspace.papershare.model.entity.objs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import tech.outspace.papershare.model.entity.rels.RepoPaperRel;
import tech.outspace.papershare.model.entity.rels.UserRepoFocusRel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "repo")
public class Repo implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Column(name = "visible", nullable = false)
    private Boolean visible = true;

    @Column(name = "description", nullable = false, length = 511)
    private String description;

    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "repo", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "repo", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserRepoFocusRel> focusRels = new ArrayList<>();

    @OneToMany(mappedBy = "repo", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RepoPaperRel> paperRels = new ArrayList<>();

    public Repo(Long id, String name, Boolean visible, String description) {
        this.id = id;
        this.name = name;
        this.visible = visible;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Repo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", visible=" + visible +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
