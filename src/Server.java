import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO read information from file users.txt
public class Server {

  private Map<String, ObjectOutputStream> users; // we only need to write to them remotely
  private List<User> information;
  private static final String ip = "192.168.1.56";
  private static final String userFile = "users.txt";
  private int port;
  private ServerSocket serverSocket;

  private Server(int port) {
    this.port = port;
    this.users = new HashMap<String, ObjectOutputStream>();
    this.information = new ArrayList<>();
  }

  public Map<String, ObjectOutputStream> getUsers() {
    return users;
  }

  public synchronized void addUser(String user, ObjectOutputStream os) {
    this.users.put(user, os);
  }

  public synchronized void removeUser(String user) {
    users.remove(user);
  }

  public List<User> getInformation() {
    List<User> shown = new ArrayList<>(information);
    synchronized(this) {shown.removeIf(x -> !users.keySet().contains(x.getId()));}
    return shown;
  }

  public String getFileOwner(String file) {
    for(User u : information) {
      synchronized(this) {if(u.getFiles().contains(file) && users.keySet().contains(u.getId())) return u.getId();}
    }
    return null;
  }

  public synchronized ObjectOutputStream getOutputStream(String user) {
    return users.get(user);
  }

  public void setInformation(List<User> information) {
    this.information = information;
  }

  public void loadInformation() {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(userFile));
      String userName = reader.readLine();
      while(!userName.equals("Server")) {
        int numFiles = Integer.parseInt(reader.readLine());
        List<String> userFiles = new ArrayList<>();
        for (int i = 0; i < numFiles; i++) {
          userFiles.add(reader.readLine());
        }
        information.add(new User(userName, ip, userFiles));
        userName = reader.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String args[]) {

    if (args.length < 1) return;

    int port1 = Integer.parseInt(args[0]);
    FreePortGenerator freePortGenerator = FreePortGenerator.getFreePortGenerator(port1);

    Server server = new Server(freePortGenerator.getFreePort());

    try {
      server.run();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


  private void run() throws IOException {
    loadInformation();
    serverSocket = new ServerSocket(port);
    Socket socket = null;

    while(true) {


      try {

        socket = serverSocket.accept();

        System.out.println("A new client is connected : " + socket);

        System.out.println("Assigning new thread for this client");
        ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
        writer.flush();
        ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
        Thread t = new ClientHandler(socket, this, reader, writer);

        t.start();

      } catch (IOException e) {
        socket.close();
        e.printStackTrace();
      }

    }

  }

}
