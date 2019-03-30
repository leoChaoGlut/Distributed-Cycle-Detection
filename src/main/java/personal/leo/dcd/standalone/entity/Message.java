package personal.leo.dcd.standalone.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author leo
 * @date 2019-03-29
 */
@Accessors(chain = true)
@Getter
@Setter
public class Message {
    /**
     * sample:
     * [
     * [1,2,3],
     * [4,5,6]
     * ]
     */
    private List<LinkedHashSet<Vertex>> vertexSequences = new ArrayList<LinkedHashSet<Vertex>>() {
        { add(new LinkedHashSet<>()); }
    };

    public synchronized Message appendToSequences(Vertex vtx) {
        for (LinkedHashSet<Vertex> vertexSequence : vertexSequences) {
            vertexSequence.add(vtx);
        }
        return this;
    }

    public static Vertex min(LinkedHashSet<Vertex> sequence) {
        return sequence
            .stream()
            .min(Comparator.comparing(Vertex::getId))
            .orElse(null);
    }
}
