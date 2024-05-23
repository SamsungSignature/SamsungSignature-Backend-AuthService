package samsung.signature.authservice.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import samsung.signature.common.exception.ErrorCode;
@AllArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCode {
	DUPLICATED_USER_EMAIL(400,"이미 존재하는 이메일입니다.","MEMBER-001"),
	DUPLICATED_USER_PHONE_NUMBER(400,"이미 존재하는 전화번호입니다.","MEMBER-002"),
	INVALID_USER_IDENTITY(400, "회원 아이디 및 비밀번호를 확인해주세요.", "MEMBER-003"),
	INVALID_ID_PATTERN(400, "아이디는 이메일 또는 전화번호 형식이어야 합니다.", "MEMBER-004"),
	;
	private final int statusCode;
	private final String message;
	private final String errorCode;

}
