import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Issuing extends Thread {

  private Client client;
  private int port;
  private String user;
  private String file;

  public Issuing(Client client, int port, String user, String file) {
    this.client = client;
    this.port = port;
    this.user = user;
    this.file = file;
  }

  @Override
  public void run() {
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      while(true) {
        Socket socket = serverSocket.accept();
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        ObjectInputStream reader = new ObjectInputStream(is);
        ObjectOutputStream writer = new ObjectOutputStream(os);
        sendFile(writer);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendFile(ObjectOutputStream writer) {
    try {
      File_Message message = new File_Message(client.getUserId(), user, file,
          Files.readAllBytes(Paths.get(file)), true);
      writer.writeObject(message);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (OutOfMemoryError e) {
      try {
        FileInputStream fis = new FileInputStream(file);
        int count;
        byte[] buffer = new byte[8192];
        while ((count = fis.read(buffer)) > 0)
        {
          File_Message message = new File_Message(client.getUserId(), user, file,
              Arrays.copyOfRange(buffer, 0, count), count < 8192);
          writer.writeObject(message);
        }
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }
}
