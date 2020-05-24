public class Ready_ClientServer_Message extends Message {

  private String user;
  private String host_ip;
  private int port;

  public Ready_ClientServer_Message(String source, String dest, String user, String host_ip,
                                    int port) {
    super(Type.READY_CLIENTSERVER, source, dest);
    this.user = user;
    this.host_ip = host_ip;
    this.port = port;
  }

  public String getUser() {
    return user;
  }

  public String getHost_ip() {
    return host_ip;
  }

  public int getPort() {
    return port;
  }
}
