package br.com.gabrielzrz.util;



import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Zorzi
 */
public class LambdaUtil {

    // Constructor
    private LambdaUtil() {
    }

    // General
    public static <T> T findOneBy(Collection<T> collection) {
        return collection.stream().findFirst().orElse(null);
    }

    public static <T> T findOneBy(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).findAny().orElse(null);
    }

    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <T, R> List<R> mapTo(Collection<T> collection, Function<T, R> function) {
        return ListUtils.isNotEmpty(collection)
                ? collection.stream().map(function).collect(Collectors.toList())
                : Collections.emptyList();
    }

    public static <T, R> List<R> mapTo(Collection<T> collection, Predicate<T> predicate, Function<T, R> function) {
        return ListUtils.isNotEmpty(collection)
                ? collection.stream().filter(predicate).map(function).collect(Collectors.toList())
                : Collections.emptyList();
    }

    public static <T, R, A, C extends Collection<R>> C mapTo(Collection<T> collection, Function<T, R> function, Collector<R, A, C> collector) {
        return collection.stream().map(function).collect(collector);
    }

    public static <T, R> List<R> mapDistinctTo(Collection<T> collection, Function<T, R> function) {
        return collection.stream().map(function).distinct().collect(Collectors.toList());
    }

    public static <T, R> String mapToString(Collection<T> collection, Predicate<T> predicate, Function<T, R> function, String delimiter) {
        return ListUtils.isNotEmpty(collection)
                ? collection.stream()
                .filter(predicate)
                .map(function)
                .map(Object::toString)
                .collect(Collectors.joining(delimiter))
                : "";
    }

    public static <T, R> String mapToString(Collection<T> collection, Function<T, R> function, String delimiter) {
        return ListUtils.isNotEmpty(collection)
                ? collection.stream()
                .map(function)
                .map(Object::toString)
                .collect(Collectors.joining(delimiter))
                : "";
    }

    public static <T, R> List<R> flatMapTo(Collection<T> collection, Function<T, Collection<R>> function) {
        return collection.stream()
                .map(function)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static <T, R> Map<R, List<T>> groupBy(Collection<T> collection, Function<T, R> function) {
        return CollectionUtils.isEmpty(collection) ? Collections.emptyMap() : collection.stream().collect(Collectors.groupingBy(function));
    }

    public static <T, R> Map<R, T> groupBySingleValue(Collection<? extends T> collection, Function<T, R> function) {
        return CollectionUtils.isEmpty(collection) ? Collections.emptyMap() : collection.stream().collect(Collectors.toMap(function, Function.identity(), (existing, replacement) -> existing));
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> cast(List<?> list, Class<T> clazz) {
        if (list.stream().allMatch(clazz::isInstance)) {
            return (List<T>) list;
        } else {
            throw new IllegalArgumentException("List contains elements that are not of type: " + clazz.getName());
        }
    }
}
