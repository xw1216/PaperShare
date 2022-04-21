package tech.outspace.papershare.model.entity.objs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import tech.outspace.papershare.model.entity.rels.UserAreaRel;
import tech.outspace.papershare.model.entity.rels.UserRepoFocusRel;
import tech.outspace.papershare.model.enumerate.ERole;
import tech.outspace.papershare.model.enumerate.EUserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "user", indexes = {@Index(name = "user_email_index", columnList = "email")})
public class User implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "pass", nullable = false, length = 511)
    private String pass;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private ERole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EUserStatus status;

    @Column(name = "orcid", length = 31, nullable = false, unique = true)
    private String orcid;

    @CreatedDate
    @Column(name = "signup_time", nullable = false)
    private LocalDateTime signUpTime;

    @Column(name = "signin_time", nullable = false)
    private LocalDateTime signInTime;

    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = {CascadeType.REMOVE})
    private Session session;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserRepoFocusRel> focusRels = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserAreaRel> areaRels = new ArrayList<>();

    public User(Long id, String email, String name, String pass, ERole role, EUserStatus status, String orcid) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.pass = pass;
        this.role = role;
        this.status = status;
        this.orcid = orcid;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                ", role=" + role +
                ", status=" + status +
                ", orcid='" + orcid + '\'' +
                ", signUpTime=" + signUpTime +
                ", signInTime=" + signInTime +
                '}';
    }
}
