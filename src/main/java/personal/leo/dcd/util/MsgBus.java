package personal.leo.dcd.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import lombok.experimental.Accessors;
import personal.leo.dcd.entity.Msg;
import personal.leo.dcd.entity.MsgId;

/**
 * @author leo
 * @date 2019-03-29
 */
public class MsgBus {
    /**
     * TODO 单机版,仅清理value,不清理消息的 key
     */
    private static Map<MsgId, List<Msg>> msgBus = new HashMap<>();

    public static synchronized boolean contains(MsgId msgId) {
        return msgBus.containsKey(msgId);
    }

    public static synchronized void append(MsgId msgId, Msg msg) {
        if (msgBus.containsKey(msgId)) {
            msgBus.get(msgId).add(msg);
        } else {
            msgBus.put(
                msgId,
                new ArrayList<Msg>() {{
                    add(msg);
                }}
            );
        }
    }

    public static synchronized List<Msg> get(MsgId msgId) {
        return msgBus.get(msgId);
    }
}
