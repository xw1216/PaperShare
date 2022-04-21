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
public class NoteCommentRelPK implements Serializable {
    private Long note_id;
    private Long cmt_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteCommentRelPK that = (NoteCommentRelPK) o;
        return Objects.equals(note_id, that.note_id) && Objects.equals(cmt_id, that.cmt_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(note_id, cmt_id);
    }

    @Override
    public String toString() {
        return "NoteCommentRelPK{" +
                "note_id=" + note_id +
                ", cmt_id=" + cmt_id +
                '}';
    }
}
