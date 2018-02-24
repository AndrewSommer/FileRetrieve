/**
 * This class contains only the main method. It simulates the Server portion
 * of 28-13_FileRetrieve_Easy. Client must run after this is started in order
 * to transfer the file
 *
 * @author Andrew Sommer
 * @version 1.0.0 06 February 2018
 */
public class FileServerDriver {
    public static void main(String[] args) {
        FileServer fileServer = new FileServer(23532);
        fileServer.start();
    }
}
