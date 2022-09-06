package tech.outspace.papershare.model.entity.objs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import tech.outspace.papershare.utils.general.SnowFlake;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "area")
public class Area implements Serializable {
    @Id
    @Column(name = "id", nullable = false, length = 127)
    private String id;

    @Column(name = "name", nullable = false, unique = true, length = 127)
    private String name;

    @Transient
    private SnowFlake idGen = new SnowFlake(2, 1);

    public Area(String name) {
        this.id = idGen.NextId();
        this.name = name;
    }

    @Override
    public String toString() {
        return "Area{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
