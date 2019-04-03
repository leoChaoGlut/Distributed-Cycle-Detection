package personal.leo.dcd.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
        List<Vertex> relations = rd.draw();
        List<Vertex> allVtxsWithoutNeighbors = rd.getAllVtxsWithoutNeighbors();

        writeDagitty(relations, allVtxsWithoutNeighbors);
        writeRelations(relations);
        writeAllVtxsWithoutNeighbors(allVtxsWithoutNeighbors);

    }

    @Test
    public void randomDagByLine() {
        RandomDag rd = RandomDag.line(10, 20);
        List<Vertex> relations = rd.draw();
        List<Vertex> allVtxsWithoutNeighbors = rd.getAllVtxsWithoutNeighbors();

        System.out.println(relations);
        System.out.println(allVtxsWithoutNeighbors);
        System.out.println(JSON.toJSONString(relations));
    }

    @Test
    public void buildCycle() throws IOException {
        List<Vertex> relations = JSON.parseArray(read(relationsFilePath), Vertex.class);
        List<Vertex> allVtxsWithoutNeighbors = JSON.parseArray(read(allVtxsWithoutNeighborsFilePath), Vertex.class);

        Map<Long, Vertex> relationMap = relations.stream()
            .distinct()
            .collect(Collectors.toMap(
                Vertex::getId,
                Function.identity()
            ));
        Map<Long, Vertex> allVtxsWithoutNeighborsMap = allVtxsWithoutNeighbors.stream()
            .distinct()
            .collect(Collectors.toMap(
                Vertex::getId,
                Function.identity()
            ));

        Vertex v1 = relationMap.get(4211L);
        Vertex v2 = allVtxsWithoutNeighborsMap.get(4412L);
        Vertex v3 = allVtxsWithoutNeighborsMap.get(4324L);

        v2.out(v1);
        v3.out(v2);

        relations.add(v2);
        relations.add(v3);

        writeRelations(relations);
        writeAllVtxsWithoutNeighbors(allVtxsWithoutNeighbors);
        writeDagitty(relations, allVtxsWithoutNeighbors);
    }

    @Test
    public void test() throws IOException {
        String json = read(relationsFilePath);
        List<Vertex> relations = JSON.parseArray(json, Vertex.class);
        System.out.println(relations);
    }

    private void writeAllVtxsWithoutNeighbors(List<Vertex> allVtxsWithoutNeighbors) throws IOException {
        String json = JSON.toJSONString(allVtxsWithoutNeighbors);
        System.out.println(json);
        write(
            allVtxsWithoutNeighborsFilePath,
            json
        );
    }

    private void writeRelations(List<Vertex> relations) throws IOException {
        String json = JSON.toJSONString(relations);
        System.out.println(json);
        write(
            relationsFilePath,
            json
        );
    }

    /**
     * 将输出的内容放到 http://www.dagitty.net/dags.html#
     * 可将生成的 dag 可视化展现
     *
     * @param relations
     * @param allVtxsWithoutNeighbors
     */
    private void writeDagitty(List<Vertex> relations, List<Vertex> allVtxsWithoutNeighbors) throws IOException {
        StringBuilder txt = new StringBuilder();
        for (Vertex vtxWithoutNeighbors : allVtxsWithoutNeighbors) {
            txt.append(
                vtxWithoutNeighbors.getId() + " 1 @10." + vtxWithoutNeighbors.getX() + ",10."
                    + vtxWithoutNeighbors.getY() + "\n"
            );
        }

        txt.append("\n");

        for (Vertex relation : relations) {
            String outVtxIds = relation.getOutNeighborVtxIds()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));

            txt.append(relation.getId() + " " + outVtxIds + "\n");
        }

        System.out.println(txt);

        write(
            dagittyFilePath,
            txt.toString()
        );

    }

}
