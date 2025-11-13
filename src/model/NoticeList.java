package model;

import java.util.ArrayList;
import java.util.List;

public class NoticeList {
    private static NoticeList instance;
    private List<Notice> notices;

    private NoticeList() {
        this.notices = new ArrayList<>();
        // 테스트용 공지사항 1개 미리 추가
        this.notices.add(new Notice("N001", "환영합니다", "가계부 앱 관리자입니다.", "admin"));
    }

    public static NoticeList getInstance() {
        if (instance == null) {
            instance = new NoticeList();
        }
        return instance;
    }

    public void addNotice(Notice notice) {
        if (notice != null) {
            this.notices.add(notice);
        }
    }

    public boolean removeNotice(String noticeId) {
        Notice notice = findById(noticeId);
        if (notice != null) {
            return this.notices.remove(notice);
        }
        return false;
    }

    public Notice findById(String noticeId) {
        if (noticeId == null) return null;

        for (Notice notice : notices) {
            if (noticeId.equals(notice.getNoticeId())) {
                return notice;
            }
        }
        return null;
    }

    public List<Notice> getAllNotices() {
        // 원본 리스트의 복사본을 반환
        return new ArrayList<>(this.notices);
    }
}
