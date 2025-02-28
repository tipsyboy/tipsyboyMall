# 

<!-- 영상 -->
<!-- 문서 구성 -->

<!-- 🎯 프로젝트 동기 및 목적 -->
## 🎯 프로젝트 동기 및 목적
객체지향 프로그래밍과 웹 개발 기술에 대한 이해를 높이기 위해 이 프로젝트를 시작했습니다. 
특히, Spring Boot와 Vue3를 활용하여 프론트엔드와 백엔드를 모두 경험하고, 서비스 설계 과정을 익히는 데 목표를 두었습니다. 
또한, 사용자 인증 및 권한 관리, 이미지 업로드와 같은 기능을 구현하며 자주 사용되는 기술을 연습하고자 했습니다.

- 엔드투엔드 개발 이해 -> API 서버를 개발하고, 프론트엔드와 통합하여 사용하는 과정을 경험. FE/BE 각 영역의 역할에 대한 이해
- CRUD 기능, 이미지 업로드, 검색 기능을 포함한 기본 기능 구현
- Spring Security를 사용한 사용자 인증 및 권한 관리 구현
- 앱 동작시 발생하는 여러 예외에 대해 관심사 분리 및 전역 예외처리
- JPA와 QueryDsl을 경험하고 왜 사용하는지 탐구
- Vue3를 통해 프론트엔드 인터페이스 설계 및 SRP 구성 이해

<!-- ## 🧾 구현 기능 -->
## 🧾 구현 기능
- 회원
  - [x] 회원가입 및 로그인 기능 구현
  - [x] 역할 기반 접근 제어 

- 상품
  - [x] 상품 등록, 수정, 삭제, 조회
  - [x] 이미지 업로드 기능 - Multipart File 처리
  - [x] 상품 상태 관리 / 주문 관리 
  - [x] 댓글 / 대댓글 기능 구현
  - [x] 댓글 추천 / 비추천 기능

- 검색 및 필터링
   - [x] 검색 기능 구현

[//]: # (- 거래 기능)
[//]: # (- [ ] 상품 구매 요청 및 상태 업데이트 기능 &#40;예약, 거래 완료 등&#41;)

[//]: # (## 💡 진행하며 느낀점)

[//]: # (## 😔 아쉬운점 / 이후 개선사항)


<!-- ## 🔍 탐구와 개선 -->
## 🔍 탐구 생활
<details>
  <summary>
    ✅ 예외 처리 구조 개선
  </summary>

1. 예외 처리 구조 개선
- 기존에 코드 전반에 흩어져 있던 지저분한 예외 처리를 정리.
- 주요 예외를 커스텀 예외 클래스로 분리하여 책임 분리와 가독성을 개선.
- 예외를 처리하는 패키지 구조를 정리하여 유지보수를 용이하게 함.

2. ExceptionHandler 및 RestControllerAdvice 활용
- Spring의 @ExceptionHandler와 @RestControllerAdvice를 활용하여 글로벌 예외 처리 구현.
- 예외 처리 로직을 통합하여 중복 코드를 제거하고 응답 형식을 일관성 있게 구성.
- REST API 에러 응답을 표준화(HTTP 상태 코드 + 에러 메시지 + 추가 정보).

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> baseExceptionHandler(BaseException e) {
        // ...
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validException(MethodArgumentNotValidException e) {
        // ...
    }
}
```

3. 구현 과정
- 프로젝트 내에서 발생 가능한 예외들을 정리.
- 예외들에 대해 각각의 커스텀 예외 클래스 작성.
- @RestControllerAdvice를 활용해 예외를 일괄 처리하는 글로벌 핸들러 구현.
- 프로젝트 내 공통 에러 응답.
- 기존 코드에 산재된 try-catch 블록을 제거하고 예외를 일관된 방식으로 처리.

```java
@Getter
public class AuthException extends BaseException {

    public AuthException(ExceptionType exceptionType) {
        super(exceptionType);
    }

}
```

```java
public enum AuthExceptionType implements ExceptionType {

    // 404
    AUTH_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    // 409
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록되어 있는 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록되어 있는 닉네임입니다.");

    private final HttpStatus statusCode;
    private final String message;

    AuthExceptionType(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public HttpStatus statusCode() {
        return this.statusCode;
    }

    @Override
    public String message() {
        return this.message;
    }
}
```

4. 성과
- 예외 처리 로직의 가독성, 유지보수성, 확장성이 크게 향상.
- 개발 과정에서 예외 처리에 소요되는 시간 단축.
- API 호출 시 클라이언트가 에러를 더 명확히 이해할 수 있도록 응답이 개선됨.
</details>

<details>
  <summary>
    ✅ JPA와 QueryDsl의 활용
  </summary>

### JPA와 QueryDsl의 활용
기존 데이터베이스 접근 기술에서 JDBC를 사용한 DB 접근은 기본적인 CRUD 코드를 작성할 때도 복잡하고 반복적인 쿼리를 작성해야 하는 불편함이 있었다. 
또한, DB와 Java 객체의 패러다임 불일치 문제로 인해 비즈니스 로직과 데이터 접근 로직들이 뒤섞이는 문제가 발생했다.
이를 보완하기 위해 ORM 기술이 도입되었으며, 이번 프로젝트를 진행하는 동안 DB 접근 기술의 발전 흐름을 알아보고 정리한다.

#### 1. JDBC + Native Query 기반 접근
```java
public List<Item> getItems(String title, String seller) throws SQLException {
    String sql = "SELECT * FROM item WHERE title LIKE ? AND seller LIKE ?";
    
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    
    List<Item> items = new ArrayList<>();

    try {
        con = getConnection();
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, memberId);
        rs = pstmt.executeQuery();
        while (rs.next()) {
          Item item = new Item();
          item.setId(rs.getLong("id"));
          item.setTitle(rs.getString("title"));
          item.setSeller(rs.getString("seller"));
          items.add(item);
        }
    } catch (SQLException e) {
        log.error("db error", e);
        throw e;
    } finally {
        close(con, pstmt, rs);
    }
  }

private void close(Connection con, Statement stmt, ResultSet rs) {  
  // ... DB 커넥션을 종료하는 과정 ...  
}  
  
private Connection getConnection() throws SQLException {  
  // ... DB 커넥션을 얻는 과정 ...  
}  
```  
문제점
1. 단순 CRUD 작업에도 복잡한 긴 코드들이 반복적으로 나타난다.
2. 자그마한 수정에도 쿼리를 수정해야 할 필요가 생긴다. 즉, SQL 쿼리에 의존적이다.
3. DB 예외 처리를 위한 코드들이 비즈니스 로직으로 침범한다.
4. DB 데이터들을 가져와 단순 매핑만 했기 때문에 엔티티 즉, 객체에 대한 신뢰성이 떨어진다.
5. DB-객체 지향의 패러다임 불일치 문제가 발생한다.
6. 동적 쿼리의 작성이 매우 불편하다.
7. 문자열로 쿼리가 작성되므로 타입 안정성이 보장되지 않는다.


#### 2. JPA 도입과 JPQL

JPA(Java Persistence API)는 ORM(Object-Relational Mapping) 기술을 통해 SQL을 자동으로 생성하고,
객체 중심으로 데이터를 다룰 수 있도록 지원한다.

```java
@Repository
public class ItemJpaRepository {
  @PersistenceContext
  private EntityManager em;
  
  public List<Item> getItems(String title, String seller) {
    return em.createQuery(
                    "SELECT i FROM Item i WHERE i.title LIKE :title AND i.seller LIKE :seller", Item.class)
            .setParameter("title", "%" + title + "%")
            .setParameter("seller", "%" + seller + "%")
            .getResultList();
  }
}
```

해결
- JPA는 결과를 Entity에 매핑을 지원해 줌으로써 데이터-객체 간의 패러다임의 불일치문제를 해결한다. 
- 패러다임의 불일치 문제 해결을 통해 좀 더 객체 지향적으로 코드를 작성할 수 있게 해준다. 
- 복잡한 데이터 매핑 로직들이 제거되었다.

여전한 문제점
- JPQL로 변경되었으나, 쿼리가 여전히 문자열 기반으로 작성되기에 휴먼에러 가능성이 있다. 
- 여전히 동적 쿼리 작성이 불편하다.

#### 3. QueryDsl

QueryDsl은 동적 쿼리를 쉽게 지원하고 타입 안정성을 확보하는 라이브러리이다.

```java
@Repository
public class ItemQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public ItemQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<Item> getItems(String title, String seller) {
        return queryFactory
                .selectFrom(item)
                .where(
                        titleLike(title),
                        sellerLike(seller)
                )
                .orderBy(item.id.desc())
                .fetch();
    }

    private BooleanExpression titleLike(String title) {
        return title != null ? item.title.contains(title) : null;
    }

    private BooleanExpression sellerLike(String seller) {
        return seller != null ? item.seller.contains(seller) : null;
    }
}
```

해결
- 자바 코드를 통한 쿼리 작성으로 문법 오류/오타 등의 휴먼 에러를 컴파일 단계에서 확인, 타입 안정성을 높인다.
- 동적 쿼리를 편하게 작성할 수 있게 해준다.

#### 4. Spring Data JPA

JPA과 QueryDsl의 도입으로 Java DB 접근 기술에서 발생할 수 있는 문제점들을 다수 해결할 수 있었다.
하지만, 처음 문제점들중 두 가지가 남아있다.

1. 단순 CRUD 작업에도 복잡한 긴 코드들이 반복적으로 나타난다. >> 아직 미해결
2. 자그마한 수정에도 쿼리를 수정해야 할 필요가 생긴다. 즉, SQL 쿼리에 의존적이다. >> 아직 미해결
3. DB 예외 처리를 위한 코드들이 비즈니스 로직으로 침범한다. >> JPA의 도입으로 해결
4. DB 데이터들을 가져와 단순 매핑만 했기 때문에 엔티티 즉, 객체에 대한 신뢰성이 떨어진다. >> JPA의 도입으로 해결
5. DB-객체 지향의 패러다임 불일치 문제가 발생한다. >> JPA의 도입으로 해결
6. 동적 쿼리의 작성이 매우 불편하다. >> QueryDsl로 해결
7. 문자열로 쿼리가 작성되므로 타입 안정성이 보장되지 않는다. >> QueryDsl로 해결

Spring Data JPA는 Spring Framework의 프로젝트 중 하나로 JDBC와 JPA의 복잡한 설정을 간소화하며, 
기본적인 CRUD 코드를 자동적으로 생성한다. 
따라서, 프로젝트를 진행하면서 늘어나는 도메인들의 기본적이고 반복적인 CRUD 작성을 피할수 있어 중요한 쿼리들에 집중할 수 있도록 도와준다.

```java
public interface ItemRepository extends JpaRepository<Item, Long> {
}
```
Spring Data JPA는 위처럼 interface 선언만으로 기본적인 쿼리를 사용할 수 있게 해준다.

### 정리

이번 정리를 통해 JDBC → JPA + JPQL → QueryDSL로 발전해 나가면서,
각 단계에서의 불편함을 피부로 느끼면서 새로운 기술들이 어떤 문제를 해결하고 어떤 이점을 제공하는지를 이해할 수 있었다.


</details>

<!-- ERD 추가 -->

<!-- 🛠️ 개발환경 -->
## 🛠️ 개발환경
- Java 17
- Spring Boot 3.2.0
- Dependency
    - Spring Web
    - Spring Security
    - Spring Data JPA
    - Lombok
