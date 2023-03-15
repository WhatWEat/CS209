package Lab5;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.io.BufferedReader;

public class FileTypeParser {

    public static void main(String[] args) throws URISyntaxException {
        Scanner sc = new Scanner(System.in);
        String filename = sc.next();
        URI uri = Objects.requireNonNull(
            FileTypeParser.class.getClassLoader().getResource(filename)).toURI();
        String filePath = Paths.get(uri).toString();

        try (FileInputStream fis = new FileInputStream(filePath)){

            byte[] buffer = new byte[65535];

            int byteNum = fis.read(buffer);

            String header = String.format("%02x%02x%02x%02x",buffer[0],buffer[1],buffer[2],buffer[3]);
            String fileClass = "";
            //class
            if(header.equals("cafebabe")){
                fileClass = "class";
            }//zip
            else if (header.equals("504b0304")){
                fileClass = "zip or jar";
            }//png
            else if (header.equals("89504e47")){
                fileClass = "png";
            }else{

            }
            printAns(filename,header,fileClass);
        } catch (FileNotFoundException e) {
            System.out.println("The pathname does not exist.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Failed or interrupted when doing the I/O operations");
            e.printStackTrace();
        }
    }
    static void printAns(String filename,String header,String fileclass){
        System.out.println(filename);
        System.out.println(header);
        System.out.println(fileclass);
    }
}
