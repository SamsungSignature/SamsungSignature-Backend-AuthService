package samsung.signature.authservice.member.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import samsung.signature.authservice.member.dto.request.JoinForm;
import samsung.signature.authservice.member.dto.request.LoginForm;
import samsung.signature.authservice.member.dto.response.NewbieInfo;
import samsung.signature.authservice.member.dto.response.TokenInfo;
import samsung.signature.authservice.member.service.MemberService;
import samsung.signature.authservice.member.util.CookieUtil;
import samsung.signature.common.response.MessageBody;
import samsung.signature.common.response.ResponseEntityFactory;
import samsung.signature.common.response.ResponseEntityFactoryWithHeaders;

@RequiredArgsConstructor
@RequestMapping("/auth-service")
@RestController
public class MemberController {
	private final MemberService memberService;

	@GetMapping("/test")
	ResponseEntity<String> test(){
		return ResponseEntity.ok("TEST OK");
	}

	@GetMapping("/v1/members/validate")
	public ResponseEntity<MessageBody<Void>> validate(
		@RequestParam(name = "email", required = true) String email,
		@RequestParam(name = "phone_number", required = true) String phoneNumber
	) {
		memberService.validateId(email, phoneNumber);
		return ResponseEntityFactory.ok("사용할 수 있는 아이디입니다.");
	}

	@PostMapping("/v1/members")
	public ResponseEntity<MessageBody<NewbieInfo>> signUp(@RequestBody JoinForm joinForm) {
		return ResponseEntityFactory.created(
			"회원가입이 완료되었습니다.",
			memberService.register(joinForm)
		);
	}

	@PostMapping("/v1/members/signin")
	public ResponseEntity<MessageBody<TokenInfo>> signIn(
		@RequestHeader(name = "UID", required = true) String UID,
		@RequestBody LoginForm loginForm
	) {
		TokenInfo tokens = memberService.login(UID, loginForm);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.SET_COOKIE, CookieUtil.setRefreshTokenCookie(tokens.refreshToken()).toString());

		return ResponseEntityFactoryWithHeaders.ok(
			headers,
			"성공적으로 로그인이 되었습니다.",
			tokens);
	}

	@GetMapping("/v1/members/logout")
	public ResponseEntity<MessageBody<Void>> logout(
		@RequestHeader(name = "Access-Token", required = true) String accessToken,
		@RequestHeader(name = "Access-Expired-Time", required = true) long expiredTime,
		@CookieValue(name = "refresh_token", required = true) String refreshToken) {
		memberService.logout(accessToken, expiredTime, refreshToken);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.SET_COOKIE, CookieUtil.deleteRefreshTokenCookie().toString());

		return ResponseEntityFactoryWithHeaders.ok(headers, "성공적으로 로그아웃되었습니다.");
	}

	@PostMapping("/v1/members/tokens")
	public ResponseEntity<MessageBody<TokenInfo>> reissue(
		@RequestHeader(name = "Member-Id", required = true) long memberId,
		@RequestHeader(name = "Access-Token", required = true) String accessToken,
		@RequestHeader(name = "Access-Expired-Time", required = true) long expiredTime,
		@CookieValue(name = "refresh_token", required = true) String refreshToken,
		@RequestHeader(name = "UID", required = true) String UID
	) {
		TokenInfo tokens = memberService.reissue(memberId, expiredTime, accessToken, refreshToken, UID);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.SET_COOKIE, CookieUtil.setRefreshTokenCookie(tokens.refreshToken()).toString());

		return ResponseEntityFactoryWithHeaders.created(
			headers,
			"refresh token 재발급 완료",
			tokens
		);
	}
}
