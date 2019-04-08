package personal.leo.dcd.impl.standalone;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;

import com.google.common.base.Stopwatch;
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

    @Test
    public void main() throws IOException {
        List<Vertex> vtxs = jsonData();

        Stopwatch watch = Stopwatch.createStarted();
        long cycleCount = Standalone.run(vtxs);
        System.out.println("Standalone: " + watch.stop() + " - " + cycleCount);
    }

}
