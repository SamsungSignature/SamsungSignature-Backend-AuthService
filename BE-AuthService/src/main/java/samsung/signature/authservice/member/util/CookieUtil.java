package samsung.signature.authservice.member.util;

import org.springframework.http.ResponseCookie;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CookieUtil {
	public static ResponseCookie setRefreshTokenCookie(String refreshToken) {
		return ResponseCookie.from("refresh_token", refreshToken)
			.path("/")
			.sameSite("None")
			.httpOnly(true)
			.secure(true)
			.maxAge(60L * 60L * 24L)
			.build();
	}

	public static ResponseCookie deleteRefreshTokenCookie() {
		return ResponseCookie.from("refresh_token", "")
			.path("/")
			.sameSite("None")
			.httpOnly(true)
			.secure(true)
			.maxAge(0L)
			.build();
	}
}
