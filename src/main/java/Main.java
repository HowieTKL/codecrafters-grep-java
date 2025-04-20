import org.howietkl.grep.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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

  static boolean matchPattern(String inputLine, String pattern) {
    List<String> exprs = Pattern.parsePattern(pattern);
    int index = 0;
    for (String expr : exprs) {
      LOG.debug("expr={}", expr);
      if ("\\d".equals(expr)) {
        index = checkDigit(inputLine, index);
        if (index++ == -1) {
          return false;
        }
      } else if ("\\w".equals(expr)) {
        index = checkLetterDigit(inputLine, index);
        if (index++ == -1) {
          return false;
        }
      } else if (expr.startsWith("[") && expr.endsWith("]")) {
        return inputLine.matches(".*" + pattern + ".*");
      } else {
        index = checkSimpleMatch(inputLine, expr, index);
        if (index++ == -1) {
          return false;
        }
        // throw new RuntimeException("Unhandled pattern: " + pattern);
      }
    }
    return true;
  }

  static int checkDigit(String inputLine, int index) {
    for (int i = index; i < inputLine.length(); ++i) {
      if (Character.isDigit(inputLine.charAt(i))) {
        return i;
      }
    }
    return -1;
  }

  static int checkLetterDigit(String inputLine, int index) {
    for (int i = index; i < inputLine.length(); ++i) {
      if (Character.isLetterOrDigit(inputLine.charAt(i))) {
        return i;
      }
    }
    return -1;
  }

  static int checkSimpleMatch(String inputLine, String expr, int index) {
    boolean isMatching = false;
    int exprIndex = 0;
    for (int i = index; i < inputLine.length() && exprIndex < expr.length() ; ++i) {
      if (inputLine.charAt(i) == expr.charAt(exprIndex)) {
        isMatching = true;
        ++exprIndex;
      } else {
        isMatching = false;
        exprIndex = 0;
      }
    }
    return exprIndex == expr.length() ? index : -1;
  }

}
