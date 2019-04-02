package personal.leo.dcd.impl.distributed.pseudo.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import personal.leo.dcd.entity.Vertex;

/**
 * @author leo
 * @date 2019-03-29
 */
public class VertexHolder {
    /**
     * key: worker id
     * value: 所属这个 worker 的 vtxs
     */
    private static Map<Integer, List<Vertex>> vtxHolder = new HashMap<>();

    public static synchronized List<Vertex> fetchActived(int workerId) {
        if (vtxHolder.containsKey(workerId)) {
            return vtxHolder.get(workerId)
                .stream()
                .filter(Vertex::isActive)
                .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Worker not exists.");
        }
    }

    public static synchronized void put(int workerId, List<Vertex> vtxs) {
        if (vtxHolder.containsKey(workerId)) {
            if (CollectionUtils.isNotEmpty(vtxs)) {
                vtxHolder.get(workerId).addAll(vtxs);
            }
        } else {
            vtxHolder.put(
                workerId,
                new ArrayList<>(vtxs)
            );
        }
    }

    public static synchronized boolean anyActive() {
        return vtxHolder.values()
            .stream()
            .flatMap(Collection::stream)
            .anyMatch(Vertex::isActive);
    }
}
