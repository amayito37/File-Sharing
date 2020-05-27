/**
 * Singleton object that generates free ports for whoever needs them.
 */
public class FreePortGenerator {

  private int port;
  private static FreePortGenerator freePortGenerator;

  private FreePortGenerator(int port) {
    this.port = port;
  }

  public static FreePortGenerator getFreePortGenerator(int port) {

    if (freePortGenerator==null) {

      freePortGenerator=new FreePortGenerator(port);
    }
    return freePortGenerator;
  }

  public static FreePortGenerator getFreePortGenerator() {

    return getFreePortGenerator(600);
  }

  public int getFreePort() {
    return port++;
  }
}
