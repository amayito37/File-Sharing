import java.io.*;
import java.net.Socket;

public class Receiver extends Thread {

  private int port;
  private static final String FOLDER = "copies";

  public Receiver(int port) {
    this.port = port;
  }

  @Override
  public void run() {
    try {
      Socket socket = new Socket(Client.HOSTNAME, port);
      ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
      File_Message message = (File_Message) reader.readObject();
      File file = new File(FOLDER + "\\" + message.getName());
      OutputStream os = new FileOutputStream(file);
      os.write(message.getFile());
      while(!message.isLast()) {
        message = (File_Message) reader.readObject();
        os.write(message.getFile());
      }
      os.close();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
