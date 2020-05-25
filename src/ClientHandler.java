import java.io.*;
import java.net.Socket;

//TODO: proteger los accesos a las cosas
public class ClientHandler extends Thread {

  private Socket socket;
  private InputStream is;
  private OutputStream os;
  private Server server;

  public ClientHandler(Socket socket, Server server) {
    this.socket = socket;
    try {
      this.is = socket.getInputStream();
      this.os = socket.getOutputStream();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.server = server;
  }

  @Override
  public void run() {

    try {
      ObjectInputStream reader = new ObjectInputStream(is);
      ObjectOutputStream writer = new ObjectOutputStream(os);

      exec: while (true) {
        Message m = (Message) reader.readObject();
        switch (m.getType()) {

          case CONNECT:
            server.addUser(((Connect_Message)m).getUser_id(), socket);
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
            OutputStream fout2 = server.getSocket(user).getOutputStream();
            ObjectOutputStream requester = new ObjectOutputStream(fout2);
            requester.writeObject(new Issue_Message("server", user,
                ((Request_Message)m).getFile(), m.getSource()));
            break;

          case READY_CLIENTSERVER:
            OutputStream fout1 =
                server.getSocket(((Ready_ClientServer_Message)m).getUser()).getOutputStream();
            ObjectOutputStream issuer = new ObjectOutputStream(fout1);
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
