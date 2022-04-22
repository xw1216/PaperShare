package tech.outspace.papershare.model.entity.objs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import tech.outspace.papershare.model.entity.rels.PaperAuthorRel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "author")
public class Author implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "popular")
    private Long popular = 0L;

    @Column(name = "orcid", length = 31)
    private String orcid;

    @Column(name = "wosid")
    private String wosid;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PaperAuthorRel> paperRels = new ArrayList<>();

    public Author(Long id, String name, String email, Long popular, String orcid, String wosid) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.popular = popular;
        this.orcid = orcid;
        this.wosid = wosid;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", popular=" + popular +
                ", orcid='" + orcid + '\'' +
                ", wosid='" + wosid + '\'' +
                '}';
    }
}
