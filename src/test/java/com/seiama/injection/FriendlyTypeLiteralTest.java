/*
 * This file is part of injection, licensed under the MIT License.
 *
 * Copyright (c) 2021-2023 Seiama
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.seiama.injection;

import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FriendlyTypeLiteralTest {
  @Test
  void testWhere() {
    final TypeLiteral<Set<String>> setOfString = setOfElementType(String.class);
    assertEquals(assertDoesNotThrow(() -> TypeInfo.class.getDeclaredField("setOfString").getGenericType()), setOfString.getType());
  }

  @SuppressWarnings("checkstyle:MethodName")
  private static <T> TypeLiteral<Set<T>> setOfElementType(final Class<T> typeOfT) {
    return new FriendlyTypeLiteral<Set<T>>() {}
      .where(new TypeArgument<T>(typeOfT) {});
  }

  @Test
  void testIn() {
    abstract class Foo<T> {
      @SuppressWarnings("unchecked")
      private final Class<T> typeOfT = (Class<T>) new FriendlyTypeLiteral<T>() {}.in(this.getClass()).getRawType();
    }

    final Foo<String> fooOfString = new Foo<String>() {
    };
    assertEquals(String.class, fooOfString.typeOfT);
  }

  @Test
  void testSetOf() {
    final Type setOfString = Types.setOf(String.class);
    final TypeLiteral<Set<String>> literal = FriendlyTypeLiteral.setOf(String.class);
    assertEquals(setOfString, literal.getType());
    assertEquals(assertDoesNotThrow(() -> TypeInfo.class.getDeclaredField("setOfString").getGenericType()), literal.getType());
  }

  @Test
  void testMapOf_class() {
    final Type mapOfStringToString = Types.mapOf(String.class, String.class);
    final TypeLiteral<Map<String, String>> literal = FriendlyTypeLiteral.mapOf(String.class, String.class);
    assertEquals(mapOfStringToString, literal.getType());
    assertEquals(assertDoesNotThrow(() -> TypeInfo.class.getDeclaredField("mapOfStringToString").getGenericType()), literal.getType());
  }

  @Test
  void testMapOf_typeLiteral() {
    final Type mapOfStringToString = Types.mapOf(String.class, Types.setOf(String.class));
    final TypeLiteral<Map<String, Set<String>>> literal = FriendlyTypeLiteral.mapOf(TypeLiteral.get(String.class), FriendlyTypeLiteral.setOf(String.class));
    assertEquals(mapOfStringToString, literal.getType());
    assertEquals(assertDoesNotThrow(() -> TypeInfo.class.getDeclaredField("mapOfStringToSetOfString").getGenericType()), literal.getType());
  }

  static class TypeInfo {
    Set<String> setOfString;
    Map<String, String> mapOfStringToString;
    Map<String, Set<String>> mapOfStringToSetOfString;
  }
}
