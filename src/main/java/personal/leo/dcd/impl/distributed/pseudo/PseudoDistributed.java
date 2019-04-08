package personal.leo.dcd.impl.distributed.pseudo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.junit.Test;
import personal.leo.dcd.entity.Vertex;
import personal.leo.dcd.impl.distributed.pseudo.component.CycleCounter;
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

    private CycleCounter cycleCounter;

    public static Long run(int workerCount, List<Vertex> vtxs) throws InterruptedException, ExecutionException {
        CycleCounter cycleCounter = new CycleCounter();

        List<Worker> workers = new ArrayList<>(workerCount);
        for (int i = 0; i < workerCount; i++) {
            workers.add(new Worker(i, cycleCounter));
        }

        return new PseudoDistributed(workers, vtxs, cycleCounter).doRun();
    }

    private PseudoDistributed() {
        throw new RuntimeException("Use static method 'run' instead");
    }

    private Long doRun() throws InterruptedException, ExecutionException {
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

        return cycleCounter.get();
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

        //VertexHolder.print();
    }

    private void distributedDetect(ExecutorService es, int round, CountDownLatch latch) {
        for (Worker worker : workers) {
            worker.setRound(round);
            worker.setLatch(latch);

            es.submit(worker);
        }
    }

}
