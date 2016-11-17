package vector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by GOSSAN on 03/12/2016.
 */
public class DictionnaryHelper {

    private static String content = "";

    public static String[] execute(String CSVFileName, String separator){
        try {
            Files.lines(Paths.get(CSVFileName)).forEach(s -> content+=s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.split(separator);
    }
}
