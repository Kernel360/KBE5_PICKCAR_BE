package com.pickcar.auth.presentation.dto.request;

import jakarta.validation.constraints.*;

public record UserInfoRequest(
        @Email(message = "xxx@xxx.xxx 이메일 형식을 입력해야 합니다.")
        @NotBlank(message = "이메일을 입력해주세요.")
        String email,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 6, max = 15, message = "비밀번호는 최소 6자리에서 최대 15자리까지 입력해야 합니다.")
        String password,

        @NotBlank(message = "이름을 입력해주세요.")
        String name,

        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "^(01[0|1|6|7|8|9])[-]?\\d{3,4}[-]?\\d{4}$", message = "휴대폰 번호는 01X-XXXX-XXXX 형식으로 입력해야 합니다.")
        String phoneNumber,

        @NotNull(message = "관리자 또는 회원을 선택해주세요.")
        Boolean isAdmin
){
}