public class Request_Message extends Message {

  private String file;

  public Request_Message(String source, String dest, String file) {
    super(Type.REQUEST, source, dest);
    this.file = file;
  }

  public String getFile() {
    return this.file;
  }
}
