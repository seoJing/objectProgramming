package view.user.shared.component;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * 이미지 로딩을 담당하는 유틸리티 클래스
 * 다양한 위치에서 이미지 파일을 찾고 로드합니다.
 * 서비스명의 변형(예: YouTube_Premium, 유튜브_프리미엄)을 기본명(예: youtube)으로 매칭합니다.
 */
public class ImageLoader {

    private static final String[] POSSIBLE_PATHS = {
        "/resources/images/",
        "/src/resources/images/",
        "resources/images/",
        "src/resources/images/"
    };

    /**
     * 이미지를 로드하고 지정된 크기로 스케일합니다.
     * 서비스명에서 기본 이름만 추출해서 이미지를 찾습니다.
     *
     * @param serviceName 서비스명 (예: "YouTube_Premium", "유튜브_프리미엄", "Netflix")
     * @param width 스케일할 너비
     * @param height 스케일할 높이
     * @return 로드된 ImageIcon, 실패 시 null 반환
     */
    public static ImageIcon loadImage(String serviceName, int width, int height) {
        // 서비스명에서 이미지명 추출 (기본 서비스명만 사용)
        String imageName = extractBaseImageName(serviceName);
        if (imageName == null || imageName.isEmpty()) {
            return null;
        }

        String imagePath = findImagePath(imageName);

        if (imagePath == null) {
            return null;
        }

        try {
            java.awt.image.BufferedImage img = ImageIO.read(new File(imagePath));
            if (img == null) {
                return null;
            }
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (Exception e) {
            System.err.println("이미지 로드 실패: " + imagePath + " (" + e.getMessage() + ")");
            return null;
        }
    }

    /**
     * 서비스명에서 기본 이미지명을 추출합니다.
     * 예: "YouTube_Premium" → "youtube.png"
     *     "유튜브_프리미엄" → "유튜브.png"
     *     "Netflix" → "netflix.png"
     *     "넷플릭스_베이직" → "넷플릭스.png"
     *
     * @param serviceName 서비스명
     * @return 기본 이미지명 (확장자 포함)
     */
    private static String extractBaseImageName(String serviceName) {
        if (serviceName == null || serviceName.isEmpty()) {
            return null;
        }

        // 언더스코어나 하이픈으로 분리된 경우 첫 번째 부분만 추출
        String baseName = serviceName.split("[_\\-]")[0].toLowerCase().trim();

        // 한글 서비스명 매핑
        String mappedName = mapKoreanServiceName(baseName);

        return mappedName + ".png";
    }

    /**
     * 한글 서비스명을 영문으로 매핑합니다.
     * 예: "유튜브" → "youtube", "넷플릭스" → "netflix"
     *
     * @param koreanName 한글 서비스명
     * @return 영문 서비스명
     */
    private static String mapKoreanServiceName(String koreanName) {
        switch (koreanName) {
            // OTT & 스트리밍
            case "유튜브":
                return "youtube";
            case "넷플릭스":
                return "netflix";
            case "스포티파이":
                return "spotify";
            case "멜론":
                return "melon";
            case "웨이브":
                return "wavve";
            case "티빙":
                return "tving";
            case "디즈니플러스":
                return "disney+";
            case "라프텔":
                return "laftel";
            case "왓챠":
                return "watcha";

            // 음악
            case "벅스":
                return "bugs";
            case "지니뮤직":
                return "genie";
            case "flo":
                return "flo";
            case "네이버바이브":
                return "vibe";
            case "애플뮤직":
                return "apple music";

            // 쇼핑
            case "쿠팡와우":
                return "coupang";
            case "네이버플러스멤버십":
                return "naver";
            case "컬리패스":
                return "naver";
            case "신세계유니버스":
                return "naver";

            // 도서
            case "밀리의서재":
                return "millie";
            case "리디셀렉트":
                return "ridi";
            case "예스24북클럽":
                return "yes24";

            // 교육
            case "클래스101":
                return "class101";

            // 생산성 & 소프트웨어
            case "마이크로소프트365":
                return "microsoft365";
            case "에버노트":
                return "evernote";
            case "어도비":
                return "adobe";
            case "챗gpt":
            case "챗gpt_플러스":
            case "chatgpt":
                return "chatgpt";
            case "줌":
                return "zoom";
            case "슬랙":
                return "slack";
            case "드롭박스":
                return "dropbox";
            case "구글":
                return "google";
            case "노션":
                return "notion";
            case "deepl":
                return "deepl";

            // 게임
            case "닌텐도":
                return "nintendo";
            case "ps":
                return "playstation";
            case "xbox":
                return "xbox";

            // 기타
            case "요기요":
                return "yogiyo";
            case "세탁특공대":
                return "laundry";
            case "apple":
                return "apple";
            case "아이튠즈":
                return "itunes";

            default:
                return koreanName;
        }
    }

    /**
     * 이미지 파일을 찾습니다.
     * 여러 가능한 경로를 시도합니다.
     *
     * @param imageName 이미지 파일명 (확장자 포함)
     * @return 찾은 이미지의 절대 경로, 찾지 못하면 null 반환
     */
    public static String findImagePath(String imageName) {
        String projectPath = new File("").getAbsolutePath();

        // 여러 경로를 시도
        for (String relativePath : POSSIBLE_PATHS) {
            String fullPath = projectPath + relativePath + imageName;
            File file = new File(fullPath);
            if (file.exists()) {
                return fullPath;
            }
        }

        // 상대 경로로도 시도
        for (String relativePath : POSSIBLE_PATHS) {
            String relativeFull = "." + relativePath + imageName;
            File file = new File(relativeFull);
            if (file.exists()) {
                return file.getAbsolutePath();
            }
        }

        return null;
    }

    /**
     * 이미지가 존재하는지 확인합니다.
     *
     * @param imageName 이미지 파일명 (확장자 포함)
     * @return 이미지가 존재하면 true, 아니면 false
     */
    public static boolean imageExists(String imageName) {
        return findImagePath(imageName) != null;
    }
}
