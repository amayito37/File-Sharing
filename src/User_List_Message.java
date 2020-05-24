public class User_List_Message extends Message {

  public User_List_Message(String source, String dest) {
    super(Type.CONFIRM_CONNECT, source, dest);
  }

}
