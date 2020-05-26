public class Connect_Message extends Message{

  private String user_id;

  public Connect_Message(String source, String dest, String user_id) {
    super(Type.CONNECT, source, dest);
    this.user_id = user_id;
  }

  public String getUser_id() {
    return this.user_id;
  }

}
