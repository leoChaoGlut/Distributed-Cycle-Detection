package personal.leo.dcd.entity;

import java.util.LinkedHashSet;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author leo
 * @date 2019-03-29
 */
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = {"vtxId", "round"})
@AllArgsConstructor
public class MsgId {

    private long vtxId;
    private int round;

}
