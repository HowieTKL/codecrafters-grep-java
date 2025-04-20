package org.howietkl.grep;

import java.util.ArrayList;
import java.util.List;

public class Pattern {

  public static List<String> parsePattern(String pattern) {
    List<String> result = new ArrayList<>();
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < pattern.length(); ++i) {
      char c = pattern.charAt(i);
      if (c == '\\') {
        if (!builder.isEmpty()) {
          result.add(builder.toString());
          builder.setLength(0);
        }
        ++i;
        c = pattern.charAt(i);
        if (c == 'd') {
          result.add("\\d");
        } else if (c == 'w') {
          result.add("\\w");
        } else {
          result.add("\\" + c);
        }
      } else if (c == '[') {
        if (!builder.isEmpty()) {
          result.add(builder.toString());
          builder.setLength(0);
        }
        builder.append(c);
        do {
          ++i;
          c = pattern.charAt(i);
          builder.append(c);
        } while (c != ']');
        if (!builder.isEmpty()) {
          result.add(builder.toString());
          builder.setLength(0);
        }
      } else {
        builder.append(c);
      }
    }
    if (!builder.isEmpty()) {
      result.add(builder.toString());
    }
    return result;
  }
}
