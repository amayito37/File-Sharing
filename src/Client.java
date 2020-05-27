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
  private UserPrinter userPrinter; // for printing messages to user

  private Client(int port) {
    this.port = port;
    this.scanner = new Scanner(System.in);
    this.userPrinter = new UserPrinter();
  }

  public String getUserId() {
    return userId;
  }

  public int getPort() {
    return port;
  }

  public UserPrinter getUserPrinter() {
    return userPrinter;
  }

  public static void main(String args[]) {
    if(args.length < 1) return;
    Client client = new Client(Integer.parseInt(args[0]));
    client.run();
  }

  private void run() {
    userPrinter.directPrint("Input user Id: ");
    userId = scanner.next();
    userPrinter.directPrint("Hello " + userId);
    try {
      socket = new Socket(HOSTNAME, port);
      os = socket.getOutputStream();
      writer = new ObjectOutputStream(os);
      writer.flush();
      is = socket.getInputStream();
      reader = new ObjectInputStream(is);
      Thread t = new ServerHandler(socket, this, reader, writer);
      t.start();
      writer.writeObject(new Connect_Message(userId, "server", userId));
      menu();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
    Generates the menu for the user and handles his requests
   */
  private void menu() throws IOException {
    int option;
    do {
      userPrinter.directPrint("Select an option:");
      userPrinter.directPrint("1.- Check user list.");
      userPrinter.directPrint("2.- Request a file.");
      userPrinter.directPrint("0.- Disconnect.");
      option = scanner.nextInt();
      switch (option) {
        case 1:
          writer.writeObject(new User_List_Message(userId, "server"));
          break;

        case 2:
          userPrinter.directPrint("What's the name of the file you want? ");
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
