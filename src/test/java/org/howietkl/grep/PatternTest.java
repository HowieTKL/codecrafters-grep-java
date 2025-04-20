package org.howietkl.grep;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatternTest {

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void parsePattern() {
    assertEquals(List.of("\\d"), Pattern.parsePattern("\\d"));
    assertEquals(List.of("\\w"), Pattern.parsePattern("\\w"));
    assertEquals(List.of("\\t"), Pattern.parsePattern("\\t"));
    assertEquals(List.of("\\d", "\\d"), Pattern.parsePattern("\\d\\d"));
    assertEquals(List.of("\\d", " ", "\\d", "\\w", "s"), Pattern.parsePattern("\\d \\d\\ws"));
    assertEquals(List.of("\\d", "\\d", "apple"), Pattern.parsePattern("\\d\\dapple"));
    assertEquals(List.of("\\d", "\\d", " apple ", "\\d", "\\w"), Pattern.parsePattern("\\d\\d apple \\d\\w"));
    assertEquals(List.of("[abc]"), Pattern.parsePattern("[abc]"));
    assertEquals(List.of("\\d", "[abc]", "\\d"), Pattern.parsePattern("\\d[abc]\\d"));
    assertEquals(List.of("defen", "[cs]", "e" ), Pattern.parsePattern("defen[cs]e"));
  }
}