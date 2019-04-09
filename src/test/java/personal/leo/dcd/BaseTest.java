package personal.leo.dcd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import personal.leo.dcd.entity.Vertex;
import personal.leo.dcd.util.Id;
import personal.leo.dcd.util.RandomDag;

/**
 * @author 谦扬(qianyang).廖超(liaochao).leo
 * @date 2019-04-03
 */
public class BaseTest {

    protected String allVtxsWithoutNeighborsFilePath
        = BaseTest.class.getClassLoader().getResource("dag/allVtxsWithoutNeighbors.json").getPath();

    protected String relationsFilePath
        = BaseTest.class.getClassLoader().getResource("dag/relations.json").getPath();

    protected String dagittyFilePath
        = BaseTest.class.getClassLoader().getResource("dag/dagitty.txt").getPath();

    protected String read(String filePath) throws IOException {
        return IOUtils.toString(new FileInputStream(filePath), StandardCharsets.UTF_8);
    }

    protected void write(String filePath, String fileContent) throws IOException {
        IOUtils.write(fileContent, new FileOutputStream(filePath), StandardCharsets.UTF_8);
    }

    /**
     * 论文中提供的简单数据
     *
     * @return
     */
    protected List<Vertex> simpleData() {
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

    protected List<Vertex> randomData() {
        return randomData(10, 10);
    }

    /**
     * 随机数据
     *
     * @param maxNodeCount
     * @param edgeCount
     * @return
     */
    protected List<Vertex> randomData(int maxNodeCount, int edgeCount) {
        RandomDag rd = RandomDag.line(maxNodeCount, edgeCount);
        return rd.draw();
    }

    /**
     * @param vtxs
     * @param expectedCycleCount 期望有多少个环,但是不一定保证有这么多个.
     */
    protected void createCycle(List<Vertex> vtxs, int expectedCycleCount) {
        Vertex rootVtx = null;
        for (Vertex vtx : vtxs) {
            if (vtx.getId() == -1L) {
                rootVtx = vtx;
                break;
            }
        }

        if (rootVtx == null) {
            throw new RuntimeException("Cannot found root vertex");
        }

        for (int i = 0; i < expectedCycleCount; i++) {
            Vertex rdVtx = vtxs.get(RandomUtils.nextInt(0, vtxs.size()));
            rdVtx.out(rootVtx);
            System.out.println("Create cycle between " + rdVtx.getId() + " and " + rootVtx.getId());
        }

    }

    /**
     * 这是已经提前测试好,准备好的一份数据,有一定代表性,数据量不多也不少
     *
     * @return
     * @throws IOException
     */
    protected List<Vertex> jsonData() throws IOException {
        String json = read(relationsFilePath);
        return JSON.parseArray(json, Vertex.class);
    }
}
