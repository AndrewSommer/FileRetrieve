import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class is the client for a networking transaction. This client specifies the file
 * wanted to transfer over to a path specified on the client machine.
 *
 * @author Andrew Sommer
 * @version 1.0.0 06 February 2018
 */
public class FileRetrieverClient {
    /**
     * The host name of the server for the socket
     */
    private final String hostName;
    /**
     * The number of the port used for the socket
     */
    private final int portNum;
    /**
     * A file name passed in through the constructor to indicate to the server which file is to be transferred
     */
    private final String fileNameWanted;
    /**
     * A file name passed in through the constructor to indicate what the resulting file should be named
     */
    private final String fileOutput;

    /**
     * Constructor for the Client
     *
     * @param hostName       the IP address for the server
     * @param portNum        the port number used for the server machine
     * @param fileNameWanted the name of the file wanted
     * @param fileOutput     the name that will be created and written to on the client machine
     */
    public FileRetrieverClient(String hostName, int portNum, String fileNameWanted, String fileOutput) {
        this.hostName = hostName;
        this.portNum = portNum;
        this.fileNameWanted = fileNameWanted;
        this.fileOutput = fileOutput;
    }

    /**
     * The client socket used for the file transfer
     */
    private Socket serverConnection;

    /**
     * Creates a socket to connect the specified port of the specified IP address, input
     * and output steams to communicate with the server, and a file stream to output the
     * file contents into from the server.
     */
    public final void start() {
        System.out.println("Client Is Waiting...");
        try {
            serverConnection = new Socket(hostName, portNum);//server and client a
            connectAndStream();
            getFileBytesFromServer();

        } catch (UnknownHostException uhe) {
            System.out.println("Unable To Resolve Server");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * The stream used to input from the server
     */
    private ObjectInputStream inputStream;
    /**
     * The stream used to output to the server
     */
    private ObjectOutputStream outputStream;

    /**
     * Called by the start() method to create input and output Object streams connected
     * to the host
     * @throws UnknownHostException
     * @throws IOException
     */
    private void connectAndStream() throws UnknownHostException, IOException {
        System.out.println("Connected");
        outputStream = new ObjectOutputStream(serverConnection.getOutputStream());
        System.out.println("Got OutputStream");
        outputStream.flush();
        System.out.println("Flushed");
        inputStream = new ObjectInputStream(serverConnection.getInputStream());
        System.out.println("Got I/O Streams");
    }

    /**
     * Called by the start() method to recieve bytes from a specified file to
     * the the fileOutputStream which puts the bytes into the file speciefied
     * by the class fileOutput variable.
     * @throws IOException
     */
    private void getFileBytesFromServer() throws IOException {
        System.out.println("Getting File Bytes");
        byte[] transferData = new byte[500];
        FileOutputStream fileOutputStream = new FileOutputStream(fileOutput);
        outputStream.writeObject(fileNameWanted);
        outputStream.flush();
        System.out.println("Sent FileName Wanted");
        try {
            if (((String) inputStream.readObject()).equals("FileFound")) {
                int sizeOfDataTransfering;
                while (inputStream.available() > 0) {
                    sizeOfDataTransfering = inputStream.read(transferData);
                    fileOutputStream.write(transferData, 0, sizeOfDataTransfering);
                }
                fileOutputStream.flush();
            } else {
                System.out.println("Server Did Not Find File");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        fileOutputStream.close();
    }

}
