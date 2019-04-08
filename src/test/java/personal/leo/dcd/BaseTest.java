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

    protected String allVtxsWithoutNeighborsFilePath =
        "/Users/leo/gitRepo/github/Distributed-Cycle-Detection/src/test/resources/dag/allVtxsWithoutNeighbors.json";

    protected String relationsFilePath
        = "/Users/leo/gitRepo/github/Distributed-Cycle-Detection/src/test/resources/dag/relations.json";

    protected String dagittyFilePath
        = "/Users/leo/gitRepo/github/Distributed-Cycle-Detection/src/test/resources/dag/dagitty.txt";

    protected String read(String filePath) throws IOException {
        return IOUtils.toString(new FileInputStream(filePath), StandardCharsets.UTF_8);
    }

    protected void write(String filePath, String fileContent) throws IOException {
        IOUtils.write(fileContent, new FileOutputStream(filePath), StandardCharsets.UTF_8);
    }

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

    protected List<Vertex> randomData(int maxNodeCount, int edgeCount) {
        RandomDag rd = RandomDag.line(maxNodeCount, edgeCount);
        return rd.draw();
    }

    protected void createCycle(List<Vertex> vtxs) {
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

        Vertex rdVtx = vtxs.get(RandomUtils.nextInt(0, vtxs.size()));

        rdVtx.out(rootVtx);
    }

    protected List<Vertex> jsonData() throws IOException {
        String json = read(relationsFilePath);
        return JSON.parseArray(json, Vertex.class);
    }
}
