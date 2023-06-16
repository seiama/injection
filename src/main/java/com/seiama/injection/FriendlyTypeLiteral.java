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

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a generic type {@code T}.
 *
 * <p>An extension of {@link TypeLiteral} with some helpful methods.</p>
 *
 * @param <T> the type
 * @since 1.0.0
 */
@SuppressWarnings("UnstableApiUsage")
public abstract class FriendlyTypeLiteral<T> extends TypeLiteral<T> {
  /**
   * Returns a {@link TypeLiteral} modelling a {@link Set} whose elements are of type {@code elementType}.
   *
   * @return a {@link TypeLiteral}
   * @see Types#setOf(Type)
   * @since 1.0.0
   */
  @SuppressWarnings("checkstyle:MethodName")
  public static <T> @NotNull TypeLiteral<Set<T>> setOf(final @NotNull Class<T> elementType) {
    return setOf(TypeLiteral.get(elementType));
  }

  /**
   * Returns a {@link TypeLiteral} modelling a {@link Set} whose elements are of type {@code elementType}.
   *
   * @return a {@link TypeLiteral}
   * @see Types#setOf(Type)
   * @since 1.0.0
   */
  @SuppressWarnings("checkstyle:MethodName")
  public static <T> @NotNull TypeLiteral<Set<T>> setOf(final @NotNull TypeLiteral<T> elementType) {
    @SuppressWarnings("unchecked")
    final TypeLiteral<Set<T>> literal = (TypeLiteral<Set<T>>) TypeLiteral.get(Types.setOf(elementType.getType()));
    return literal;
  }

  /**
   * Returns a {@link TypeLiteral} modelling a {@link Map} whose keys are of type {@code keyType} and whose values are of type {@code valueType}.
   *
   * @return a {@link TypeLiteral}
   * @see Types#mapOf(Type, Type)
   * @since 1.0.0
   */
  public static <K, V> @NotNull TypeLiteral<Map<K, V>> mapOf(final @NotNull Class<K> keyType, final @NotNull Class<V> valueType) {
    return mapOf(TypeLiteral.get(keyType), TypeLiteral.get(valueType));
  }

  /**
   * Returns a {@link TypeLiteral} modelling a {@link Map} whose keys are of type {@code keyType} and whose values are of type {@code valueType}.
   *
   * @return a {@link TypeLiteral}
   * @see Types#mapOf(Type, Type)
   * @since 1.0.0
   */
  public static <K, V> @NotNull TypeLiteral<Map<K, V>> mapOf(final @NotNull TypeLiteral<K> keyType, final @NotNull TypeLiteral<V> valueType) {
    @SuppressWarnings("unchecked")
    final TypeLiteral<Map<K, V>> literal = (TypeLiteral<Map<K, V>>) TypeLiteral.get(Types.mapOf(keyType.getType(), valueType.getType()));
    return literal;
  }

  /**
   * Creates a type literal by substituting formal type variables with the given actual type arguments.
   *
   * @param args the actual type arguments
   * @return a type literal
   * @see Types#newParameterizedType(Type, Type...)
   * @see TypeToken#where(TypeParameter, TypeToken)
   * @since 1.0.0
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public @NotNull TypeLiteral<T> where(final @NotNull TypeArgument<?> @NotNull ... args) {
    // https://github.com/google/guice/issues/657
    TypeToken<T> token = EvenMoreTypes.token(this);
    for (final TypeArgument arg : args) {
      token = token.where(arg, arg.actual);
    }
    return EvenMoreTypes.literal(token);
  }

  /**
   * Resolves this type in the context of {@code declaringClass}.
   *
   * @param declaringClass the context class
   * @return a type literal
   * @since 1.0.0
   */
  public @NotNull TypeLiteral<T> in(final @NotNull Class<?> declaringClass) {
    final TypeToken<?> token = TypeToken.of(declaringClass).resolveType(this.getType());
    @SuppressWarnings("unchecked")
    final TypeLiteral<T> literal = (TypeLiteral<T>) EvenMoreTypes.literal(token);
    return literal;
  }
}
