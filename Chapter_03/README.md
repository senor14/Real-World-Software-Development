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