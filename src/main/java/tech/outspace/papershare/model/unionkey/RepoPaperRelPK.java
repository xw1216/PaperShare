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
public class RepoPaperRelPK implements Serializable {
    private Long repo_id;
    private Long paper_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepoPaperRelPK that = (RepoPaperRelPK) o;
        return Objects.equals(repo_id, that.repo_id) && Objects.equals(paper_id, that.paper_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repo_id, paper_id);
    }

    @Override
    public String toString() {
        return "RepoPaperRelPK{" +
                "repo_id=" + repo_id +
                ", paper_id=" + paper_id +
                '}';
    }
}
