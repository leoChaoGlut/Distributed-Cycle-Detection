# Distributed-Cycle-Detection
Implement of Distributed cycle detection in large-scale sparse graphs

大规模稀疏图分布式环检测论文实现

论文url:https://github.com/leoChaoGlut/Distributed-Cycle-Detection/blob/master/Distributed%20cycle%20detection%20in%20large-scale%20sparse%20graphs.pdf

# 使用方法
1. **RandomDag**:  personal.leo.dcd.util.RandomDagTest
2. **Standalone**:  personal.leo.dcd.impl.standalone.StandaloneTest
3. **PseudoDistributed**:  personal.leo.dcd.impl.distributed.pseudo.PseudoDistributedTest

# 说明
1. 测试生成的 dag 数据,可到 http://www.dagitty.net/dags.html 进行可视化绘图.
2. 所有的 Test 类 **以注解 @Test 开头的**,**命名有意义的public方法**,都是可用的测试方法.
