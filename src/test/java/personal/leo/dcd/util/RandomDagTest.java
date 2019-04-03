package personal.leo.dcd.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import personal.leo.dcd.entity.Vertex;

/**
 * @author leo
 * @date 2019-03-29
 *
 * src: srouce
 * dest: destination
 */

public class RandomDagTest {
    @Test
    public void test() {
        RandomDag rd = RandomDag.matrix(10, 10, 10);
        List<Vertex> vtxs = rd.draw();
        List<Vertex> allVtxs = rd.getAllVtxs();

        for (Vertex vtx : allVtxs) {
            System.err.println(vtx.getId() + " 1 @10." + vtx.getX() + ",10." + vtx.getY());
        }
        System.out.println();
        for (Vertex vtx : vtxs) {
            System.out.println(vtx.getId() + " " + vtx.getOutNeighborVtxIds().stream().map(String::valueOf)
                .collect(Collectors.joining(" "))
            );
        }
    }
}
