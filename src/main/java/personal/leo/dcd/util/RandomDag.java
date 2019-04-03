package personal.leo.dcd.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.RandomUtils;
import personal.leo.dcd.entity.Vertex;

/**
 * @author leo
 * @date 2019-03-29
 *
 * src: srouce
 * dest: destination
 *
 * tips: https://stackoverflow.com/questions/12790337/generating-a-random-dag
 */
@Accessors(chain = true)
@Setter
public class RandomDag {

    private int maxWidth;
    private int maxDepth;
    private int deptPow;

    private int maxNodeCount;

    private int edgeCount;
    private ChoiceSrcVtx choiceSrcVtx;
    private ChoiceDestVtx choiceDestVtx;

    /**
     * 仅存储 vtx 的 id,x,y 等信息,不包含 neighbors
     */
    private Map<Long, Vertex> vtxHolder = new HashMap<>();
    private Vertex rootVtx = new Vertex(-1).setX(-1).setY(-1);

    /**
     * 简单测试用
     *
     * @param maxNodeCount
     * @param edgeCount
     * @return
     */
    public static RandomDag line(int maxNodeCount, int edgeCount) {
        return new RandomDag()
            .setMaxNodeCount(maxNodeCount)
            .setEdgeCount(edgeCount)
            .setChoiceSrcVtx(() -> {
                int index = RandomUtils.nextInt(0, maxNodeCount);

                if (index == maxNodeCount - 1) {
                    index = RandomUtils.nextInt(0, maxNodeCount - 1);
                }

                return new Vertex(index);
            })
            .setChoiceDestVtx(srcVtx -> {
                int index = RandomUtils.nextInt((int)srcVtx.getId(), maxNodeCount);

                if (index == srcVtx.getId()) {
                    index = RandomUtils.nextInt((int)srcVtx.getId() + 1, maxNodeCount);
                }

                return new Vertex(index);
            });
    }

    /**
     * 如果需要绘图到 http://www.dagitty.net/dags.html#
     * 则需要使用该方法
     *
     * @param maxWidth
     * @param maxDepth
     * @param edgeCount
     * @return
     */
    public static RandomDag matrix(int maxWidth, int maxDepth, int edgeCount) {

        /*
            求一个数字有多少位
            5: 1
            24: 2
            555: 3
            1235: 4
         */
        int widthNumCount = String.valueOf(maxWidth).length();

        /*
          当使用 matrix 构造 dag 时,vertex 的 id 等于 depth * depthPow + width
          id = depth * depthPow + width
         */
        int deptPow = (int)Math.pow(10, widthNumCount);

        return new RandomDag()
            .setMaxWidth(maxWidth)
            .setMaxDepth(maxDepth)
            .setEdgeCount(edgeCount)
            .setDeptPow(deptPow)
            .setChoiceSrcVtx(() -> {
                int x = RandomUtils.nextInt(0, maxWidth);
                int y = RandomUtils.nextInt(0, maxDepth);

                if (x == maxWidth - 1 && y == maxDepth - 1) {
                    x = RandomUtils.nextInt(0, maxWidth - 1);
                    y = RandomUtils.nextInt(0, maxDepth - 1);
                }

                return new Vertex(y * deptPow + x)
                    .setX(x)
                    .setY(y);
            })
            .setChoiceDestVtx(srcVtx -> {
                int x = RandomUtils.nextInt(srcVtx.getX(), maxWidth);
                int y = RandomUtils.nextInt(srcVtx.getY(), maxDepth);

                if (x == srcVtx.getX() && y == srcVtx.getY()) {
                    x = RandomUtils.nextInt(srcVtx.getX() + 1, maxWidth);
                    y = RandomUtils.nextInt(srcVtx.getY() + 1, maxDepth);
                }

                return new Vertex(y * deptPow + x)
                    .setX(x)
                    .setY(y);
            });
    }

    /**
     * @return vtxs with neighbors
     */
    public List<Vertex> draw() {

        Map<Long, Vertex> vtxMap = new HashMap<>();

        for (int i = 0; i < edgeCount; i++) {
            Vertex srcVtx = choiceSrcVtx.choice();
            Vertex destVtx = choiceDestVtx.choice(srcVtx);

            vtxHolder.putIfAbsent(srcVtx.getId(), srcVtx.copy());
            vtxHolder.putIfAbsent(destVtx.getId(), destVtx.copy());

            long srcVtxId = srcVtx.getId();

            if (vtxMap.containsKey(srcVtxId)) {
                vtxMap.get(srcVtxId).out(destVtx);
            } else {
                srcVtx.out(destVtx);
                vtxMap.put(srcVtxId, srcVtx);
            }

        }

        vtxHolder.values().forEach(vtx -> rootVtx.out(vtx));

        vtxMap.put(rootVtx.getId(), rootVtx);
        vtxHolder.put(rootVtx.getId(), rootVtx);

        return new ArrayList<>(vtxMap.values());

    }

    public List<Vertex> getAllVtxsWithoutNeighbors() {
        return new ArrayList<>(vtxHolder.values());
    }

    /**
     * 随机选择下标,只要不是最大的下标即可. 因为从 src 找 dest 时,src 必须是小于 dest 的.
     */
    public interface ChoiceSrcVtx {
        Vertex choice();
    }

    public interface ChoiceDestVtx {
        Vertex choice(Vertex srcVtx);
    }
}
