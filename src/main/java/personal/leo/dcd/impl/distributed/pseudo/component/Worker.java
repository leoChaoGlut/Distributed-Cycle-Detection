package personal.leo.dcd.impl.distributed.pseudo.component;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import personal.leo.dcd.entity.Msg;
import personal.leo.dcd.entity.MsgId;
import personal.leo.dcd.entity.Vertex;
import personal.leo.dcd.util.MsgBus;

/**
 * @author leo
 * @date 2019-03-29
 */
@Getter
public class Worker implements Runnable {

    private CycleCounter cycleCounter;
    private int id;
    @Setter
    private CountDownLatch latch;
    @Setter
    private int round;

    public Worker(int id, CycleCounter cycleCounter) {
        this.id = id;
        this.cycleCounter = cycleCounter;
    }

    @Override
    public void run() {
        try {
            List<Vertex> activeVtxs = VertexHolder.fetchActived(id);
            for (Vertex activeVtx : activeVtxs) {
                detect(round, activeVtx);
            }
        } finally {
            latch.countDown();
        }

    }

    public void detect(int round, Vertex curVtx) {
        //如果是第0轮,只需要把当前 vertex 的 id 作为 msg 发送给它的所有 outNeighbors
        if (round == 0) {
            firstRound(round, curVtx);
        } else {
            notFirstRound(round, curVtx);
        }
    }

    private void notFirstRound(int round, Vertex curVtx) {
        long curVtxId = curVtx.getId();
        List<Msg> msgs = MsgBus.get(new MsgId(curVtxId, round));

        //如果节点没有收到消息,代表该节点需要被 halt
        if (CollectionUtils.isEmpty(msgs)) {
            curVtx.setActive(false);
        } else {
            Iterator<Msg> msgIter = msgs.iterator();
            while (msgIter.hasNext()) {
                Msg msg = msgIter.next();
                LinkedHashSet<Long> vtxSeq = msg.getVtxSeq();

                if (Objects.equals(msg.firstVtxId(), curVtxId)) {
                    if (curVtxEqualsTheMinVtxOfAFoundCycle(vtxSeq, curVtx.getId())) {
                        //throw new RuntimeException("Circular found: " + vtxSeqStr + "->" + curVtx.getId());
                        long cycCount = cycleCounter.increAndGet();
                        System.out.println(
                            Thread.currentThread().getId() + "-" +
                                "Worker-" + id + " found a cycle:" + vtxSeq + "-" + curVtx.getId()
                                + ",current count:"
                                + cycCount
                        );
                    } else {
                        //discard,环已经被检测过
                        //System.out.println("Cycle is already found:" + vtxSeq + "-" + curVtx.getId());
                    }
                } else {
                    if (vtxSeq.contains(curVtxId)) {
                        //discard,环已经被检测过
                    } else {
                        for (Long outNeighborVtxId : curVtx.getOutNeighborVtxIds()) {
                            MsgBus.append(
                                new MsgId(outNeighborVtxId, round + 1),
                                msg.appendToSeq(curVtxId)
                            );
                        }
                    }
                }
                //消息消费完后删除
                msgIter.remove();
            }

        }
    }

    private void firstRound(int round, Vertex curVtx) {
        Long curVtxId = curVtx.getId();

        for (Long outNeighborVtxId : curVtx.getOutNeighborVtxIds()) {
            if (MsgBus.contains(new MsgId(outNeighborVtxId, round))) {
                throw new RuntimeException(
                    "The first round, msgBus should be empty,bug got vertex id:" + outNeighborVtxId
                );
            }

            MsgBus.append(
                new MsgId(outNeighborVtxId, round + 1),
                new Msg().appendToSeq(curVtxId)
            );
        }
    }

    /**
     * 判断当前节点是否是一个已发现的环中的最小节点.从而排除多次发现同一个环
     *
     * @param vtxSeq
     * @param vtxId
     * @return
     */
    private boolean curVtxEqualsTheMinVtxOfAFoundCycle(LinkedHashSet<Long> vtxSeq, Long vtxId) {
        Long minVtxId = vtxSeq
            .stream()
            .min(Long::compareTo)
            .orElse(null);

        return Objects.equals(minVtxId, vtxId);
    }

}
