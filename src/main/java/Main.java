import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Main {
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    if (args.length != 2 || !args[0].equals("-E")) {
      System.out.println("Usage: ./your_program.sh -E <pattern>");
      System.exit(1);
    }

    String pattern = args[1];  
    Scanner scanner = new Scanner(System.in);
    String inputLine = scanner.nextLine();

    if (matchPattern(inputLine, pattern)) {
      LOG.debug("Match pattern - exit 0");
      System.exit(0);
    } else {
      LOG.debug("Did not match pattern - exit 1");
      System.exit(1);
    }
  }

  public static boolean matchPattern(String inputLine, String pattern) {
    if ("\\d".equals(pattern)) {
      return inputLine.matches(".*\\d.*");
    } else if (pattern.length() == 1) {
      return inputLine.contains(pattern);
    } else {
      throw new RuntimeException("Unhandled pattern: " + pattern);
    }
  }
}
