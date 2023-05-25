package chapter_03;

import java.time.Month;

public class BankTransactionIsInFebruaryAndExpensive implements BankTransactionFilter<BankTransaction>{
    @Override
    public boolean test(final BankTransaction bankTransaction) {
        return bankTransaction.getDate().getMonth() == Month.FEBRUARY
                && bankTransaction.getAmount() >= 1_000;
    }
}
