package personal.leo.dcd.standalone.entity;

import java.util.LinkedHashSet;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author leo
 * @date 2019-03-29
 */
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class Vertex {
    private Long id;
    private LinkedHashSet<Long> inNeighborsId = new LinkedHashSet<>();
    private LinkedHashSet<Long> outNeighborsId = new LinkedHashSet<>();
    private boolean active = true;

    public Vertex(Long id) {
        this.id = id;
    }

    public synchronized Vertex in(Vertex vertex) {
        inNeighborsId.add(vertex.id);
        return this;
    }

    public synchronized Vertex out(Vertex vertex) {
        outNeighborsId.add(vertex.id);
        return this;
    }

}
