/**
 *
 *  @author Pluciński Tomasz S27477
 *
 */

package zad1;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Tools {
    public static Options createOptionsFromYaml(String fileName) {
        try (InputStreamReader io = new InputStreamReader(Files.newInputStream(Paths.get(fileName)))) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(io);
            String host = (String) data.get("host");
            int port = (int) data.get("port");
            boolean concurMode = (boolean) data.get("concurMode");
            boolean showSendRes = (boolean) data.get("showSendRes");
            Map<String, List<String>> clientsMap = (Map<String, List<String>>) data.get("clientsMap");

            return new Options(host, port, concurMode, showSendRes, clientsMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}