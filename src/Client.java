import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

  public static final String HOSTNAME = "192.168.1.56";

  private String userId;
  private Socket socket;
  private int port;
  private InputStream is;
  private OutputStream os;
  private ObjectOutputStream writer;
  private ObjectInputStream reader;
  private Scanner scanner;

  private Client(int port) {
    this.port = port;
    this.scanner = new Scanner(System.in);
  }

  public String getUserId() {
    return userId;
  }

  public int getPort() {
    return port;
  }

  public static void main(String args[]) {
    if(args.length < 1) return;
    Client client = new Client(Integer.parseInt(args[0]));
    client.run();
  }

  private void run() {
    System.out.print("Input user Id: ");
    userId = scanner.next();
    try {
      socket = new Socket(HOSTNAME, port);
      Thread t = new ServerHandler(socket, this);
      t.start();
      is = socket.getInputStream();
      reader = new ObjectInputStream(is);
      os = socket.getOutputStream();
      writer = new ObjectOutputStream(os);
      writer.writeObject(new Connect_Message(userId, "server"));
      menu();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void menu() throws IOException {
    int option;
    do {
      System.out.println("Select an option:");
      System.out.println("1.- Check user list.");
      System.out.println("2.- Request a file.");
      System.out.println("0.- Disconnect.");
      option = scanner.nextInt();
      switch (option) {
        case 1:
          writer.writeObject(new User_List_Message(userId, "server"));
          break;

        case 2:
          System.out.print("What's the name of the file you want? ");
          String file = scanner.next();
          writer.writeObject(new Request_Message(userId, "server", file));
          break;

        default:
          break;
      }
    } while(option != 0);
    writer.writeObject(new Disconnect_Message(userId, "server"));
  }
}
