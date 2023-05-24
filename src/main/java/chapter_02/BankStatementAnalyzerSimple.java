package chapter_02;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/*
현재 코드에서 발생할 수 있는 문제
- 파일이 비어 있다면?
- 데이터에 문제가 있어서 금액을 파싱하지 못 한다면?
- 행의 데이터가 완벽하지 않다면?
 */
public class BankStatementAnalyzerSimple {
    private static final String RESOURCES = "src/main/resources/";

    public static void main(final String[] args) throws Exception {
        final Path path = Paths.get(RESOURCES + "bank-data-simple.csv");
        final List<String> lines = Files.readAllLines(path);
        double total = 0;
        for(final String line: lines) {
            String[] columns = line.split(",");
            double amount = Double.parseDouble(columns[1]);
            total += amount;
        }

        System.out.println("The total for all transactions is " + total);
    }
}
