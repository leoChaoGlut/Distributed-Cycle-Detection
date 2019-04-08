package personal.leo.dcd.impl.distributed.pseudo;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSON;

import com.google.common.base.Stopwatch;
import org.junit.Before;
import org.junit.Test;
import personal.leo.dcd.BaseTest;
import personal.leo.dcd.entity.Vertex;
import personal.leo.dcd.impl.distributed.pseudo.component.VertexHolder;
import personal.leo.dcd.impl.distributed.pseudo.component.Worker;
import personal.leo.dcd.impl.standalone.Standalone;
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
public class PseudoDistributedTest extends BaseTest {

    @Test
    public void fewData() throws InterruptedException, ExecutionException {
        int workerCount = 2;

        Vertex v1 = new Vertex(Id.next());
        Vertex v2 = new Vertex(Id.next());
        Vertex v3 = new Vertex(Id.next());
        Vertex v4 = new Vertex(Id.next());
        Vertex v5 = new Vertex(Id.next());

        v1.out(v2);
        v2.out(v3);
        v3.out(v4).out(v5);
        v4.out(v2);

        List<Vertex> vtxs = Arrays.asList(v1, v2, v3, v4, v5);

        PseudoDistributed.run(workerCount, vtxs);

    }

    @Test
    public void moreData() throws InterruptedException, ExecutionException, IOException {
        int workerCount = 16;

        Stopwatch watch = Stopwatch.createStarted();
        //List<Vertex> vtxs = randomData(10_000_000, 10_000_000);
        List<Vertex> vtxs = jsonData();
        System.out.println(vtxs.size());
        System.out.println(watch.stop());

        createCycle(vtxs);

        watch.reset().start();
        Long cycleCount = PseudoDistributed.run(workerCount, vtxs);
        System.out.println("PseudoDistributed: " + watch.stop() + " - " + cycleCount);

    }

}
