package personal.leo.dcd.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.RandomUtils;
import personal.leo.dcd.entity.Msg;
import personal.leo.dcd.entity.MsgId;

/**
 * @author leo
 * @date 2019-03-29
 */
public class Id {
    private static LinkedList<Long> ids = new LinkedList<>();

    public static final int MAX_SIZE = 10000;

    public static final boolean USE_SEQUENCE = true;

    static {
        if (USE_SEQUENCE) {
            for (int i = 0; i < MAX_SIZE; i++) {
                ids.add((long)(MAX_SIZE - i));
            }
        } else {
            for (int i = 0; i < MAX_SIZE; i++) {
                ids.add(RandomUtils.nextLong());
            }
            ids = ids.stream().distinct().collect(Collectors.toCollection(LinkedList::new));
        }
    }

    public static synchronized Long next() {
        return ids.pollLast();
    }

}
