package com.example.reservation.Facilitys.DTO;

import com.example.reservation.Facilitys.User;

/**
 * 일반 사용자 조회 응답 DTO.
 *
 * 이전에는 User 엔티티를 그대로 반환해서 JSON에 password(BCrypt 해시), email, role까지
 * 모두 노출됐다. 해시라도 외부에 노출되면 오프라인 대입 공격의 재료가 되므로
 * 다른 사용자에게 보여줘도 되는 값만 담는 응답 객체를 따로 만들었다.
 */
public class UserResponse {

    private final long id;
    private final String name;

    private UserResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
