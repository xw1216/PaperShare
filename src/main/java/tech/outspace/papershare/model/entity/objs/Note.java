package tech.outspace.papershare.model.entity.objs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import tech.outspace.papershare.model.entity.rels.NoteCommentRel;

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
@Table(name = "note")
public class Note implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", length = 1023)
    private String content = "";

    @Column(name = "visible", nullable = false)
    private Boolean visible = true;

    @Column(name = "pos_x", nullable = false)
    private String posX;

    @Column(name = "pos_y", nullable = false)
    private Integer pos_Y;

    @Column(name = "repo_id", nullable = false)
    private Long repoId;

    @Column(name = "paper_id", nullable = false)
    private Long paperId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

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

    @OneToMany(mappedBy = "note", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "note", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<NoteCommentRel> commentRels = new ArrayList<>();

    public Note(Long id, String content, Boolean visible, String posX, Integer pos_Y,
                Long repoId, Long paperId, Long userId) {
        this.id = id;
        this.content = content;
        this.visible = visible;
        this.posX = posX;
        this.pos_Y = pos_Y;
        this.repoId = repoId;
        this.paperId = paperId;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", visible=" + visible +
                ", posX='" + posX + '\'' +
                ", pos_Y=" + pos_Y +
                ", repoId=" + repoId +
                ", paperId=" + paperId +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
