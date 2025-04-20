import org.howietkl.grep.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
    LOG.debug("exprs={}", exprs);
    for (String expr : exprs) {
      if ("\\d".equals(expr)) {
        index = checkDigit(inputLine, index);
        if (index == -1) {
          LOG.debug("checkDigit failed index={}", index);
          return false;
        }
      } else if ("\\w".equals(expr)) {
        index = checkLetterDigit(inputLine, index);
        if (index == -1) {
          LOG.debug("checkLetterDigit failed index={}", index);
          return false;
        }
      } else if (expr.startsWith("[") && expr.endsWith("]")) {
        index = checkSquareBracketMatch(inputLine, expr, index);
        if (index == -1) {
          LOG.debug("checkSquareBracketMatch failed index={}", index);
          return false;
        }
      } else {
        index = checkSimpleMatch(inputLine, expr, index);
        if (index == -1) {
          LOG.debug("checkSimpleMatch failed index={}", index);
          return false;
        }
      }
    }
    return true;
  }

  static int checkDigit(String inputLine, int index) {
    for (int i = index; i < inputLine.length(); ++i) {
      if (Character.isDigit(inputLine.charAt(i))) {
        return ++i;
      }
    }
    return -1;
  }

  static int checkLetterDigit(String inputLine, int index) {
    for (int i = index; i < inputLine.length(); ++i) {
      if (Character.isLetterOrDigit(inputLine.charAt(i))) {
        return ++i;
      }
    }
    return -1;
  }

  static int checkSimpleMatch(String inputLine, String expr, int index) {
    boolean isMatching = false;
    int exprIndex = 0;
    for (int i = index; i < inputLine.length() && exprIndex < expr.length(); ++i) {
      if (inputLine.charAt(i) == expr.charAt(exprIndex)) {
        isMatching = true;
        ++exprIndex;
      } else {
        isMatching = false;
        exprIndex = 0;
      }
    }
    return exprIndex == expr.length() ? ++index : -1;
  }

  static int checkSquareBracketMatch(String inputLine, String expr, int index) {
    Set<Character> lookup = new HashSet<>();
    int startExprIndex = 1;
    if (expr.charAt(1) == '^') {
      startExprIndex = 2;
    }
    expr.substring(startExprIndex, expr.length() - 1).chars().forEach(c -> lookup.add((char) c));

    for (int i = index; i < inputLine.length(); ++i) {
      if (startExprIndex == 1 && lookup.contains(inputLine.charAt(i))) {
        return ++i;
      } else if (startExprIndex == 2 && !lookup.contains(inputLine.charAt(i))) {
        return ++i;
      }
    }
    return -1;
  }

}
