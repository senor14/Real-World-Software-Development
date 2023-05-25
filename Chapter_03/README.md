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