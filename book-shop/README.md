
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



