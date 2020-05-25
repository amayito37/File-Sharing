public class File_Message extends Message {

  private String name;
  private byte[] file;
  private boolean last;

  public File_Message(String source, String dest, String name, byte[] file, boolean last) {
    super(Type.FILE, source, dest);
    this.name = name;
    this.file = file;
    this.last = last;
  }

  public String getName() {
    return name;
  }

  public byte[] getFile() {
    return file;
  }

  public boolean isLast() {
    return last;
  }
}
