Chapter_02
=============
### KISS 원칙
keep it short and simple <br/>
기능을 만들 때, 응용프로그램 코드를 한 개의 클래스로 구현 <br/>
아직 파일이 존재하지 않거나 파일 내용을 파싱할 때 발생하는 문제를 해결하기 위한 예외 처리를 신경 쓰지 않음 <br/>

### 코드 유지보수성과 안티 패턴
* 구현하는 코드가 가졌으면 하는 속성 목록
  - 특정 기능을 담당하는 코드를 쉽게 찾을 수 있어야 함
  - 코드가 어떤 일을 수행하는지 쉽게 이해할 수 있어야 함
  - 새로운 기능을 쉽게 추가하거나 기존 기능을 쉽게 제거할 수 있어야 함
  - **캡슐화**가 잘 되어 있어야 함. 즉 코드 사용자에게는 세부 구현 내용이 감춰져 있으므로 
  사용자가 쉽게 코드를 이해하고, 기능을 바꿀 수 있어야 함
* 안티 패턴
  - 새로운 요구 사항이 생길 때마다 복사, 붙여넣기로 이를 해결하려면 다음과 같은 문제가 생김.
  이는 효과적이지 않은 해결 방법으로 잘 알려져 있으며, **안티 패턴**이라고 부름
    + 한 개의 거대한 **갓 클래스** 때문에 코드를 이해하기 어렵다.
    + **코드 중복** 때문에 코드가 불안정하고 변화에 쉽게 망가진다.
* 갓 클래스
  - 하나의 거대한 클래스가 모든 일을 수행함
  - 기존 코드 로직을 갱신해야 할 때, 코드를 찾기 어려움
  - 갓 클래스를 피하기 위해 **단일 책임 원칙(SRP)**을 지켜야 함
* 코드 중복
  - 코드가 하드코딩돼 있으면 기존의 기능을 바꾸려면 여러 곳의 코드를 바꿔야 하고 버그가 발생할 가능성이 커짐

#### 결론 
* 코드를 간결하게 유지하는 것도 중요하지만, KISS 원칙을 남용하지 않아야 함

### 단일 책임 원칙
- **단일 책임 원칙(SRP)** 은 쉽게 관리하고 유지보수하는 코드를 구현하는 데 도움을 주는 포괄적인 소프트웨어
개발 지침
- SRP를 적용법
  * 한 클래스는 한 기능만 책임진다.
  * 클래스가 바뀌어야 하는 이유는 오직 하나여야 한다.
    + 코드가 바뀌는 이유가 한 가지가 아니라면, 여러 장소에서 코드 변경이 발생하므로 코드 유지보수가 어려워짐
    또한 코드를 이해하고 바꾸기 어렵게 만드는 요인이 됨
> **BankTransactionAnalyzerSimple** 는 어떻게 바뀌어야 할까?
> 1. 입력 읽기
> 2. 주어진 형식의 입력 파싱
> 3. 결과 처리
> 4. 결과 요약 리포트