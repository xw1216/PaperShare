package tech.outspace.papershare.model.entity.rels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import tech.outspace.papershare.model.entity.objs.Comment;
import tech.outspace.papershare.model.entity.objs.Note;
import tech.outspace.papershare.model.unionkey.NoteCommentRelPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "rel_note_comment")
@IdClass(NoteCommentRelPK.class)
public class NoteCommentRel implements Serializable {
    @Id
    @Column(name = "note_id", nullable = false)
    private Long noteId;

    @Id
    @Column(name = "cmt_id", nullable = false)
    private Long cmtId;

    @ManyToOne
    @JoinColumn(name = "note_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_note_comment_note_id"))
    private Note note;

    @ManyToOne
    @JoinColumn(name = "cmt_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_note_comment_cmt_id"))
    private Comment comment;

    public NoteCommentRel(Long noteId, Long cmt_id) {
        this.noteId = noteId;
        this.cmtId = cmt_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteCommentRel that = (NoteCommentRel) o;
        return Objects.equals(noteId, that.noteId) && Objects.equals(cmtId, that.cmtId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteId, cmtId);
    }

    @Override
    public String toString() {
        return "NoteCommentRel{" +
                "note_id=" + noteId +
                ", cmt_id=" + cmtId +
                '}';
    }
}
