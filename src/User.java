import java.util.List;

public class User {

  private String id;
  private String hostip;
  private List<String> files;

  public User(String id, String hostip, List<String> files) {
    this.id = id;
    this.hostip = hostip;
    this.files = files;
  }
}
