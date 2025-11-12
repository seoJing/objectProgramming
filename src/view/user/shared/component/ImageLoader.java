package view.user.shared.component;

import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * 이미지 로딩을 담당하는 유틸리티 클래스
 * 다양한 위치에서 이미지 파일을 찾고 로드합니다.
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
     *
     * @param imageName 이미지 파일명 (확장자 포함)
     * @param width 스케일할 너비
     * @param height 스케일할 높이
     * @return 로드된 ImageIcon, 실패 시 null 반환
     */
    public static ImageIcon loadImage(String imageName, int width, int height) {
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
