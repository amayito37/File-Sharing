public class Ready_ServerClient_Message extends Message {

  private String host_ip;
  private int port;

  public Ready_ServerClient_Message(String source, String dest, String host_ip, int port) {
    super(Type.READY_SERVERCLIENT, source, dest);
    this.host_ip = host_ip;
    this.port = port;
  }

  public String getHost_ip() {
    return host_ip;
  }

  public int getPort() {
    return port;
  }
}
