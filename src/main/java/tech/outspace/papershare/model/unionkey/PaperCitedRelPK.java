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
public class PaperCitedRelPK implements Serializable {
    private Long source_id;
    private Long cited_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperCitedRelPK that = (PaperCitedRelPK) o;
        return Objects.equals(source_id, that.source_id) && Objects.equals(cited_id, that.cited_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source_id, cited_id);
    }

    @Override
    public String toString() {
        return "PaperCitedRelPK{" +
                "source_id=" + source_id +
                ", cited_id=" + cited_id +
                '}';
    }
}
