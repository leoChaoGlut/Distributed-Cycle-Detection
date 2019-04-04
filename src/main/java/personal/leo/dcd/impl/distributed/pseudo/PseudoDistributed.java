package personal.leo.dcd.impl.distributed.pseudo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.junit.Test;
import personal.leo.dcd.entity.Vertex;
import personal.leo.dcd.impl.distributed.pseudo.component.VertexHolder;
import personal.leo.dcd.impl.distributed.pseudo.component.Worker;
import personal.leo.dcd.util.Id;
import personal.leo.dcd.util.RandomDag;

/**
 * @author leo
 * @date 2019-03-29
 * seg: segment
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PseudoDistributed {

    private List<Worker> workers;
    private List<Vertex> vtxs;

    public static void run(int workerCount, List<Vertex> vtxs) throws InterruptedException {
        List<Worker> workers = new ArrayList<>(workerCount);
        for (int i = 0; i < workerCount; i++) {
            workers.add(new Worker(i));
        }

        new PseudoDistributed(workers, vtxs).doRun();
    }

    private PseudoDistributed() {
        throw new RuntimeException("Use static method 'run' instead");
    }

    private void doRun() throws InterruptedException {
        dispatchJobs();

        int round = 0;
        ExecutorService es = Executors.newFixedThreadPool(workers.size());

        while (VertexHolder.anyActive()) {
            CountDownLatch latch = new CountDownLatch(workers.size());

            distributedDetect(es, round, latch);

            //在分布式环境下,需要一个 coordinator 来控制所有 worker 何时走向下一轮,一致性可通过 db 保证.
            latch.await();

            round++;
        }
    }

    private void dispatchJobs() {
        if (vtxs.size() < workers.size()) {
            for (int i = 0; i < vtxs.size(); i++) {
                VertexHolder.put(workers.get(i).getId(), Collections.singletonList(vtxs.get(i)));
            }
        } else {
            int segSize = vtxs.size() / workers.size() + 1;
            for (int i = 0, workerIndex = 0; ; i += segSize, workerIndex++) {
                int beginIndex = i;
                int endIndex = i + segSize;

                if (endIndex < vtxs.size()) {
                    VertexHolder.put(workers.get(workerIndex).getId(), vtxs.subList(beginIndex, endIndex));
                } else {
                    VertexHolder.put(workers.get(workerIndex).getId(), vtxs.subList(beginIndex, vtxs.size()));
                    break;
                }
            }
        }
    }

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

        List<Worker> workers = Arrays.asList(w1, w2);

        ExecutorService es = Executors.newFixedThreadPool(2);

        int round = 0;
        while (VertexHolder.anyActive()) {
            CountDownLatch latch = new CountDownLatch(workerCount);

            distributedDetect(es, round, latch);

            //在分布式环境下,需要一个 coordinator 来控制所有 worker 何时走向下一轮,一致性可通过 db 保证.
            latch.await();

            round++;
        }

    }

    private void distributedDetect(ExecutorService es, int round, CountDownLatch latch) {
        for (Worker worker : workers) {
            worker.setRound(round);
            worker.setLatch(latch);

            es.submit(worker);
        }
    }

    @Test
    public void moreData() {
        int workerCount = 10;
        int vtxCount = 100_000;

    }

}
