package service;

import model.Account;
import model.Transaction;
import model.TransactionType;
import model.User;
import util.SessionManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountService {
    private static final AccountService INSTANCE = new AccountService();
    public static AccountService getInstance() { return INSTANCE; }
    private AccountService() {}

    private final List<Account> mockAccounts = new ArrayList<>();
    private boolean seeded = false;

    /** 현재 사용자 기준 계좌 목록 반환 (최초 1회 목데이터 시드) */
    public List<Account> getAccounts(User user) {
        if (!seeded) {
            seedMockData(user);
            seeded = true;
        }
        return Collections.unmodifiableList(mockAccounts);
    }

    public int getTotalBalance(User user) {
        int sum = 0;
        for (Account a : getAccounts(user)) {
            sum += a.getCurrentBalance();
        }
        return sum;
    }

    /** 해당 계좌의 거래 목록 */
    public List<Transaction> getTransactions(Account account) {
        if (account == null) return List.of();
        return account.getTransactionList();
    }

    // 내부 유틸: 목데이터 시드 & 거래 추가
    private void seedMockData(User user) {
        // 예시 계좌
        Account shinhan = new Account("110-123-456789", "신한은행", 200_000);
        Account kookmin = new Account("123456-78-901234", "국민은행", 150_000);
        Account toss = new Account("7256-78-10234", "토스뱅크", 78_000);
        Account hana = new Account("523456-785-5739", "하나은행", 350_000);
        Account nh = new Account("236-7128-024138", "농협은행", 130_000);


        // 거래 샘플
        // 신한
        addTx(shinhan, TransactionType.INCOME, 50_000, "카카오페이", "용돈", "부모님 용돈", LocalDateTime.now().minusDays(2));
        addTx(shinhan, TransactionType.EXPENSE, 5_800, "스타벅스", "카페", "라떼", LocalDateTime.now().minusDays(1).withHour(10).withMinute(5));
        addTx(shinhan, TransactionType.EXPENSE, 1_250, "지하철", "교통", "환승", LocalDateTime.now().withHour(8).withMinute(24));
        addTx(shinhan, TransactionType.EXPENSE, 18_900, "배달의민족", "식비", "점심 김치찌개", LocalDateTime.now().minusDays(1).withHour(12).withMinute(42));
        addTx(shinhan, TransactionType.INCOME,  20_000, "토스이체",   "이체", "친구 더치페이 정산", LocalDateTime.now().minusDays(1).withHour(21).withMinute(15));
        addTx(shinhan, TransactionType.EXPENSE,  7_200, "GS25",     "간식", "야식 샌드위치",     LocalDateTime.now().withHour(23).withMinute(5));
        addTx(shinhan, TransactionType.EXPENSE,  2_300,  "세븐일레븐", "간식",  "삼각김밥",         LocalDateTime.now().minusDays(3).withHour(8).withMinute(10));
        addTx(shinhan, TransactionType.INCOME,  15_000,  "카카오페이", "이체",  "스터디 정산",       LocalDateTime.now().minusDays(2).withHour(20).withMinute(33));
        addTx(shinhan, TransactionType.EXPENSE, 28_000,  "교보문고",   "교육",  "알고리즘 책",       LocalDateTime.now().minusDays(2).withHour(16).withMinute(5));
        addTx(shinhan, TransactionType.EXPENSE,  6_400,  "맘스터치",   "식비",  "치즈볼+콜라",       LocalDateTime.now().minusDays(1).withHour(13).withMinute(48));

        // 국민
        addTx(kookmin, TransactionType.EXPENSE, 27_900, "쿠팡", "쇼핑", "양말 세트", LocalDateTime.now().minusDays(3).withHour(10).withMinute(5));
        addTx(kookmin, TransactionType.INCOME, 300_000, "급여", "월급", "알바 급여", LocalDateTime.now().minusDays(3).withHour(14).withMinute(0));
        addTx(kookmin, TransactionType.EXPENSE, 12_000, "GS25", "편의점", "간식", LocalDateTime.now().withHour(9).withMinute(48));
        addTx(kookmin, TransactionType.EXPENSE, 42_000, "버거킹",   "식비",  "저녁 세트 2개",    LocalDateTime.now().minusDays(2).withHour(19).withMinute(40));
        addTx(kookmin, TransactionType.EXPENSE, 13_500, "카카오T",  "교통",  "택시 요금",        LocalDateTime.now().minusDays(2).withHour(22).withMinute(10));
        addTx(kookmin, TransactionType.INCOME,  50_000, "N페이포인트", "적립", "포인트 전환",     LocalDateTime.now().minusDays(1).withHour(9).withMinute(10));
        addTx(kookmin, TransactionType.EXPENSE,  9_800,  "에버라인",   "교통",  "지하철",           LocalDateTime.now().minusDays(4).withHour(9).withMinute(2));
        addTx(kookmin, TransactionType.INCOME,  25_000,  "토스이체",   "이체",  "팀원 더치페이",     LocalDateTime.now().minusDays(3).withHour(22).withMinute(41));
        addTx(kookmin, TransactionType.EXPENSE, 14_700,  "이디야커피", "카페",  "바닐라라떼",        LocalDateTime.now().minusDays(2).withHour(11).withMinute(7));
        addTx(kookmin, TransactionType.EXPENSE, 57_000,  "교통카드",   "교통",  "충전",             LocalDateTime.now().minusDays(1).withHour(8).withMinute(19));

        // 토스뱅크
        addTx(toss, TransactionType.INCOME,  120_000, "중고거래",   "수입",   "키보드 판매",      LocalDateTime.now().minusDays(4).withHour(15).withMinute(30));
        addTx(toss, TransactionType.EXPENSE,  9_900,  "넷플릭스",   "구독",   "월 구독료",        LocalDateTime.now().minusDays(3).withHour(6).withMinute(10));
        addTx(toss, TransactionType.EXPENSE,  4_500,  "지하철",     "교통",   "출근",             LocalDateTime.now().minusDays(3).withHour(8).withMinute(35));
        addTx(toss, TransactionType.EXPENSE,  6_800,  "이디야커피", "카페",   "아메리카노+샷",     LocalDateTime.now().minusDays(2).withHour(10).withMinute(20));
        addTx(toss, TransactionType.INCOME,   30_000, "토스이체",   "이체",   "친구 입금",        LocalDateTime.now().minusDays(2).withHour(21).withMinute(55));
        addTx(toss, TransactionType.EXPENSE, 27_300,  "쿠팡",       "쇼핑",   "문구/볼펜 세트",   LocalDateTime.now().minusDays(1).withHour(14).withMinute(12));
        addTx(toss, TransactionType.EXPENSE,    3_900,  "GS25",       "간식",  "핫도그",           LocalDateTime.now().minusDays(5).withHour(21).withMinute(12));
        addTx(toss, TransactionType.INCOME,    70_000,  "네이버페이", "환급",  "취소 환불",         LocalDateTime.now().minusDays(4).withHour(10).withMinute(45));
        addTx(toss, TransactionType.EXPENSE,   19_800,  "배달의민족", "식비",  "치킨너겟",          LocalDateTime.now().minusDays(2).withHour(18).withMinute(27));
        addTx(toss, TransactionType.EXPENSE,   11_400,  "다이소",     "생활",  "수납함/테이프",      LocalDateTime.now().minusDays(1).withHour(15).withMinute(3));

        // 하나
        addTx(hana, TransactionType.EXPENSE,  21_000, "이마트24",   "식비",   "도시락+음료",      LocalDateTime.now().minusDays(5).withHour(12).withMinute(5));
        addTx(hana, TransactionType.EXPENSE,  59_000, "통신요금",   "통신",   "휴대폰 요금",      LocalDateTime.now().minusDays(4).withHour(7).withMinute(45));
        addTx(hana, TransactionType.INCOME,  200_000, "급여",       "월급",   "프로젝트 용역비",  LocalDateTime.now().minusDays(4).withHour(18).withMinute(0));
        addTx(hana, TransactionType.EXPENSE,  15_200, "배민B마트",  "식비",   "과일/우유",        LocalDateTime.now().minusDays(3).withHour(20).withMinute(22));
        addTx(hana, TransactionType.EXPENSE,  33_000, "영화관",     "여가",   "주말 영화",        LocalDateTime.now().minusDays(2).withHour(16).withMinute(10));
        addTx(hana, TransactionType.EXPENSE,  8_900,  "스타벅스",   "카페",   "콜드브루",         LocalDateTime.now().minusDays(1).withHour(9).withMinute(2));
        addTx(hana, TransactionType.EXPENSE,    8_200,  "투썸플레이스","카페", "아메리카노",        LocalDateTime.now().minusDays(5).withHour(9).withMinute(55));
        addTx(hana, TransactionType.EXPENSE,   42_500,  "쿠팡",        "쇼핑", "USB 허브",          LocalDateTime.now().minusDays(3).withHour(13).withMinute(22));
        addTx(hana, TransactionType.INCOME,    35_000,  "카카오뱅크",  "이체", "용돈 입금",         LocalDateTime.now().minusDays(2).withHour(20).withMinute(14));
        addTx(hana, TransactionType.EXPENSE,   27_800,  "파리바게뜨",  "간식", "빵/음료",           LocalDateTime.now().minusDays(1).withHour(17).withMinute(38));

        // 농혀브냉
        addTx(nh, TransactionType.EXPENSE,   3_200,  "버스",       "교통",   "학교 가는 길",     LocalDateTime.now().minusDays(6).withHour(8).withMinute(12));
        addTx(nh, TransactionType.EXPENSE,  12_800,  "학식",       "식비",   "점심 정식",        LocalDateTime.now().minusDays(6).withHour(12).withMinute(30));
        addTx(nh, TransactionType.INCOME,   40_000,  "토스이체",   "이체",   "과제 대행 정산",   LocalDateTime.now().minusDays(5).withHour(21).withMinute(18));
        addTx(nh, TransactionType.EXPENSE,  24_000,  "문구점",     "교육",   "프레젠테이션 재료", LocalDateTime.now().minusDays(4).withHour(15).withMinute(5));
        addTx(nh, TransactionType.EXPENSE,  71_000,  "병원",       "의료",   "진료/약값",        LocalDateTime.now().minusDays(3).withHour(11).withMinute(40));
        addTx(nh, TransactionType.EXPENSE,  10_500,  "빵집",       "간식",   "크루아상/아메리카노", LocalDateTime.now().minusDays(2).withHour(9).withMinute(28));
        addTx(nh, TransactionType.EXPENSE,      5_500,  "버스",        "교통", "하교",             LocalDateTime.now().minusDays(6).withHour(18).withMinute(7));
        addTx(nh, TransactionType.EXPENSE,     16_900,  "CU",          "간식", "과자/음료",         LocalDateTime.now().minusDays(4).withHour(14).withMinute(55));
        addTx(nh, TransactionType.INCOME,      22_000,  "토스이체",    "이체", "공모전 상금 분배",   LocalDateTime.now().minusDays(3).withHour(19).withMinute(31));
        addTx(nh, TransactionType.EXPENSE,     39_000,  "다이소",      "생활", "정리함/문구",       LocalDateTime.now().minusDays(2).withHour(16).withMinute(2));


        mockAccounts.add(shinhan);
        mockAccounts.add(kookmin);
        mockAccounts.add(toss);
        mockAccounts.add(hana);
        mockAccounts.add(nh);

        // 첫 계좌를 선택 상태로
        if (SessionManager.getInstance().getSelectedAccount() == null) {
            SessionManager.getInstance().setSelectedAccount(shinhan);
        }
    }

    private void addTx(Account account, TransactionType type, int amount,
                       String location, String category, String memo, LocalDateTime when) {
        int signed = (type == TransactionType.INCOME) ? amount : -amount;
        int newBal = account.getCurrentBalance() + signed;

        Transaction tx = new Transaction(
                type, amount, location, category, memo, when, newBal, account
        );

        account.getTransactionList().add(tx);
        account.setCurrentBalance(newBal);
    }
}
