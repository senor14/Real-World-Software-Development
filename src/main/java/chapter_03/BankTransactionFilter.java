package chapter_03;

@FunctionalInterface
public interface BankTransactionFilter<T> {
    boolean test(T t);
}
