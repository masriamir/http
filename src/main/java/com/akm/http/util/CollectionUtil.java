package com.akm.http.util;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Collection utility functions.
 *
 * @author Amir
 * @since 0.4
 */
public final class CollectionUtil {

  /**
   * Filters the given Collection with the provided Predicate and returns the new List. No changes
   * are made to the underlying data.
   *
   * @param collection the Collection to filter
   * @param filter     the Predicate to filter the Collection with
   *
   * @return the filtered Collection
   *
   * @param <T> the type of elements in this collection
   */
  public static <T> Collection<T> filterCollection(
      final Collection<T> collection, final Predicate<T> filter) {
    return collection.parallelStream().filter(filter)
        .collect(Collectors.toList());
  }

  /**
   * Filters the given Map with the provided Predicate and returns the new map. No changes are made
   * to the underlying data.
   *
   * @param map    the Map to filter
   * @param filter the Predicate to filter the Map with
   *
   * @return the filtered Map
   *
   * @param <K> the type of keys maintained by this map
   * @param <V> the type of mapped values
   */
  public static <K, V> Map<K, V> filterMap(final Map<K, V> map,
      final Predicate<Map.Entry<K, V>> filter) {
    return map.entrySet().parallelStream().filter(filter).collect(
        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private CollectionUtil() {
  }
}
