public class Confirm_Disconnect_Message extends Message {

  public Confirm_Disconnect_Message(String source, String dest) {
    super(Type.CONFIRM_DISCONNECT, source, dest);
  }
}
