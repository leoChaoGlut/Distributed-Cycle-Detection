package personal.leo.dcd.util;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import org.junit.Test;
import personal.leo.dcd.BaseTest;
import personal.leo.dcd.entity.Vertex;

/**
 * @author leo
 * @date 2019-03-29
 *
 * src: srouce
 * dest: destination
 */

public class RandomDagTest extends BaseTest {

    @Test
    public void randomDagByMatrix() throws IOException {
        RandomDag rd = RandomDag.matrix(50, 50, 30);
        List<Vertex> vtxs = rd.draw();
        List<Vertex> allVtxsWithoutNeighbors = rd.getAllVtxsWithoutNeighbors();

        writeDagitty(vtxs, allVtxsWithoutNeighbors);
        writeVtxs(vtxs);
        writeAllVtxsWithoutNeighbors(allVtxsWithoutNeighbors);

    }

    @Test
    public void randomDagByLine() {
        RandomDag rd = RandomDag.line(10, 20);
        List<Vertex> vtxs = rd.draw();
        List<Vertex> allVtxsWithoutNeighbors = rd.getAllVtxsWithoutNeighbors();

        System.out.println(vtxs);
        System.out.println(allVtxsWithoutNeighbors);
        System.out.println(JSON.toJSONString(vtxs));
    }

    @Test
    public void updateVtxs() throws IOException {
        List<Vertex> vtxs = JSON.parseArray(read(vtxsFilePath), Vertex.class);
        List<Vertex> allVtxsWithoutNeighbors = JSON.parseArray(read(allVtxsWithoutNeighborsFilePath), Vertex.class);

    }

    private void writeAllVtxsWithoutNeighbors(List<Vertex> allVtxsWithoutNeighbors) throws IOException {
        String json = JSON.toJSONString(allVtxsWithoutNeighbors);
        System.out.println(json);
        write(
            allVtxsWithoutNeighborsFilePath,
            json
        );
    }

    private void writeVtxs(List<Vertex> vtxs) throws IOException {
        String json = JSON.toJSONString(vtxs);
        System.out.println(json);
        write(
            vtxsFilePath,
            json
        );
    }

    /**
     * 将输出的内容放到 http://www.dagitty.net/dags.html#
     * 可将生成的 dag 可视化展现
     *
     * @param vtxs
     * @param allVtxsWithoutNeighbors
     */
    private void writeDagitty(List<Vertex> vtxs, List<Vertex> allVtxsWithoutNeighbors) throws IOException {
        StringBuilder txt = new StringBuilder();
        for (Vertex vtx : allVtxsWithoutNeighbors) {
            txt.append(vtx.getId() + " 1 @10." + vtx.getX() + ",10." + vtx.getY() + "\n");
        }

        txt.append("\n");

        for (Vertex vtx : vtxs) {
            String outVtxIds = vtx.getOutNeighborVtxIds()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));

            txt.append(vtx.getId() + " " + outVtxIds + "\n");
        }

        System.out.println(txt);

        write(
            dagittyFilePath,
            txt.toString()
        );

    }

}
