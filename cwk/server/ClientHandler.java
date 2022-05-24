import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

// ClientHandler class for handling one client with the server
public class ClientHandler extends Thread {
    private Socket client = null;
    private Members members = null;

    public ClientHandler(Socket client, Members members) {
        super("ClientHandler");
        this.client = client;
        this.members = members;
    }

    // run the thread
    public void run() {
        // get the input and output stream
        try {
            PrintWriter output = new PrintWriter(client.getOutputStream(), true);
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                            client.getInputStream()));

            String inputStr, outputStr;
            // read the client request
            inputStr = input.readLine();

            // get client IP address
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();

            Protocol protocol = new Protocol(members, ipAddress);
            outputStr = protocol.process(inputStr);

            output.print(outputStr);

            output.close();
            input.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
