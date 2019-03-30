package personal.leo.dcd.standalone.entity;

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
public class MessageAndVertex {
    private Vertex vertex;
    private Message msg;
}
