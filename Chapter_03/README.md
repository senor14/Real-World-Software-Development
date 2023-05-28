Chapter_03
============
### 개방/폐쇄 원칙
- 간단한 기능부터 구현한다. 특정 금액 이상의 모든 입출금 내역을 검색하는 메서드를 구현한다. '이 메서드를 어디에 정의해야
할까?' 라는 의문이 먼저 떠오른다. 간단한 findTransactions() 메서드를 포함하는 BankTransactionFinder 클래스를 따로 만들 수
있다. 하지만 이미 BankTransactionProcessor 클래스를 선언했다. 지금 같은 상황에서는 메서드를 추가하려고 클래스를 새로
만들어도 크게 좋은 점이 없다. 새로 클래스를 추가한 탓에 여러 이름이 생기면서 다양한 동작 간의 관계를 이해하기가 어려워지고
전체 프로젝트가 복잡해지기 때문이다. 이런 메서드는 일종의 처리 기능을 담당하므로 BankTransactionProcessor 클래스 안에
정의하면 나중에 관련 메서드를 조금 더 쉽게 찾을 수 있다.
- BankStatementProcessor 에 추가한 3개의 메서드 (findTransactionGreaterThanEqual(), findTransactionGreaterThanEqual(), 
findTransactionsInMonthAndGreater()) 같이 복사해서 추가하고 수정하는 방식에는 여러 한계가 있다.
  * 거래 내역의 여러 속성을 조합할수록 코드가 점점 복잡해진다.
  * 반복 로직과 비즈니스 로직이 결합되어 분리하기가 어려워진다.
  * 코드를 반복한다.
- 이런 상황에서 개방/폐쇄 원칙을 적용하면 코드를 직접 바꾸지 않고 해당 메서드나 클래스의 동작을 바꿀 수 있다.
여기서는 BankTransactionFilter 인터페이스를 만들어 문제를 해결한다. BankTransactionFilter 인터페이스는 완료된
BankTransaction 객체를 인수로 받아 불리언을 반환하는 test() 메서드 한 개를 포함한다. 여기서 test() 메서드는
BankTransaction 의 모든 속성에 접근할 수 있으므로 이를 이용해 특정 조건의 참, 거짓 여부를 판단한다.
- 이제 새로운 요구 사항에 맞는 필터를 구현한 후(BankTransactionIsInFebruaryAndExpensive 클래스), 아래와 같이 
findTransactions() 메서드의 인수로 필터의 인스턴스를 전달할 수 있다.
```
final List<BankTransaction transactions 
= bankStatementProcessor.findTransactions(new BankTransactionIsInFebruaryAndExpensive());
```
- 하지만 요구 사항이 있을 때마다 별도의 클래스를 만들어야 한다. 이는 큰 의미가 없는 코드를 반복해서 만드는 귀찮은
작업이다. **람다 표현식** 과 **메서드 레퍼런스** 를 사용하여 이름 없이 인터페이스 구현 객체를 코드 블록 형태로
전달할 수 있다.
```
final List<BankTransaction transactions 
= bankStatementProcessor.findTransactions(bankTransaction ->
            bankTransaction.getDate().getMonth() == Month.FEBRUARY
            && bankTransaction.getAmount() >= 1_000);
);
```
- 요약하자면, 다음과 같은 장점 덕분에 개방/폐쇄 원칙을 사용한다.
  * 기존 코드를 바꾸지 않으므로 기존 코드가 잘못될 가능성이 줄어든다.
  * 코드가 중복되지 않으므로 기존 코드의 재사용성이 높아진다.
  * 결합도가 낮아지므로 코드 유지보수성이 좋아진다.

### 인터페이스 문제
- 지금까지 주어진 조건을 만족하는 거래 내역을 검색하도록 유연한 메서드를 만들었다. 리팩터리을 하고 나니
BankTransactionProcessor 클래스 안의 다른 메서드는 어떻게 되는 건지 의문이 생긴다. 인터페이스로 이들을 옮겨야 할까?
아니면 다른 클래스로 옮기는 게 좋을까? 결국 Chapter2에서 구현한 서로 다른 세 개의 메서드를 어떻게 처리하느냐의 문제가
남았다. (calculateTotalAmount(), calculateTotalInMonth(), calculateTotalForCategory())
- 한 인터페이스에 모든 기능을 추가하는 **갓 인터페이스**를 만드는 일은 피해야 한다.

#### 갓 인터페이스
- BankTransactionProcessor 클래스가 API 역할을 한다고 생각할 수 있다. 결과적으로 여러 입출금 내역 분석기 구현에서
결합을 제거하도록 인터페이스를 정의해야 한다. 이 인터페이스(BankTransactionProcessor)는 입출금 내역 분석기가 구현해야 할
모든 기능을 포함한다.
```
interface BankTransactionProcessor {
    double calculateTotalAmount();
    double calculateTotalInMonth(Month month);
    double calculateTotalInJanuary();
    double calculateAverageAmount();
    double calculateAverageAmountForCategory(Category category);
    List<BankTransaction> findTransactions(BankTransactionFilter bankTransactionFilter);
}
```
- 이 접근 방식에는 몇 가지 문제가 있다. 우선 모든 헬퍼 연산이 명시적인 API 정의에 포함되면서 인터페이스가 복잡해진다.
또한 Chapter2에서 살펴본 갓 클래스와 비슷한 인터페이스가 만들어진다. 실제로 위 코드의 인터페이스는 모든 연산을 담당하고,
심지어 두 가지 형식의 결합이 발생한다.
  * 자바의 인터페이스는 모든 구현이 지켜야 할 규칙을 정의한다. 즉 구현 클래스는 인터페이스에서 정의한 모든 연산의
  구현 코드를 제공해야 한다. 따라서 인터페이스를 바꾸면 이를 구현한 코드도 바뀐 내용을 지원하도록 갱신되어야 한다.
  더 많은 연산을 추가할수록 더 자주 코드가 바뀌며, 문제가 발생할 수 있는 범위도 넓어진다.
  * 월, 카테고리 같은 BankTransaction 의 속성이 calculateAverageForCategory(), calculateTotalInJanuary() 처럼 메서드
  이름의 일부로 사용되었다. 인터페이스가 도메인 객체의 특정 접근자에 종속되는 문제가 생겼다. 도메인 객체의 세부 내용이
  바뀌면 인터페이스도 바뀌어야 하며 결과적으로 구현 코드도 바뀌어야 한다.
- 이런 이유에서 보통 작은 인터페이스를 권장한다. 그래야 도메인 객체의 다양한 내부 연산으로 디펜던시를 최소화할 수 있다.

#### 지나친 세밀함
- 인터페이스는 작을수록 좋은 걸까? 아래 코드는 각 동작을 별도의 인터페이스로 정의하는 극단적인 예다.
BankTransactionProcessor 클래스는 이 모든 인터페이스를 구현해야 한다.
```
interface CalculateTotalAmount {
    double calculateTotalAmount();
}

interface CalculateAverage {
    double caulcateAverage();
}

interface CalculataeTotalInMonth {
    double calculateTotalInMonth(Month month);
}
```
- 지나치게 인터페이스가 세밀해도 코드 유지보수에 방해가 된다. 실제로 위 코드는 **안티 응집도** 문제가 발생한다. 즉
기능이 여러 인터페이스로 분산되므로 필요한 기능을 찾기가 어렵다. 자주 사용하는 기능을 쉽게 찾을 수 있어야 유지보수성도
좋아진다. 더욱이 인터페이스가 너무 세밀하면 복잡도가 높아지며, 새로운 인터페이스가 계속해서 프로젝트에 추가된다.

### 명시적 API vs 암묵적 API
- 이 문제는 개방/폐쇄 원칙을 적용하면 연산에 유연성을 추가하고 가장 공통적인 상황을 클래스로 정의할 수 있다. 이 상황에서는
BankTransactionProcessor 의 다양한 구현을 기대하지 않으므로 인터페이스의 필요성이 사라진다. 또한 전체 응용프로그램에
도움되는 메서드를 제공하지도 않는다. 결론적으로 코드베이스에 불필요한 추상화를 추가해 일을 복잡하게 만들 필요가 없다.
BankTransactionProcessor 는 단순히 입출금 내역에서 통계적 연산을 수행하는 클래스일 뿐이다.
- 일반적인 findTransactions() 메서드를 쉽게 정의할 수 있는 상황에서 findTransactionsGreaterThanEqual() 처럼 구체적으로
메서드를 정의해야 하는지 의문도 생긴다. 이런 딜레마를 명시적 API 제공 vs 암묵적 API 제공 문제라고 부른다.
- 양측 모드 장단점이 있다. findTransactionGreaterThanEqual() 같은 메서드는 자체적으로 어떤 동작을 수행하는지 잘 설명되어
있고, 사용하기 쉽다. API의 가독성을 높이고 쉽게 이해하도록 메서드 이름을 서술적으로 만들었다. 하지만 이 메서드의 용도가
특정 상황에 국한되어 각 상황에 맞는 새로운 메서드를 많이 만들어야 하는 상황이 벌어진다. 반면 findTransactions()와 같은
메서드는 처음 사용하기가 어렵고, 문서화를 잘해놓아야 한다. 하지만 거래 내역을 검색하는 데 필요한 모든 상황을 단순한 API로
처리할 수 있다. 어떤 것이 좋은 방법인지는 정해져 있지 않다. 이는 필요한 질문의 종류에 따라 달라질 수 있기 때문이다.
findTransactionGreaterThanEqual() 메서드가 가장 흔히 사용하는 연산이라면, 사용자가 쉽게 이해하고 사용하도록 이를 명시적
API로 만드는 것이 합리적인 방법이다.

#### 도메인 클래스 vs 원싯값
- BankTransactionSummarizer 의 인터페이스를 간단하게 정의하면서 double이라는 윈싯값을 결과로 반환하는데, 이는 일반적으로
좋은 방법이 아니다. 원싯값으로는 다양한 결과를 반환할 수 없어 유연성이 떨어지기 때문이다. 예를 들어 summarizeTransaction()
메서드는 현재 double을 반환한다. 다양한 결과를 포함하도록 메서드 시그니처를 바꾸려면 모든 BankTransactionProcessor의 구현을
바꿔야 한다.
- double을 감싸는 새 도메인 클래스 Summary를 만들면 이 문제를 해결할 수 있다. 새 클래스에 필요한 필드와 결과를 언제든
추가할 수 있다. 또한 이 기법을 이용하면 도메인의 다양한 개념 간의 결합을 줄이고, 요구 사항이 바뀔 때 연쇄적으로 코드가
바뀌는 일도 최소화할 수 있다.

### 다양한 형식으로 내보내기
#### 도메인 객체 소개
- 숫자
  * calculateAverageInMonth 처럼 어떤 연산의 반환 결과가 필요한 사용자가 있을 것이다. 이때 결괏값은 double 이다.
  이미 살펴봤듯이 double 을 반환하면 가장 간단하게 프로그램을 구현할 수 있지만 요구 사항이 바뀔 때 유연하게 대처할 수
  없다. 내보내기 기능이 double 을 입력으로 받는다고 가정하자. 이 상황에서는 내보내기 기능을 호출하는 모든 코드의 결괏값의
  형식을 바꿔야 하며 새로운 버그가 발생할 가능성이 크다.
- 컬렉션
  * findTransaction() 이 반환하는 입출금 목록을 원하는 사용자도 있을 것이다. Iterable 을 반환하면 상황에 맞춰서 처리하기
  때문에 유연성을 높일 수 있다. 이때 유연성은 좋아지지만 오직 컬렉션만 반환해야 한다는 제약이 따른다. 어떻게 하면 목록,
  기타 요약 정보 등 다양한 종류의 결과를 반환할 수 있을까?
- 특별한 도메인 객체
  * 사용자가 내보내려는 요약 정보를 대표하는 SummaryStatistics 라는 새로운 개념을 만들 수 있다. 도메인 객체는 자신의
  도메인과 관련된 클래스의 인스턴스다. 도메인 객체를 이용하면 결합을 깰 수 있다. 새로운 요구 사항이 생겨서 추가 정보를
  내보내야 한다면 기존 코드를 바꿀 필요 없이 새로운 클래스의 일부로 이를 구현할 수 있다.
- 더 복잡한 도메인 객체
  * Report 처럼 조금 더 일반적이며 거래 내역 컬렉션 등 다양한 결과를 저장하는 필드를 포함하는 개념을 만들 수 있다.
  사용자의 요구 사항이 무엇이며 더 복잡한 정보를 내보내야 하는지 여부에 따라 사용할 도메인 객체가 달라진다. 어떤 상황이든
  Report 객체를 생산하는 부분과 이를 소비하는 부분이 서로 결합하지 않는다는 큰 장점이 있다.

#### 적절하게 인터페이스를 정의하고 구현하기
- 요약 정보를 저장하는 도메인 객체 생성(SummaryStatistics)
- 내보낼 형식이 무엇인지를 파악했으므로 이를 구현하는 API가 필요하다. Exporter라는 인터페이스를 정의해 다양한 내보내기
구현 코드가 다른 코드와 결합하지 않도록 방지한다. 이는 개방/폐쇄 원칙으로 다시 연결된다. JSON으로 내보내든, XML로 내보내든
같은 인터페이스를 구현하면 되므로 다양한 내보내기 기능을 편리하게 구현할 수 있다. 처음에는 아래와 같이 인터페이스를 정의한다.
export() 메서드는 SummaryStatistics 객체를 인수로 받아 void를 반환한다.
```
// Exporter 인터페이스의 나쁜 예
public interface Exporter {
    void export(SummaryStatistics summaryStatistics);
}
```
- 인터페이스를 이렇게 정의하면 다음과 같은 문제가 발생한다.
  * void 반환 형식은 아무 도움이 되지 않고, 기능을 파악하기도 어렵다. 메서드가 무엇을 반환하는지 알 수 없기 때문이다.
  export() 메서드 자체가 아무것도 반환하지 않으므로 다른 구현 메서드에서 어떤 작업을 진행하고, 이를 기록하거나 화면에
  출력할 가능성이 크다. 인터페이스로부터 얻을 수 있는 정보가 아무것도 없다.
  * void를 반환하면 어서션으로 결과를 테스트하기도 매우 어렵다. 예상한 값과 실제 결괏값을 어떻게 비교할까? 안타깝게도
  void를 반환하면 아무 결과도 없다.
- String을 반환하는 API를 만든다. (Exporter). Exporter가 반환한 텍스트를 이용해 이를 출력하거나, 파일에 저장하거나,
어디론가 전송한다. 텍스트 문자열은 어서션으로 직접 비교할 수 있으므로 쉽게 테스트할 수 있다.
```
// Exporter 인터페이스의 좋은 예
public interface Exporter {
    String export(SummaryStatistics summaryStatistics);
}
```
- 정보를 내보내는 API를 정의했으므로 Exporter 인터페이스를 준수하는 다양한 내보내기 기능을 구현할 수 있다.
  (HtmlExporter)

### 예외 처리
- 입출금 내역 분석기 소프트웨어가 아래와 같은 오작동을 일으킨다면 어떻게 해야 할까?
  * 데이터를 적절하게 파싱하지 못한다면?
  * 입출금 내역을 포함하는 CSV 파일을 읽을 수 없다면?
  * 응용프로그램을 실행하는 하드웨어에 램이나 저장 공간이 부족하다면?
- 이런 문제가 발생하면 어디서 문제가 발생했는지를 보여주는 스택 트레이스와 함께 이상한 오류 메시지가 나타난다.

#### 예외를 사용해야 하는 이유
- BankStatementCSVParser 를 살펴보자. 어떻게 파싱 문제를 해결할 수 있을까? 예를 들면 파일의 CSV 행이 아래와 같이 예상하지
못한 형식으로 구성되었을 수 있다.
  * CSV 행의 열이 세 개를 초과한다.
  * CSV 행의 열이 세 개 미만이다.
  * 날짜 등 일부 열의 자료 형식이 잘못되었다.
- 고전적인 C 프로그래밍에서는 수많은 if 조건을 추가해 암호 같은 오류 코드를 반환했다. 하지만 그 방법에는 여러 단점이 
존재한다. 먼저 전역으로 공유된 가변 상태에 의존해 최근에 발생한 오류를 검색해야 한다. 이 때문에 코드 부분이 따로 분리되어
이해하기가 어려워진다. 결과적으로 코드를 유지보수하기 어렵다. 또한 어떤 값이 실제 값인지 아니면 오류를 가리키는 값인지
구분하기 어렵다. 강력한 형식 시스템이 있었다면 이 문제를 해결하는 데 조금이나마 도움이 되었을 것이다. 마지막으로 제어 흐름이
비즈니스 로직과 섞이면서 코드를 유지보수하거나 프로그램의 일부를 따로 테스트하기도 어려워진다.
- 자바는 이런 문제를 해결하도록 예외를 일급 언어 기능으로 추가하고 다음과 같은 장점을 제공한다.
  * **문서화**: 메서드 시그니처 자체에 예외를 지원한다.
  * **형식 안정성**: 개발자가 예외 흐름을 처리하고 있는지를 형식 시스템이 파악한다.
  * **관심사 분리**: 비즈니스 로직과 예외 회복이 각각 try/catch 블록으로 구분된다.
- 다만 예외 기능으로 복잡성이 증가한다는 단점이 생긴다. 자바는 두 가지 종류의 예외를 지원한다.
  * **확인된 예외**: 회복해야 하는 대상의 예외다. 자바에서는 메서드가 던질 수 있는 확인된 예외 목록을 선언해야 한다. 아니면
  해당 예외를 try/catch 로 처리해야 한다.
  * **미확인 예외**: 프로그램을 실행하면서 언제든 발생할 수 있는 종류의 예외다. 확인된 예외와 달리 메서드 시그니처에
  명시적으로 오류를 선언하지 않으면 호출자도 이를 꼭 처리할 필요가 없다.
- 자바 예외 클래스는 계층적으로 잘 조직되어 있다. Error와 RuntimeException 클래스는 미확인 예외이며 Throwable의 서브클래스다.
보통 이런 오류는 잡지 않는다. Exception 클래스는 일반적으로 프로그램에서 잡아 회복해야 하는 오류를 가리킨다.