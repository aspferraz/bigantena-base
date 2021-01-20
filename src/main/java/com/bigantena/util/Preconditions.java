/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.util;

/**
 * Verifies correct argument values and state (borrowed from Guava.)
 * 
 * @author aspferraz
 */
public final class Preconditions {
    
    private Preconditions() {
    }

  /**
   * Verifies that the given {@code String} is not {@code null} or empty.
   *
   * @param s the given {@code String}.
   * @return the validated {@code String}.
   * @throws NullPointerException     if the given {@code String} is {@code null}.
   * @throws IllegalArgumentException if the given {@code String} is empty.
   */
  public static String checkNotNullOrEmpty(String s) {
    String checked = checkNotNull(s);
    if (checked.isEmpty()) {
      throw new IllegalArgumentException();
    }
    return checked;
  }

  /**
   * Verifies that the given array is not {@code null} or empty.
   *
   * @param array the given array.
   * @return the validated array.
   * @throws NullPointerException     if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   */
  public static <T> T[] checkNotNullOrEmpty(T[] array) {
    T[] checked = checkNotNull(array);
    if (checked.length == 0) {
      throw new IllegalArgumentException();
    }
    return checked;
  }

  /**
   * Verifies that the given object reference is not {@code null}.
   *
   * @param <T>       the type of the given object reference.
   * @param reference the given object reference.
   * @return the non-{@code null} reference that was validated.
   * @throws NullPointerException if the given object reference is {@code null}.
   */
  public static <T> T checkNotNull(T reference) {
    if (reference == null) {
      throw new NullPointerException();
    }
    return reference;
  }
}
