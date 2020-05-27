/**
 * For sending messages to the user
 */
public class UserPrinter {

  private StringAlignUtils left;
  private StringAlignUtils right;

  public UserPrinter() {
    this.left = new StringAlignUtils(100, StringAlignUtils.Alignment.LEFT);
    this.right = new StringAlignUtils(100, StringAlignUtils.Alignment.RIGHT);
  }

  /**
   * For normal straight messages to user
   */
  public void directPrint(String message) {
    System.out.print(left.format(message));
  }

  /**
   * For other not synchronized messages
   */
  public void otherInfoPrint(String message) {
    System.out.print(right.format(message));
  }


}
