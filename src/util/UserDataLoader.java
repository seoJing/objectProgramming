package util;

import model.User;
import model.UserList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserDataLoader {

    private static final String USER_FILE_PATH = "src/resources/data/users.txt";

    public static void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE_PATH))) {

            String line;
            while ((line = br.readLine()) != null) {

                if (line.isBlank()) continue;

                // 공백 기준으로 split → users.txt 형식 그대로 사용
                String[] parts = line.split(" ");

                String id = parts[0];
                String rawPassword = parts[1];  // 규칙에 맞는 비밀번호
                String name = parts[2];
                String gender = parts[3];
                int age = Integer.parseInt(parts[4]);
                String occupation = parts[5];
                String residence = parts[6];
                String phone = parts[7];
                boolean admin = Boolean.parseBoolean(parts[8]);

                // 회원가입 규칙과 동일하게 해시 적용
                String passwordHash = PasswordUtil.hashPasswordWithIdSalt(id, rawPassword);

                User user = new User(
                        id,
                        passwordHash,
                        name,
                        gender,
                        age,
                        occupation,
                        residence,
                        phone,
                        admin
                );

                UserList.getInstance().add(user);
            }

            System.out.println(" users.txt 로딩 완료. 총 " 
                + UserList.getInstance().size() + "명 등록됨");

        } catch (IOException e) {
            System.err.println(" users.txt 로드 실패: " + e.getMessage());
        }
    }
}
