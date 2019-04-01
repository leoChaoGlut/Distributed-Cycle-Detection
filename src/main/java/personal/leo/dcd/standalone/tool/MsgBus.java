package personal.leo.dcd.standalone.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import personal.leo.dcd.standalone.entity.Msg;
import personal.leo.dcd.standalone.entity.MsgId;

/**
 * @author leo
 * @date 2019-03-29
 */
@Accessors(chain = true)
@Setter
public class MsgBus {
    /**
     * TODO 单机版,仅清理value,不清理消息的 key
     */
    Map<MsgId, List<Msg>> msgBus = new HashMap<>();

    public boolean contains(MsgId msgId) {
        return msgBus.containsKey(msgId);
    }

    public void append(MsgId msgId, Msg msg) {
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

    public List<Msg> get(MsgId msgId) {
        return msgBus.get(msgId);
    }
}
