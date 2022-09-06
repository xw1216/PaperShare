package tech.outspace.papershare.model.entity.rels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import tech.outspace.papershare.model.entity.objs.Paper;
import tech.outspace.papershare.model.entity.objs.Repo;
import tech.outspace.papershare.model.unionkey.RepoPaperRelPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "rel_repo_paper")
@IdClass(RepoPaperRelPK.class)
public class RepoPaperRel implements Serializable {
    @Id
    @Column(name = "repo_id", nullable = false, length = 127)
    private String repoId;

    @Id
    @Column(name = "paper_id", nullable = false, length = 127)
    private String paperId;


    @ManyToOne
    @JoinColumn(name = "repo_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_repo_paper_repo"))
    private Repo repo;

    @ManyToOne
    @JoinColumn(name = "paper_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rel_repo_paper_paper"))
    private Paper paper;

    public RepoPaperRel(String repoId, String paperId) {
        this.repoId = repoId;
        this.paperId = paperId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepoPaperRel that = (RepoPaperRel) o;
        return Objects.equals(repoId, that.repoId) && Objects.equals(paperId, that.paperId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repoId, paperId);
    }

    @Override
    public String toString() {
        return "RepoPaperRel{" +
                "repoId='" + repoId + '\'' +
                ", paperId='" + paperId + '\'' +
                '}';
    }
}
