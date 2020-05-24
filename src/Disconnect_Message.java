public class Disconnect_Message extends Message {

  public Disconnect_Message(String source, String dest) {
    super(Type.DISCONNECT, source, dest);
  }
}
