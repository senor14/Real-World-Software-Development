package chapter_02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

 /*
 현재 코드에서 발생할 수 있는 문제
 - 파일이 비어 있다면?
 - 데이터에 문제가 있어서 금액을 파싱하지 못 한다면?
 - 행의 데이터가 완벽하지 않다면?
  */
public class BankTransactionAnalyzerSimple {
    private static final String RESOURCE = "src/main/resources/";

    public static void main(String[] args) throws IOException {

        final Path path = Paths.get(RESOURCE + args[0]);
        final List<String> lines = Files.readAllLines(path);
        double total = 0d;
        final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (final String line : lines) {
            final String[] columns = line.split(",");
            final LocalDateTime date = LocalDateTime.parse(columns[0], DATE_PATTERN);
            if (date.getMonth() == Month.JANUARY) {
                final double amount = Double.parseDouble(columns[1]);
                total += amount;
            }
        }

        System.out.println("The total for all transactions is January is " + total);
    }
}
