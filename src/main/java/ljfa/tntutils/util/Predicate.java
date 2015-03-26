package ljfa.tntutils.util;

/** A replacement for Java 8's Predicate class */
public interface Predicate<T> {
    boolean test(T t);
}
