import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerHandler extends Thread{

  private Socket socket;
  private ObjectInputStream reader;
  private ObjectOutputStream writer;
  private Client client;
  private UserPrinter userPrinter;

  public ServerHandler(Socket socket, Client client, ObjectInputStream reader,
                       ObjectOutputStream writer) {
    this.socket = socket;
    this.client = client;
    this.writer = writer;
    this.reader = reader;
    this.userPrinter = client.getUserPrinter();
  }

  @Override
  public void run() {
    exec: while(true) {
      try {
        Message m = (Message) reader.readObject();
        switch (m.getType()) {
          case CONFIRM_CONNECT:
            userPrinter.otherInfoPrint("Connection with server established successfully");
            break;

          case CONFIRM_USER_LIST:
            userPrinter.otherInfoPrint("The user list is: ");
            printUserList(((Confirm_User_List_Message)m).getInformation());
            break;

          case ISSUE:
            int port = FreePortGenerator.getFreePortGenerator().getFreePort();
            Issuing issuing = new Issuing(client, port,
                ((Issue_Message)m).getUser(),
                ((Issue_Message)m).getFile());
            issuing.start();
            writer.writeObject(new Ready_ClientServer_Message(client.getUserId(), "server",
                ((Issue_Message)m).getUser(), Client.HOSTNAME, (port)));
            break;

          case READY_SERVERCLIENT:
            Receiver receiver = new Receiver(((Ready_ServerClient_Message)m).getPort());
            receiver.start();
            break;

          case CONFIRM_DISCONNECT:
            userPrinter.otherInfoPrint("You are now disconnected from server");
            break exec;
        }
      } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  private void printUserList(List<User> userList) {
    userPrinter.otherInfoPrint("Size: " + userList.size());
    for (User user : userList) {
      userPrinter.otherInfoPrint(user.getId() + ":            ");
      for(String s : user.getFiles()) {
        userPrinter.otherInfoPrint(s);
      }
    }
  }

}
