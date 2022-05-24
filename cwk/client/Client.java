import java.io.*;
import java.net.*;

// Client class for interacting with the server
public class Client {
    private Socket socket = null;
    private PrintWriter output = null;
    private BufferedReader input = null;

    // run the client
    public void run(String request) {
        try {
            // create the socket
            socket = new Socket("localhost", 9001);
            System.out.println(socket.getPort());
            // chain the writing and reading stream
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.err.println("Unknown host!\n");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to get I/0 connect to the host");
            System.exit(1);
        }

        // write to the server
        output.println(request);
        // read from the server
        String serverInput;
        try {
            while ((serverInput = input.readLine()) != null) {
                System.out.println(serverInput);
            }
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("I/O Exception!\n");
            System.exit(1);
        }
    }

    // handle the command line arguments
    public static String handleInput(String[] args) {
        StringBuilder request = new StringBuilder();
        for (String arg : args) {
            request.append(arg);
            request.append(" ");
        }
        request = new StringBuilder(request.toString().trim());
        request.insert(0, args.length + " ");
        return request.toString();
    }

    public static void main(String[] args) {
        String request = handleInput(args);
        Client client = new Client();
        client.run(request);
    }
}