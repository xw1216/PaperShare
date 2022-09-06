package tech.outspace.papershare.model.unionkey;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class RepoPaperRelPK implements Serializable {
    private String repoId;
    private String paperId;

    public RepoPaperRelPK(String repoId, String paperId) {
        this.repoId = repoId;
        this.paperId = paperId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepoPaperRelPK that = (RepoPaperRelPK) o;
        return Objects.equals(repoId, that.repoId) && Objects.equals(paperId, that.paperId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repoId, paperId);
    }

    @Override
    public String toString() {
        return "RepoPaperRelPK{" +
                "repoId='" + repoId + '\'' +
                ", paperId='" + paperId + '\'' +
                '}';
    }
}
