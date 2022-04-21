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
public class PaperAuthorRelPK implements Serializable {
    private Long paper_id;
    private Long author_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperAuthorRelPK that = (PaperAuthorRelPK) o;
        return Objects.equals(paper_id, that.paper_id) && Objects.equals(author_id, that.author_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paper_id, author_id);
    }

    @Override
    public String toString() {
        return "PaperAuthorPelPK{" +
                "paper_id=" + paper_id +
                ", author_id=" + author_id +
                '}';
    }
}
