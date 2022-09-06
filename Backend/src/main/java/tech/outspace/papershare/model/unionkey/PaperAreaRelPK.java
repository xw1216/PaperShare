package tech.outspace.papershare.model.unionkey;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class PaperAreaRelPK implements Serializable {
    private String paperId;
    private String areaId;

    public PaperAreaRelPK(String paperId, String areaId) {
        this.paperId = paperId;
        this.areaId = areaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperAreaRelPK that = (PaperAreaRelPK) o;
        return Objects.equals(paperId, that.paperId) && Objects.equals(areaId, that.areaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paperId, areaId);
    }

    @Override
    public String toString() {
        return "PaperAreaRelPK{" +
                "paperId='" + paperId + '\'' +
                ", areaId='" + areaId + '\'' +
                '}';
    }
}
