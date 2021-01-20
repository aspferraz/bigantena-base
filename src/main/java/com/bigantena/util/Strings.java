/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.util;

import static com.bigantena.util.Preconditions.checkNotNull;
import java.text.Normalizer;

/**
 * Utility methods related to {@code String}s.
 * 
 * @author aspferraz
 */
public final class Strings {
    
    private Strings() {
    }
    
 /**
   * Removes accents (diacritic signs) from the given String s.
   *
   * @param s the given {@code String}.
   * @return Return the given {@code String} whithout accents, or {@code null} if the given {@code String} is {@code null}.
   */
    public static String unaccent(String s) {
        if (s == null)
            return s;
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[^\\p{ASCII}]", "");
        return s;
    }
    
    
  /**
   * Indicates whether the given {@code String} is {@code null} or empty.
   *
   * @param s the {@code String} to check.
   * @return {@code true} if the given {@code String} is {@code null} or empty, otherwise {@code false}.
   */
  public static boolean isNullOrEmpty(String s) {
    return s == null || s.length() == 0;
  }

  /**
   * Returns the given {@code String} surrounded by single quotes, or {@code null} if the given {@code String} is {@code
   * null}.
   *
   * @param s the given {@code String}.
   * @return the given {@code String} surrounded by single quotes, or {@code null} if the given {@code String} is {@code
   *         null}.
   */
  public static String quote(String s) {
    return s != null ? String.format("'%s'", s) : null;
  }

  /**
   * Returns the given object surrounded by single quotes, only if the object is a {@code String}.
   *
   * @param o the given object.
   * @return the given object surrounded by single quotes, only if the object is a {@code String}.
   * @see #quote(String)
   */
  public static Object quote(Object o) {
    return o instanceof String ? quote(o.toString()) : o;
  }

  /**
   * Concatenates the given objects into a single {@code String}. This method is more efficient than concatenating using
   * "+", since only one {@link StringBuilder} is created.
   *
   * @param objects the objects to concatenate.
   * @return a {@code String} containing the given objects, or empty {@code String} if the given array is empty.
   */
  public static String concat(Object... objects) {
    checkNotNull(objects);
    StringBuilder b = new StringBuilder();
    for (Object o : objects) {
      b.append(o);
    }
    return b.toString();
  }

  /**
   * Joins the given {@code String}s using a given delimiter. The following example illustrates proper usage of this
   * method:
   * <p/>
   * <pre>
   * Strings.join(&quot;a&quot;, &quot;b&quot;, &quot;c&quot;).with(&quot;|&quot;)
   * </pre>
   * <p/>
   * which will result in the {@code String "a|b|c"}.
   *
   * @param strings the {@code String}s to join.
   * @return an intermediate object that takes a given delimiter and knows how to join the given {@code String}s.
   * @see StringsToJoin#with(String)
   */
  public static StringsToJoin join(String... strings) {
    return new StringsToJoin(strings);
  }

  /**
   * Appends a given {@code String} to the given target, only if the target does not end with the given {@code String}
   * to append. The following example illustrates proper usage of this method:
   * <pre>
   * Strings.append(&quot;c&quot;).to(&quot;ab&quot;);
   * Strings.append(&quot;c&quot;).to(&quot;abc&quot;);
   * </pre>
   * resulting in the {@code String "abc"} for both cases.
   *
   * @param toAppend the {@code String} to append.
   * @return an intermediate object that takes the target {@code String} and knows to append the given {@code String}.
   * @see StringToAppend#to(String)
   */
  public static StringToAppend append(String toAppend) {
    return new StringToAppend(toAppend);
  }

  /**
   * Knows how to join {@code String}s using a given delimiter.
   *
   * @see Strings#join(String[])
   */
  public static class StringsToJoin {
    /**
     * The {@code String}s to join.
     */
    private final String[] strings;

    /**
     * Creates a new {@link StringsToJoin}.
     *
     * @param strings the {@code String}s to join.
     */
    StringsToJoin(String... strings) {
      this.strings = strings;
    }

    /**
     * Specifies the delimeter to use to join {@code String}s.
     *
     * @param delimeter the delimeter to use.
     * @return the {@code String}s joined using the given delimeter.
     * @throws NullPointerException if the given delimeter is {@code null}.
     */
    public String with(String delimeter) {
      checkNotNull(delimeter);
      if (strings == null || strings.length == 0) {
        return "";
      }
      StringBuilder b = new StringBuilder();
      int stringCount = strings.length;
      for (int i = 0; i < stringCount; i++) {
        String s = strings[i];
        b.append(s != null ? s : "");
        if (i < stringCount - 1) {
          b.append(delimeter);
        }
      }
      return b.toString();
    }
  }

  /**
   * Knows how to append a given {@code String} to the given target, only if the target does not end with the given
   * {@code String} to append.
   */
  public static class StringToAppend {
    private final String toAppend;

    StringToAppend(String toAppend) {
      this.toAppend = toAppend;
    }

    /**
     * Appends the {@code String} specified in the constructor to the {@code String} passed as argument.
     *
     * @param s the target {@code String}.
     * @return a {@code String} containing the target {@code String} with the given {@code String} to append added to
     *         the end.
     */
    public String to(String s) {
      if (!s.endsWith(toAppend)) {
        return concat(s, toAppend);
      }
      return s;
    }
  }
}
