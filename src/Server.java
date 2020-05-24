import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

  private Map<String, Socket> users;
  private Map<String, List<String>> information;
  private static final String ip = "192.168.1.56";
  private int port;
  private ServerSocket serverSocket;

  private Server(int port) {
    this.port = port;
    this.users = new HashMap<String, Socket>();
    this.information = new HashMap<String, List<String>>();
  }

  public Map<String, Socket> getUsers() {
    return users;
  }

  public void addUser(String user, Socket socket) {
    this.users.put(user,socket);
  }

  public void removeUser(String user) {
    users.remove(user);
    information.remove(user);
  }

  public Map<String, List<String>> getInformation() {
    return information;
  }

  public String getFileOwner(String file) {
    for(Map.Entry<String, List<String>> entry : information.entrySet()) {
      if(entry.getValue().contains(file)) return entry.getKey();
    }
    return null;
  }

  public Socket getSocket(String user) {
    return users.get(user);
  }

  public void setInformation(Map<String, List<String>> information) {
    this.information = information;
  }

  public static void main(String args[]) {

    if (args.length < 1) return;

    int port1 = Integer.parseInt(args[0]);

    Server server = new Server(port1);

    try {
      server.run();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


  private void run() throws IOException {

    serverSocket = new ServerSocket(port);
    Socket socket = null;

    while(true) {


      try {

        socket = serverSocket.accept();

        System.out.println("A new client is connected : " + socket);

        System.out.println("Assigning new thread for this client");

        Thread t = new ClientHandler(socket, this);

        t.start();

      } catch (IOException e) {
        socket.close();
        e.printStackTrace();
      }

    }

  }

}
