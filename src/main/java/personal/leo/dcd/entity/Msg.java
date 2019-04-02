package personal.leo.dcd.entity;

import java.util.LinkedHashSet;

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
public class Msg {

    /**
     * vtxSeq: Vertex Sequence,
     * example: 1,2,3
     */
    private LinkedHashSet<Long> vtxSeq = new LinkedHashSet<>();

    public synchronized Msg appendToSeq(Long vtxId) {
        vtxSeq.add(vtxId);
        return this;
    }

    public synchronized Long firstVtxId() {
        return vtxSeq.iterator().next();
    }

    public static Long min(LinkedHashSet<Long> seq) {
        return seq
            .stream()
            .min(Long::compareTo)
            .orElse(null);
    }
}
