package tech.outspace.papershare.model.entity.objs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
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
@Table(name = "comment")
public class Comment implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "cont", nullable = false)
    private String cont;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "note_id", nullable = false)
    private String noteId;

    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    @ManyToOne
    @JoinColumn(name = "note_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_comment_user_id"))
    private Note note;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_comment_note_id"))
    private User user;

    @OneToMany(mappedBy = "comment", orphanRemoval = true)
    private List<NoteCommentRel> noteRels = new ArrayList<>();

    public Comment(Long id, String cont, Long userId, String noteId) {
        this.id = id;
        this.cont = cont;
        this.userId = userId;
        this.noteId = noteId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", cont='" + cont + '\'' +
                ", userId=" + userId +
                ", noteId='" + noteId + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
