package chapter_03;

@FunctionalInterface
public interface BankTransactionsSummarizer<T> {
    double summarize(double accumulator, T t);
}
