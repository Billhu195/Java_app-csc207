package Exception;

/** Handles Invalid Input for all classes */
public class InvalidInputException extends Exception {
  /**
   * When we expected invalid input from users
   * @param message message to be display
   */
  public InvalidInputException(String message) {
    super(message);
  }
}