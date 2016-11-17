package vector;

import org.apache.commons.lang3.StringUtils;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by GOSSAN on 17/11/2016.
 */
public class EMFHelper {

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
        props.put("mail.host", "smtp.dummydomain.com");
        props.put("mail.transport.protocol", "smtp");
        Session mailSession = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(mailSession, is);
        return (String) message.getContent();
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
