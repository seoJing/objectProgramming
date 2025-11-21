package model;


public class Notice {
    private String noticeId; // 공지 ID (예: "N001")
    private String title;
    private String content;
    private String authorId; // 작성한 AdminUser의 ID

    public Notice(String noticeId, String title, String content, String authorId) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
    }

    // Getters
    public String getNoticeId() { return noticeId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthorId() { return authorId; }

    // Setters (수정 기능용)
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
}
