import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Server class for interacting with the client
public class Server {
    private ServerSocket server = null;
    private ExecutorService service = null;

    // check if request is all numbers
    public void checkInput(String request) {
        // check each character of the request
        String trim = request.replace(" ", "");
        for (int i = 0; i < trim.length(); i++) {
            if (trim.charAt(i) < '0' || trim.charAt(i) > '9') {
                System.err.println("Please provide positive numbers!");
                System.exit(-1);
            }
        }
    }

    // run the server
    public void run(String request) throws IOException {
        // open the listening port
        try {
            server = new ServerSocket(9001);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to listen the port!");
            System.exit(-1);
        }
        // initialize the executor service
        service = Executors.newFixedThreadPool(25);

        // check the number of args
        String[] args = request.split(" ");

        // initialize the members
        int listNum = Integer.parseInt(args[0]);
        int memberNum = Integer.parseInt(args[1]);
        Members members = new Members(listNum, memberNum);

        // submit the new client handler to the thread pool
        while (true) {
            Socket client = server.accept();
            System.out.println(client.getPort());
            service.submit(new ClientHandler(client, members));
        }
    }

    // generate one string for the command line argument
    public static String handleInput(String[] args) {
        StringBuilder request = new StringBuilder();
        for (String arg : args) {
            request.append(arg);
            request.append(" ");
        }
        request = new StringBuilder(request.toString().trim());
        return request.toString();
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Please provide 2 arguments!");
            System.exit(-1);
        }
        Server server = new Server();
        String request = handleInput(args);
        server.checkInput(request);
        server.run(request);
    }
}