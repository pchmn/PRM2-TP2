package vector;

import org.apache.commons.lang3.StringUtils;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Created by GOSSAN on 17/11/2016.
 */
public class EMLHelper {

    private static final String[] dictionnary = DictionnaryHelper.execute("dictionnary.csv", ";");

    private static Function<String[], Map> initialVector = new Function<String[], Map>() {
        @Override
        public Map apply(String[] s) {
            Map<String, Integer> map = new HashMap<>();
            for(String word : s)
                map.put(word, 1);
            return map;
        }
    };

    private static final Map<String, Integer> V0 = initialVector.apply(dictionnary);

    private static List<String> OK = new ArrayList<>();
    private static List<String> KO = new ArrayList<>();

    public static void display(){
        System.out.println("----------------- Mail OK ---------------- "+OK.size()+" fichiers");
        for(String value : OK)
            System.out.println("-> "+value);
        System.out.println("--------------------- Mail KO ------------ "+KO.size()+" fichiers");
        for(String value : KO)
            System.out.println("-> "+value);
    }

    public static void run(String directoryPath) {

        List<String> emfFilesPath = getEmfFiles(directoryPath);
        Map<String, Integer> V1;

        for (String path : emfFilesPath){
            try {

                String emlContent = getEmfFileContent(path);
                V1 = getVector(emlContent, dictionnary);
                double cos = Vector.cos(V0, V1);

                File file = new File(path);
                if(cos <= 0.8)
                    KO.add(file.getName());
                else
                    OK.add(file.getName());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        display();
    }

    public static List<String> getEmfFiles(String directoryPath){
        List<String> emfFilesPath = new ArrayList<>();
        File directory = new File(directoryPath);

        File[] fList = directory.listFiles();

        for (File file : fList){
            if (file.isFile())
                emfFilesPath.add(file.getAbsolutePath());
            else if (file.isDirectory())
                getEmfFiles(file.getAbsolutePath());
        }
        return emfFilesPath;
    }

    public static String getEmfFileContent(String EmfPathValue) throws IOException, MessagingException {
        InputStream is = Files.newInputStream(Paths.get(EmfPathValue));
        Properties props = System.getProperties();
        Session mailSession = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(mailSession, is);
        String content = "", result = "";

        try{
            MimeMultipart mp = (MimeMultipart) message.getContent();
            int count = mp.getCount();
            for(int i = 0; i<count; i++){
                BodyPart bp = mp.getBodyPart(i);

                    result = new BufferedReader(
                            (new InputStreamReader(bp.getInputStream())))
                            .lines()
                            .collect(Collectors.joining("\n"));

                content += result;
            }

        }catch(ClassCastException ex){
            content = (String)message.getContent();
        }
        return  content;
    }

    public static Map<String, Integer> getVector(String EmfFileContent, String[] dictionary) throws IOException, MessagingException {
        Map<String, Integer> vector = new HashMap<>();
        for(String keyWord : dictionary){
            int occurrence = StringUtils.countMatches(EmfFileContent, keyWord);
            vector.put(keyWord, occurrence);
        }
        return vector;
    }


}
