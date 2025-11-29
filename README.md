# 구독 관리형 가계부

> 객체지향 프로그래밍 팀 프로젝트

구독 서비스와 계좌 거래를 통합 관리하는 Java Swing 기반 가계부 애플리케이션입니다.

## 프로젝트 소개

현대인들의 다양한 구독 서비스(Netflix, Spotify 등) 지출을 효과적으로 관리하고, 계좌 거래 내역을 한눈에 파악할 수 있는 데스크톱 애플리케이션입니다.

### 주요 기능

- **구독 서비스 관리**: 구독 추가/수정/삭제, 월별 구독료 자동 계산
- **계좌 관리**: 여러 계좌의 거래 내역 조회 및 잔액 관리
- **거래 내역 추적**: 입금/출금 내역 기록 및 분류
- **사용자 인증**: 일반 사용자 및 관리자 모드 지원
- **통계 분석**: 구독료 통계 및 지출 분석 (관리자)

### 개발 목적

- 객체지향 설계 원칙(SOLID) 학습 및 적용
- Java Swing을 활용한 GUI 프로그래밍 실습
- 팀 협업을 통한 소프트웨어 개발 프로세스 경험

## 기술 스택

- **Language**: Java 17
- **GUI Framework**: Java Swing
- **Build Tool**: Eclipse / IntelliJ IDEA / VSCode
- **Design Pattern**: Singleton, Template Method

## 프로젝트 구조

```
objectProgramming/
├── src/
│   ├── main/
│   │   └── Main.java                             # 애플리케이션 진입점
│   ├── model/                                    # 데이터 모델 계층
│   │   ├── User.java                             # 사용자 모델
│   │   ├── UserList.java                         # 사용자 컬렉션 관리 (Singleton)
│   │   ├── Account.java                          # 계좌 모델
│   │   ├── Transaction.java                      # 거래 내역 모델
│   │   ├── TransactionType.java                  # 거래 타입 enum
│   │   ├── Ledger.java                           # 가계부 모델
│   │   └── SubscriptionService.java              # 구독 서비스 모델
│   ├── view/                                     # UI 계층
│   │   ├── MainFrame.java                        # 메인 프레임
│   │   ├── layout/                               # 레이아웃 템플릿
│   │   │   ├── BaseLayout.java                   # 기본 레이아웃 (Template Method Pattern)
│   │   │   ├── UserLayout.java                   # 사용자 레이아웃
│   │   │   └── AdminLayout.java                  # 관리자 레이아웃
│   │   ├── user/                                 # 사용자 화면
│   │   │   ├── UserSidePanel.java                # 사용자 사이드 네비게이션
│   │   │   ├── MainPanel.java                    # 메인 대시보드
│   │   │   ├── AccountPanel.java                 # 계좌 관리
│   │   │   ├── SubscriptionPanel.java            # 구독 목록
│   │   │   ├── SubscriptionDetailPanel.java      # 구독 상세
│   │   │   ├── TransactionPanel.java             # 거래 내역
│   │   │   ├── TransactionDetailPanel.java       # 거래 상세
│   │   │   ├── MyPagePanel.java                  # 마이페이지
│   │   │   ├── AlertPanel.java                   # 알림 관리
│   │   │   ├── SettingPanel.java                 # 설정
│   │   │   ├── StorePanel.java                   # 스토어/구독 시장
│   │   │   ├── StoreDetailPanel.java             # 스토어 상세
│   │   │   └── GroupPanel.java                   # 그룹
│   │   ├── admin/
│   │   │   ├── AdminSidePanel.java               # 관리자 사이드 네비게이션
│   │   │   ├── AdminMainPanel.java               # 관리자 메인
│   │   │   ├── AdminStatisticsPanel.java         # 통계 분석
│   │   │   ├── AdminSubscriptionMachinePanel.java# 구독 기계 관리
│   │   │   └── AdminSubscriptionManagePanel.java # 구독 서비스 관리
│   │   └── login/
│   │       └── LoginPanel.java                   # 로그인 화면
│   └── util/                                     # 유틸리티 계층
│       ├── Router.java                           # 화면 네비게이션 관리 (Singleton)
│       ├── Routes.java                           # 화면 경로 상수
│       ├── SessionManager.java                   # 세션 상태 관리 (Singleton)
│       └── UIConstants.java                      # UI 상수 (색상, 폰트 등)
├── .editorconfig                                 # 코드 포맷팅 설정
├── .gitignore
└── README.md
```

### 아키텍처 설명

#### 계층 구조

- **Model**: 비즈니스 로직과 데이터 구조 정의
- **View**: 사용자 인터페이스 및 화면 구성
- **Util**: 공통 유틸리티 및 상수 관리

#### 주요 디자인 패턴

- **Singleton Pattern**: `SessionManager`, `UserList`, `Router` - 전역 상태 관리
- **Template Method Pattern**: `BaseLayout` - 공통 레이아웃 구조 정의
- **Enum Pattern**: `TransactionType` - 타입 안전성 보장

## 팀원 및 역할

| 이름 | 학과 | 학번 | 역할 | 담당 기능 |
| ---- | ------ | ----- | ---- | --------- |
| 서진규 | 컴퓨터공학과 | 202211461 | 팀장 | 유저 사이드 |
| 이성찬 | 컴퓨터공학과 | 202211495 | DB구현 | 유저 사이드 |
| 손혜원 | 컴퓨터공학과 | 202411388 | GUI구현 | 유저 사이드 |
| 차태훈 | 컴퓨터공학과 | 202211536 | GUI구현 | 어드민 사이드 |
| 조제현 | 컴퓨터공학과 | 202313039 |  DB구현 | 어드민 사이드 |
| 김민아 | 응용통계학과 | 202311045 | Auth구현 | 로그인 |

## 실행 방법

### 사전 요구사항

- Java 17 이상
- Eclipse / IntelliJ IDEA / VSCode (Java Extension Pack)

### 1. Eclipse에서 실행

```bash
# 1. 프로젝트 클론
git clone [repository-url]

# 2. Eclipse 실행
# File → Open Projects from File System → 프로젝트 폴더 선택

# 3. 실행
# src/main/Main.java 우클릭 → Run As → Java Application
```

### 2. IntelliJ IDEA에서 실행

```bash
# 1. 프로젝트 클론
git clone [repository-url]

# 2. IntelliJ IDEA 실행
# File → Open → 프로젝트 폴더 선택

# 3. 실행
# src/main/Main.java 열기 → Run 버튼 클릭
```

### 3. VSCode에서 실행

```bash
# 1. 프로젝트 클론
git clone [repository-url]

# 2. VSCode 실행 및 프로젝트 폴더 열기
code objectProgramming

# 3. Java Extension Pack 설치 (최초 1회)

# 4. 실행
# src/main/Main.java 열기 → F5 또는 Run 버튼 클릭
```

### 4. 명령줄에서 실행

```bash
# 1. 컴파일
javac -d bin src/**/*.java

# 2. 실행
java -cp bin main.Main
```

## 코딩 컨벤션

### 1. 코드 스타일

- **들여쓰기**: 스페이스 4칸
- **최대 줄 길이**: 120자
- **문자 인코딩**: UTF-8
- **줄바꿈**: LF (Unix 스타일)

프로젝트에 포함된 `.editorconfig` 파일이 자동으로 적용됩니다.

### 2. 명명 규칙

- **클래스**: PascalCase (예: `UserList`, `SessionManager`)
- **메서드/변수**: camelCase (예: `getCurrentUser`, `userName`)
- **상수**: UPPER_SNAKE_CASE (예: `MAX_SIZE`, `DEFAULT_COLOR`)
- **패키지**: lowercase (예: `model`, `view.user`)

### 3. 상수 사용

하드코딩 대신 상수 클래스를 사용하세요:

```java
// ❌ 나쁜 예
cardLayout.show(container, "MAIN");
label.setFont(new Font("맑은 고딕", Font.BOLD, 24));

// ✅ 좋은 예
cardLayout.show(container, Routes.MAIN);
label.setFont(UIConstants.TITLE_FONT);
```

### 4. 화면 추가 시 패턴

새로운 화면을 추가할 때는 기존 패턴을 따르세요:

```java
// 1. Routes.java에 경로 상수 추가
public static final String NEW_SCREEN = "NEW_SCREEN";

// 2. Panel 클래스 생성 (UserLayout 또는 AdminLayout 상속)
public class NewPanel extends UserLayout {
    public NewPanel() {
        super();
        JPanel content = createContent();
        setContent(content);
    }

    private JPanel createContent() {
        // 화면 내용 구현
    }
}

// 3. SidePanel에 등록
contentContainer.add(new NewPanel(), Routes.NEW_SCREEN);
```

## 개발 가이드

### 디버깅

```java
// 현재 로그인 사용자 확인
User currentUser = SessionManager.getInstance().getCurrentUser();

// 라우팅 확인
Router.getInstance().navigateTo(Routes.MAIN);

// 사용자 목록 확인
List<User> users = UserList.getInstance().getAll();
```

### 주의사항

- `SessionManager`는 세션 상태만 관리합니다. 비즈니스 로직은 Model 클래스에 작성하세요.
- 모든 화면 이름은 `Routes` 클래스의 상수를 사용하세요.
- UI 스타일은 `UIConstants`의 상수를 사용하세요.

## 주요 업데이트

### v1.0.0
- 기본 프로젝트 구조 및 아키텍처 설계
- 사용자 로그인 및 인증 기능
- 구독 서비스 관리 기능
- 계좌 및 거래 내역 관리
- 사용자/관리자 모드 분리
- 통계 분석 기능

## 라이선스

This project is for educational purposes only.

## 참고 자료

- [Java Swing Tutorial](https://docs.oracle.com/javase/tutorial/uiswing/)
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)
- [Git Collaboration Guide](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests)
