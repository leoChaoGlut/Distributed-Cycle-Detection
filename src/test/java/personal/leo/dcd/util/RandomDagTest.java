package personal.leo.dcd.util;

import java.util.List;
import java.util.stream.Collectors;

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
    public void randomDagByMatrix() {
        RandomDag rd = RandomDag.matrix(100, 10, 20);
        List<Vertex> vtxs = rd.draw();
        List<Vertex> allVtxsWithoutNeighbors = rd.getAllVtxsWithoutNeighbors();

        printDagitty(vtxs, allVtxsWithoutNeighbors);

    }

    /**
     * 将输出的内容放到 http://www.dagitty.net/dags.html#
     * 可将生成的 dag 可视化展现
     *
     * @param vtxs
     * @param allVtxsWithoutNeighbors
     */
    private void printDagitty(List<Vertex> vtxs, List<Vertex> allVtxsWithoutNeighbors) {
        for (Vertex vtx : allVtxsWithoutNeighbors) {
            System.err.println(vtx.getId() + " 1 @10." + vtx.getX() + ",10." + vtx.getY());
        }
        System.out.println();
        for (Vertex vtx : vtxs) {
            System.out.println(vtx.getId() + " " + vtx.getOutNeighborVtxIds().stream().map(String::valueOf)
                .collect(Collectors.joining(" "))
            );
        }
    }

    @Test
    public void randomDagByLine() {
        RandomDag rd = RandomDag.line(10, 10);
        List<Vertex> vtxs = rd.draw();
        List<Vertex> allVtxsWithoutNeighbors = rd.getAllVtxsWithoutNeighbors();

        System.out.println(vtxs);
        System.out.println(allVtxsWithoutNeighbors);

    }
}
