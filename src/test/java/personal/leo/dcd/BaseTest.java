package personal.leo.dcd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

/**
 * @author 谦扬(qianyang).廖超(liaochao).leo
 * @date 2019-04-03
 */
public class BaseTest {

    protected String allVtxsWithoutNeighborsFilePath =
        "/Users/leo/gitRepo/github/Distributed-Cycle-Detection/src/test/resources/dag/allVtxsWithoutNeighbors.json";

    protected String relationsFilePath
        = "/Users/leo/gitRepo/github/Distributed-Cycle-Detection/src/test/resources/dag/relations.json";

    protected String dagittyFilePath
        = "/Users/leo/gitRepo/github/Distributed-Cycle-Detection/src/test/resources/dag/dagitty.txt";

    protected String read(String filePath) throws IOException {
        return IOUtils.toString(new FileInputStream(filePath), StandardCharsets.UTF_8);
    }

    protected void write(String filePath, String fileContent) throws IOException {
        IOUtils.write(fileContent, new FileOutputStream(filePath), StandardCharsets.UTF_8);
    }
}
