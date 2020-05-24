public abstract class Message {

  private Type type;
  private String source;
  private String dest;

  public Message(Type type, String source, String dest) {
    this.type = type;
    this.source = source;
    this.dest = dest;
  }

  public Type getType() {
    return this.type;
  }

  public String getSource(){
    return this.source;
  }

  public String getDest(){
    return this.dest;
  }
}
