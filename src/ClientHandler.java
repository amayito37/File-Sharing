import java.io.*;
import java.net.Socket;


public class ClientHandler extends Thread {

  private Socket socket;
  private ObjectInputStream reader;
  private ObjectOutputStream writer;
  private Server server;

  public ClientHandler(Socket socket, Server server, ObjectInputStream reader,
                       ObjectOutputStream writer) {
    this.socket = socket;
    this.reader = reader;
    this.writer = writer;
    this.server = server;
  }

  @Override
  public void run() {

    try {
      exec: while (true) {
        Message m = (Message) reader.readObject();
        switch (m.getType()) {

          case CONNECT:
            server.addUser(((Connect_Message)m).getUser_id(), writer);
	          writer.writeObject(new Confirm_Connect_Message("server",
                ((Connect_Message)m).getUser_id()));
            break;

          case USER_LIST:
            Message message = new Confirm_User_List_Message("server", m.getSource(), server.getInformation());
            writer.writeObject(message);
            break;

          case DISCONNECT:
            server.removeUser(m.getSource());
            writer.writeObject(new Confirm_Disconnect_Message("server", m.getSource()));
            reader.close();
            writer.close();
            socket.close();
            break exec; // so that it breaks the while and not the switch

          case REQUEST:
            String user = server.getFileOwner(((Request_Message)m).getFile());
            ObjectOutputStream requester = server.getOutputStream(user);
            requester.writeObject(new Issue_Message("server", user,
                ((Request_Message)m).getFile(), m.getSource()));
            break;

          case READY_CLIENTSERVER:
            ObjectOutputStream issuer =
              server.getOutputStream(((Ready_ClientServer_Message)m).getUser());
            issuer.writeObject(new Ready_ServerClient_Message("server",
                ((Ready_ClientServer_Message)m).getUser(),
                ((Ready_ClientServer_Message)m).getHost_ip(),
                ((Ready_ClientServer_Message)m).getPort()));
            break;
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

}
