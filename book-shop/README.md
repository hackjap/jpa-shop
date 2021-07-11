
# Spring boot  + JPA 를 이용한 Book-shop

## 프로젝트 생성

---
### 라이브러리 
- web, thymeleaf, lombok, jpa, h2 database
- spring-boot-devtools : 서버 재시작 없이 리로딩을 도와줌


### JPA 와 데이터베이스 환경설정 
- apllication.yml 설정 
    - ddl-auto 시 , 테이블 자동생성
    - Log 설정
    
### JPA 개요  
- @Entity, @EntityManger
- Transactoin 시, 데이터가 변경이 적용
- test에서는 transaction이 rollback 되므로 필요시 , @rollback(false)를 주어서 적용이 가능 
 
### 빌드
- ./gradlew clean build 
- /build/lib/ xxx.jar  : 해당 파일 java -jar 로 실행 

### 쿼리 파라미터 로그 남기기 ( 정말 필요한 기능, 강추!!)
- yml파일에서 log 부분에 "org.hibernate.type : trace" 을 추가
- 보통 value(?,?) 이런식으로 로그가 남는다. 따라서 이 로그를 보여주는 외부 라이브러리를 설치해보자.
- https://github.com/gavlyukovskiy/spring-boot-data-source-decorator - P6Spy
  yml에 implementation "com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6" 추가 
- 개발단계에서 좋은 라이브러지만 , 운영단계에서는 성능을 고려해볼 필요가 있음 


## 도메인 분석 설계

---

### 요구사항 분석(기능목록)

- 회원 기능
  - 회원 등록
  - 회원 조회
- 상품 기능
  - 상품 등록 
  - 상품 목록
- 주문 기능
  - 주문 등록
  - 주문 내역
  

### 도메인 모델과 테이블 설계
- JPA에서는 다대다 , 양방향 관계를 가급적 사용하지 말아야 함
  - 다대다 관계의 중간에 매핑 테이블을 생성하여 1:N , N:1 관계로 풀어내야 함 
- 양방향 관계에서 연관관계의 주인을 정해야 하는데, "외래 키가 있는 것을 주인"으로 정하는 것이 좋다
  - ※ 외래 키가 있는 곳을 연관관계의 주인으로 정해라!! 
  - 자동차와 바퀴처럼 인간이 생각하는 주체가 아닌 바퀴를 연관관계의 주인으로 정함 
- 카테고리는 @ManyToMany(다대다 관계)를 사용(사용하면 안되지만, 예시를 위해 사용)


## 엔티스 클래스 개발 

--- 

- 실무에서는 가급적 Getter는 열어두고, Setter는 꼭 필요한 경우에만 사용하는 것을 추천 
- setter 사용 시,나중에 변경 시, 변경 지점을 알기 쉽지 않음 (개별 메소드를 사용 )

### 엔티티 설계시 주의점
- 엔티티에는 가급적이면 Setter를 사용하지 말자.
  - Setter가 모두 열러있다. 변경 포인트가 너무 많아서 유지보수가 어렵다.
  - Setter 대체 방법은 뒤에서 소개하겠음
- 모든 연관관계는 자연로딩으로 설정
  - 즉시로딩은(EAGER) 예측이 어렵고, 어떤 SQL이 실행될지 추적이 어려움. JPQL 실행시 N + 1 문제 발생
  - 실무에서는 모든 연관관계는 지연로딩(LAZY)로 설정
- ! X to One(하나를 가리키는) 은 defalut가 EAGER 이므로  LAZY로 변경해주어야함
- 컬렉션은 필드에서 초기화 하자.
  - null 문제에서 안전함
- 테이블명, 컬럼명 생성 전략 
  - defalut : 카멜 케이스 -> 언더바 ( orderDate -> order_date )
  
- cascade 옵션 : persist를 전파
- 연관관계 편의 매서드 


# 애플리케이션 개발 

## 회원 도메인 개발 

### 구현기능 
- 회원 등록
- 회원 목록 조회 

### 구현
- JPA 모든 데이터변경에는 @Transactionl 이 필요 
  - 데이터 읽기에는 readOnly(읽기전용) 옵션을 적용 ,  
  - 상황에 따라 많은 쪽(읽기,쓰기)에 class레벨의 Transacton을 주고 필요한 부분에  @Transactinoal 을 개별적으로 부여

> @RequiredArgsConstructor
lobok의 annotation으로 private final 변수에 맞춰 자동으로 생성자를 생성하고 주입해줌, 또한 스프링 데이터 JPA가 Entitymager 또한 지원 
>

### 테스트
- @Rollback(value = false) : 테스트 모드시 기본 rollback 처리 되어있어 DB 저장 시 , 옵션
- @Transactional(데이터 변경시에 꼭!!!)
- em.flush 를 해줄경우 rollback 되는 insert 문을 보여줌
- memory db를 이용하여 테스트
  - test/resource/application.yml 생성
  - url : jdbc:h2:mem:test 로 변경
  - 하지만 스프링 부트는 기본적인 설정이없으면 자동으로 메모리 모드로 설정 
- create : 실행시 drop 후 crate 
- create - drop : 종료시점에 drop 하여 깨끗하게 날려줌

## 상품 도메인 개발

### 구현 기능
- 상품 등록
- 상품 목록 조회
- 상품 수정 


### 상품 엔티티 개발 
- 엔티티에 비즈니스 로직을 넣는게 좋음 
 

## 주문 도메인 개발(가장 중요 !) 

### 구현기능
- 상품 주문
- 주문 내역 조회
- 주문 취소 

### 순서
- 주문 엔티티, 주문상품 엔티티 개발
- 주문 레포지토리 개발
- 주문 서비스 개발
- 주문 검색기능 개발
- 주문 기능 테스트  

### 도메인 모델 패턴
- 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 "도메인 모델 패턴" 이라고 함
- 반대로 서비스 계층에서 대부분의 비즈니스 로직을 처리하는 것을 "트랜잭션 스크립트 패턴"이라고 한다.
 




  
