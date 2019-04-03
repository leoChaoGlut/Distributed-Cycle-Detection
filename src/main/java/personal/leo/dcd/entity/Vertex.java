package personal.leo.dcd.entity;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

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
public class Vertex {
    private long id;
    /**
     * 当前节点的入节点
     */
    private LinkedHashSet<Long> inNeighborVtxIds = new LinkedHashSet<>();
    /**
     * 当前节点的出节点
     */
    private LinkedHashSet<Long> outNeighborVtxIds = new LinkedHashSet<>();
    private boolean active = true;

    /**
     * 绘图时使用
     * http://www.dagitty.net/dags.html
     */
    private int x, y;

    public Vertex(long id) {
        this.id = id;
    }

    public synchronized Vertex in(Vertex vertex) {
        inNeighborVtxIds.add(vertex.id);
        return this;
    }

    public synchronized Vertex out(Vertex vertex) {
        outNeighborVtxIds.add(vertex.id);
        return this;
    }

    public synchronized Vertex copy() {
        return new Vertex(this.id)
            .setX(this.x)
            .setY(this.y);
    }

    @Override
    public String toString() {
        String in = inNeighborVtxIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        String out = outNeighborVtxIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        return "[" + in + "->" + id + "->" + out + "]";
    }
}
