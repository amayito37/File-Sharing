import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

  private String id;
  private String hostip;
  private List<String> files;

  public User(String id, String hostip, List<String> files) {
    this.id = id;
    this.hostip = hostip;
    this.files = files;
  }

  public String getId() {
    return id;
  }

  public String getHostip() {
    return hostip;
  }

  public List<String> getFiles() {
    return files;
  }

  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", hostip='" + hostip + '\'' +
        ", files=" + files +
        '}';
  }
}
