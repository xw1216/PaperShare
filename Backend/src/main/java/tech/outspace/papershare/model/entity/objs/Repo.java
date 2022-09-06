package tech.outspace.papershare.model.entity.objs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import tech.outspace.papershare.model.entity.rels.RepoPaperRel;
import tech.outspace.papershare.model.entity.rels.UserRepoFocusRel;
import tech.outspace.papershare.utils.general.SnowFlake;
import tech.outspace.papershare.utils.time.TimeUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "repo")
public class Repo implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 127)
    private String id;

    @Column(name = "title", nullable = false, length = 127)
    private String name;

    @Column(name = "user_id", nullable = false, length = 127)
    private String userId;

    @Column(name = "cont", length = 511)
    private String cont;

    @Column(name = "visible", nullable = false)
    private Boolean visible = true;

    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "repo", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserRepoFocusRel> focusRels = new ArrayList<>();

    @OneToMany(mappedBy = "repo", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RepoPaperRel> paperRels = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_repo_user_id"))
    private User user;

    @Transient
    private SnowFlake idGen = new SnowFlake(1, 0);

    public Repo(String name, Boolean visible, String cont) {
        this.id = idGen.NextId();
        this.name = name;
        this.visible = visible;
        this.cont = cont;
        this.createTime = TimeUtil.getUTC();
        this.updateTime = TimeUtil.getUTC();
    }

    @Override
    public String toString() {
        return "Repo{" +
                "id='" + id + '\'' +
                ", title='" + name + '\'' +
                ", cont='" + cont + '\'' +
                ", visible=" + visible +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
