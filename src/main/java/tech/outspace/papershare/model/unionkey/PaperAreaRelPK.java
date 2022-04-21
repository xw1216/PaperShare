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
public class PaperAreaRelPK implements Serializable {
    private Long paper_id;
    private Long area_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperAreaRelPK that = (PaperAreaRelPK) o;
        return Objects.equals(paper_id, that.paper_id) && Objects.equals(area_id, that.area_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paper_id, area_id);
    }

    @Override
    public String toString() {
        return "PaperAreaRelPK{" +
                "paper_id=" + paper_id +
                ", area_id=" + area_id +
                '}';
    }
}
