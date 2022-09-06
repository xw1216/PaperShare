package tech.outspace.papershare.model.entity.objs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import tech.outspace.papershare.utils.general.SnowFlake;
import tech.outspace.papershare.utils.time.TimeUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 127)
    private String id;

    @Column(name = "cont", nullable = false, length = 255)
    private String cont;

    @Column(name = "user_id", nullable = false, length = 127)
    private String userId;

    @Column(name = "paper_id", nullable = false, length = 127)
    private String paperId;

    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    @ManyToOne
    @JoinColumn(name = "paper_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_paper_repo_id"))
    private Paper paper;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_comment_user_id"))
    private User user;

    @Transient
    private SnowFlake idGen = new SnowFlake(2, 0);

    public Comment(String cont, String userId, String paperId) {
        this.id = idGen.NextId();
        this.cont = cont;
        this.userId = userId;
        this.paperId = paperId;
        this.createTime = TimeUtil.getUTC();
        this.updateTime = TimeUtil.getUTC();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", cont='" + cont + '\'' +
                ", userId='" + userId + '\'' +
                ", repoId='" + paperId + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
