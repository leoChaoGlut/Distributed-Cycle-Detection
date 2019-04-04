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

}
