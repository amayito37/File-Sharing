import java.io.*;
import java.net.Socket;

public class ServerHandler extends Thread{

  private Socket socket;
  private InputStream is;
  private OutputStream os;
  private ObjectInputStream reader;
  private ObjectOutputStream writer;
  private Client client;
  private int port;

  public ServerHandler(Socket socket, Client client) {
    this.socket = socket;
    this.client = client;
    this.port = client.getPort() + 1;
    try {
      this.is = socket.getInputStream();
      this.os = socket.getOutputStream();
      this.reader = new ObjectInputStream(is);
      this.writer = new ObjectOutputStream(os);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void run() {
    exec: while(true) {
      try {
        Message m = (Message) reader.readObject();
        switch (m.getType()) {
          case CONFIRM_CONNECT:
            System.out.println("Connection with server established successfully");
            break;

          case CONFIRM_USER_LIST:
            System.out.println("The user list is: ");
            System.out.println(((Confirm_User_List_Message)m).getInformation());
            break;

          case ISSUE:
            Issuing issuing = new Issuing(client, port, ((Issue_Message)m).getUser(),
                ((Issue_Message)m).getFile());
            writer.writeObject(new Ready_ClientServer_Message(client.getUserId(), "server",
                ((Issue_Message)m).getUser(), Client.HOSTNAME, port));
            break;

          case READY_SERVERCLIENT:
            Receiver receiver = new Receiver(port);
            break;

          case CONFIRM_DISCONNECT:
            System.out.println("You are now disconnected from server");
            break exec;
        }
      } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

}
