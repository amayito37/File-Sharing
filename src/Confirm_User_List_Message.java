import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Confirm_User_List_Message extends Message {


  private Map<String, List<String>> information;

  public Confirm_User_List_Message(String source, String dest, Map<String, List<String>> information) {
    super(Type.CONFIRM_USER_LIST, source, dest);
    this.information = new HashMap<>(information);
  }

  public Map<String, List<String>> getInformation() {
    return this.information;
  }
}
