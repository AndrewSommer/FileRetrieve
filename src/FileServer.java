import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class is the server for a networking transaction. This Server recieves a filename
 * and outputs the bytes through a ServerSocket
 *
 * @author Andrew Sommer
 * @version 1.0.0 06 February 2018
 */
public class FileServer {

    private Socket clientConnection;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String filenameToSend;

    /**
     * The number of the port used for the socket
     */
    private final int portNum;

    /**
     * The constructor to make a File server
     * @param portNum the number of the port used for the socket
     */
    public FileServer(int portNum) {
        this.portNum = portNum;
    }

    /**
     * The Server Socket that waits for the client to connect to
     */
    private ServerSocket serverSocket;

    /**
     * Creates the Server Socket that the client needs to connect to, retrieves a
     * filename which is sent through the connection of the socket via the output
     * stream
     */
    public final void start() {
        System.out.println("Server Is Waiting...");
        try {
            serverSocket = new ServerSocket(portNum);//Establish Port Number
            while (true) {
                connectAndStream();
                getFileNameFromClient();
                sendFileToClient();
            }
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Unexpected Data From Client");
            cnfe.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (IOException io) {
                System.out.println("Error Closing Connection");
                io.printStackTrace();
            }
        }
    }

    /**
     * Listens for the client to connect using the IP address and port number then
     * creates object output and input streams
     * @throws IOException
     */
    private void connectAndStream() throws IOException {
        clientConnection = serverSocket.accept();//Listen For Client Attempt To Connect
        System.out.println("Connection Established");
        outputStream = new ObjectOutputStream(clientConnection.getOutputStream());
        System.out.println("Got OutputStream");
        outputStream.flush();
        inputStream = new ObjectInputStream(clientConnection.getInputStream());//Create in
        System.out.println("Got I/O Streams");
    }

    /**
     * Reads in the input stream for the filename which should be a string
     * otherwise ClassNotFoundException is thrown
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private void getFileNameFromClient() throws ClassNotFoundException, IOException {
        System.out.println("Getting Filename From Client...");
        filenameToSend = (String) inputStream.readObject();
        //filenameToSend = "/Users/AndrewSommer/git/sommr_swd/oral_exam2/28-13_FileRetrieve_Easy/FileInput.txt";
        System.out.println("Got Filename From Client");

    }

    /**
     * Uses the filename gotten from getFileNameFromClient() method, and
     * creates a file input stream which recieves bytes of data from the
     * file, then writes those bytes to the outputStream (inputStream)
     * of the client
     * @throws IOException
     */
    private void sendFileToClient() throws IOException {
        System.out.println("SendingFileTo");
        File fileToSend = new File(filenameToSend);
        try {
            FileInputStream fileInputStream = new FileInputStream(fileToSend);
            byte[] sendingData = new byte[500]; //500 ASCII Characters || 250 Unicode Characters
            int sizeOfDataSent;
            outputStream.writeObject("FileFound"); //Client Should Expect File Bytes Now
            while ((sizeOfDataSent = fileInputStream.read(sendingData)) > 0) {
                outputStream.write(sendingData, 0, sizeOfDataSent);//Send Data To Client
            }
            outputStream.flush();
            fileInputStream.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("FileNotFound");
            outputStream.writeObject("FileNotFound"); //Client Knows Not To Expect The File
        }
    }

    /**
     *  Closes the input stream, output stream, and the client connection
     * @throws IOException
     */
    private void closeConnection() throws IOException {
        inputStream.close();
        outputStream.close();
        clientConnection.close();
    }
}
