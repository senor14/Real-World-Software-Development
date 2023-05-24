package chapter_02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/*
리팩터링 덕분에 메인 응용프로그램에서 파싱 로직을 구현하는 부분이 사라짐
대신 파싱 기능을 다른 클래스와 메서드에 위임했고, 이 기능을 독립적으로 구현했음
다양한 문제를 처리해야 하는 새 요구 사항이 들어오면, BankStatementCSVParse 클래스로 캡슐화된 기능을 재사용해 구현함
 */
public class BankStatementAnalyzerSRP {
    private static final String RESOURCE = "src/main/resources/";

    public static void main(String[] args) throws IOException {

        final BankStatementCSVParser bankStatementCSVParser = new BankStatementCSVParser();

        final String fileName = args[0];
        final Path path = Paths.get(RESOURCE + fileName);

        final List<String> lines = Files.readAllLines(path);

        final List<BankTransaction> bankTransactions
                = bankStatementCSVParser.parseLinesFromCSV(lines);


        System.out.println("The total for all transactions is " + calculateTotalAmount(bankTransactions));
        System.out.println("Transactions in January " + selectInMonth(bankTransactions, Month.JANUARY));
    }

    public static List<BankTransaction> selectInMonth(final List<BankTransaction> bankTransactions,
                                                      final Month month) {
        final List<BankTransaction> bankTransactionsInMonth = new ArrayList<>();
        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month) {
                bankTransactionsInMonth.add(bankTransaction);
            }
        }
        return bankTransactionsInMonth;
    }

    public static double calculateTotalAmount(final List<BankTransaction> bankTransactions) {
        double total = 0d;
        for (final BankTransaction bankTransaction : bankTransactions) {
            total += bankTransaction.getAmount();
        }
        return total;
    }
}
