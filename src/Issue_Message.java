public class Issue_Message extends Message {

  private String file;
  private String user;

  public Issue_Message(String source, String dest, String file, String user) {
    super(Type.ISSUE, source, dest);
    this.file = file;
    this.user = user;
  }

  public String getFile() {
    return file;
  }

  public String getUser() {
    return user;
  }
}
