package personal.leo.dcd.impl.distributed.pseudo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.junit.Test;
import personal.leo.dcd.entity.Vertex;
import personal.leo.dcd.impl.distributed.pseudo.component.VertexHolder;
import personal.leo.dcd.impl.distributed.pseudo.component.Worker;
import personal.leo.dcd.util.Id;
import personal.leo.dcd.util.RandomDag;

/**
 * @author leo
 * @date 2019-03-29
 */
public class PseudoDistributedTest {
    @Test
    public void fewData() throws InterruptedException {
        int workerCount = 2;
        Worker w1 = new Worker(1);
        Worker w2 = new Worker(2);

        Vertex v1 = new Vertex(Id.next());
        Vertex v2 = new Vertex(Id.next());
        Vertex v3 = new Vertex(Id.next());
        Vertex v4 = new Vertex(Id.next());
        Vertex v5 = new Vertex(Id.next());

        v1.out(v2);
        v2.out(v3);
        v3.out(v4).out(v5);
        v4.out(v2);

        VertexHolder.put(w1.getId(), Arrays.asList(v1, v2));
        VertexHolder.put(w2.getId(), Arrays.asList(v3, v4, v5));

        ExecutorService es = Executors.newFixedThreadPool(2);

        int round = 0;
        while (VertexHolder.anyActive()) {
            CountDownLatch latch = new CountDownLatch(workerCount);

            distributedDetect(w1, w2, es, round, latch);

            //在分布式环境下,需要一个 coordinator 来控制所有 worker 何时走向下一轮,一致性可通过 db 保证.
            latch.await();

            round++;
        }

    }

    private void distributedDetect(Worker w1, Worker w2, ExecutorService es, int round, CountDownLatch latch) {
        w1.setRound(round);
        w1.setLatch(latch);

        w2.setRound(round);
        w2.setLatch(latch);

        es.submit(w1);
        es.submit(w2);
    }

    @Test
    public void moreData() {
        int workerCount = 10;
        int vtxCount = 100_000;

    }

}
