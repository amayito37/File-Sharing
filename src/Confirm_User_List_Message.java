import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Confirm_User_List_Message extends Message {


  private List<User> information;

  public Confirm_User_List_Message(String source, String dest, List<User> information) {
    super(Type.CONFIRM_USER_LIST, source, dest);
    this.information = new ArrayList<>(information);
  }

  public List<User> getInformation() {
    return this.information;
  }
}
