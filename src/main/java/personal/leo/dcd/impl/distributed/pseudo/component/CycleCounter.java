package personal.leo.dcd.impl.distributed.pseudo.component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author 谦扬(qianyang).廖超(liaochao).leo
 * @date 2019-04-08
 */
public class CycleCounter {
    private AtomicLong cycleCount = new AtomicLong(0L);

    public long increAndGet() {
        return cycleCount.incrementAndGet();
    }

    public long get() {
        return cycleCount.get();
    }
}
