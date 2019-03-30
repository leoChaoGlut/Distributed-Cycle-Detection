package personal.leo.dcd.standalone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import personal.leo.dcd.standalone.entity.Message;
import personal.leo.dcd.standalone.entity.MessageAndVertex;
import personal.leo.dcd.standalone.entity.Vertex;

/**
 * @author leo
 * @date 2019-03-29
 * Dcd: Distributed Cycle Detection
 */
public class DcdTest {

    /**
     * 每一轮迭代都会讲 msgQueue 的所有元素遍历
     */
    List<MessageAndVertex> msgQueue = new ArrayList<>();
    /**
     * 遍历 msgQueue 的过程中会发送消息,消息会落入 buffer 中,最后 buffer 再放入 msgQueue 中
     */
    List<MessageAndVertex> msgQueueBuffer = new ArrayList<>();

    /**
     * key: vertex id
     * value vertex
     */
    Map<Long, Vertex> vertexMap = new HashMap<>();

    List<Long> ids = new ArrayList<>();

    @Before
    public void before() {
        for (int i = 0; i < 100; i++) {
            ids.add(RandomUtils.nextLong());
        }
        ids = ids
            .stream()
            .distinct()
            .collect(Collectors.toList());
    }

    @Test
    public void dcdTest() {
        Vertex v1 = new Vertex(ids.get(0));
        Vertex v2 = new Vertex(ids.get(1));
        Vertex v3 = new Vertex(ids.get(2));
        Vertex v4 = new Vertex(ids.get(3));
        Vertex v5 = new Vertex(ids.get(4));
        Vertex v6 = new Vertex(ids.get(5));

        v1.out(v2);
        v1.out(v3);
        v2.out(v4);
        v2.out(v5);
        v3.out(v5);
        v3.out(v6);
        v5.out(v1);
        v6.out(v5);

        List<Vertex> vertices = Arrays.asList(v1, v2, v3, v4, v5, v6);

        vertexMap = vertices
            .stream()
            .collect(Collectors.toMap(
                Vertex::getId,
                Function.identity()
            ));

        int round = 0;
        //第0轮, 每个节点要把自己发送给它的 outNeighbors,执行完后,msgQueueBuffer 会有数据.
        for (Vertex vertex : vertices) {
            MessageAndVertex mav = new MessageAndVertex().setVertex(vertex);
            detect(round, mav);
        }
        round++;

        //开始迭代
        while (true) {
            msgQueue.addAll(msgQueueBuffer);
            msgQueueBuffer.clear();
            if (msgQueue.isEmpty()) {
                break;
            }

            for (MessageAndVertex mav : msgQueue) {
                detect(round, mav);
            }

            msgQueue.clear();
            round++;
        }

    }

    public void detect(int round, MessageAndVertex mav) {
        Vertex receivedMsgVertex = mav.getVertex();
        Message receivedMsg = mav.getMsg();

        //如果是第0轮,只需要把当前 vertex 的 id 作为 msg 发送给它的所有 outNeighbors
        if (round == 0) {
            for (Long outNeighborId : receivedMsgVertex.getOutNeighborsId()) {
                Message sentMsg = new Message().appendToSequences(receivedMsgVertex);
                send(sentMsg, vertexMap.get(outNeighborId));
            }
        } else if (receivedMsg == null || CollectionUtils.isEmpty(receivedMsg.getVertexSequences())) {
            //如果节点没有收到消息,代表该节点需要被 halt
            receivedMsgVertex.setActive(false);
        } else {
            for (LinkedHashSet<Vertex> vtxSequence : receivedMsg.getVertexSequences()) {
                Vertex firstVtx = vtxSequence.iterator().next();

                if (Objects.equals(firstVtx, receivedMsgVertex) &&
                    Objects.equals(Message.min(vtxSequence), receivedMsgVertex)
                ) {
                    String vtxSeqStr = vtxSequence
                        .stream()
                        .map(vertex -> String.valueOf(vertex.getId()))
                        .collect(Collectors.joining("->"));
                    throw new RuntimeException(
                        "Circular found:" + vtxSeqStr + "->" + receivedMsgVertex.getId()
                    );
                } else if (!Objects.equals(firstVtx, receivedMsgVertex) &&
                    !vtxSequence.contains(receivedMsgVertex)
                ) {
                    for (Long outNeighborId : receivedMsgVertex.getOutNeighborsId()) {
                        Message sentMsg = receivedMsg.appendToSequences(receivedMsgVertex);
                        send(sentMsg, vertexMap.get(outNeighborId));
                    }
                }
            }
        }
    }

    /**
     * 发送 msg 到 指定的vertex
     *
     * @param msg
     * @param vertex 指定的 vertex
     */
    private void send(Message msg, Vertex vertex) {
        msgQueueBuffer.add(
            new MessageAndVertex()
                .setVertex(vertex)
                .setMsg(msg)
        );
    }
}
