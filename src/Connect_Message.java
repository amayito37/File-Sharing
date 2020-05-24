public class Connect_Message extends Message{

  private String user_id;

  public Connect_Message(String source, String dest) {
    super(Type.CONNECT, source, dest);
  }

  public String getUser_id() {
    return this.user_id;
  }

}
