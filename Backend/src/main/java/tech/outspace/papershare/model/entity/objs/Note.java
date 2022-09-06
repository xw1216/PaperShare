package tech.outspace.papershare.model.entity.objs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
@Table(name = "note")
public class Note implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 127)
    private String id;

    @Column(name = "title", length = 1023, nullable = false)
    private String title = "";

    @Column(name = "cont", nullable = false)
    private String cont = "";

    @Column(name = "page", nullable = false)
    private Integer page = 0;

    @Column(name = "repo_id", nullable = false, length = 127)
    private String repoId;

    @Column(name = "paper_id", nullable = false, length = 127)
    private String paperId;

    @Column(name = "user_id", nullable = false, length = 127)
    private String userId;

    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    @ManyToOne
    @JoinColumn(name = "repo_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_note_repo_id"))
    private Repo repo;

    @ManyToOne
    @JoinColumn(name = "paper_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_note_paper_id"))
    private Paper paper;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_note_user_id"))
    private User user;

    @Transient
    private SnowFlake idGen = new SnowFlake(1, 2);

    public Note(String title, String cont, String repoId, String paperId, String userId, Integer page) {
        this.id = idGen.NextId();
        this.title = title;
        this.cont = cont;
        this.repoId = repoId;
        this.paperId = paperId;
        this.userId = userId;
        this.page = page;
        this.createTime = TimeUtil.getUTC();
        this.updateTime = TimeUtil.getUTC();
    }


}
