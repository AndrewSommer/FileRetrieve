/**
 * This class contains only the main method. It simulates the Client portion
 * of 28-13_FileRetrieve_Easy. ServerDriver must be running in order for
 * the client to connect and revieve the file
 *
 * @author Andrew Sommer
 * @version 1.0.0 06 February 2018
 */
public class ClientDriver {
    public static void main(String args[]) {
        String currDirectory = System.getProperty("user.dir");
        String wantedInput = currDirectory + "/oral_exam2/28-13_FileRetrieve_Easy/FileInput.txt";
        String wantedOutput = currDirectory + "/oral_exam2/28-13_FileRetrieve_Easy/FileOutput.txt";
        FileRetrieverClient fileRetrieverClient = new FileRetrieverClient("127.0.0.1", 23532, wantedInput, wantedOutput);
        fileRetrieverClient.start();
    }
}
