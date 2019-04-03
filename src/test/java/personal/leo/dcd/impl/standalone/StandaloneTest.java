package personal.leo.dcd.impl.standalone;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;

import org.junit.Before;
import org.junit.Test;
import personal.leo.dcd.BaseTest;
import personal.leo.dcd.entity.Vertex;
import personal.leo.dcd.util.Id;
import personal.leo.dcd.util.RandomDag;

/**
 * @author leo
 * @date 2019-03-29
 * Dcd: Distributed Cycle Detection
 * vtx: Vertex
 * msg: Message
 * Iter: Iterator
 * seq: Sequence
 */
public class StandaloneTest extends BaseTest {

    Set<Vertex> activeVtxs = new LinkedHashSet<>();

    /**
     * 在 main() 之前执行
     */
    @Before
    public void before() throws IOException {
        //List<Vertex> vtxs = simpleData();
        //List<Vertex> vtxs = randomData();
        List<Vertex> vtxs = jsonData();

        activeVtxs.addAll(vtxs);
    }

    @Test
    public void main() {
        Standalone.run(activeVtxs);
    }

    private List<Vertex> simpleData() {
        Vertex v1 = new Vertex(Id.next());
        Vertex v2 = new Vertex(Id.next());
        Vertex v3 = new Vertex(Id.next());
        Vertex v4 = new Vertex(Id.next());
        Vertex v5 = new Vertex(Id.next());

        v1.out(v2);
        v2.out(v3);
        v3.out(v4).out(v5);
        v4.out(v2);

        return Arrays.asList(v1, v2, v3, v4, v5);
    }

    private List<Vertex> randomData() {
        RandomDag rd = RandomDag.line(10, 10);
        return rd.draw();
    }

    private List<Vertex> jsonData() throws IOException {
        String json = read(relationsFilePath);
        return JSON.parseArray(json, Vertex.class);
    }

}
