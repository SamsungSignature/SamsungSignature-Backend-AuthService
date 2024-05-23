package samsung.signature.authservice.member.util;

import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import samsung.signature.authservice.member.domain.Member;
import samsung.signature.authservice.member.dto.request.LoginForm;
import samsung.signature.authservice.member.exception.MemberErrorCode;
import samsung.signature.authservice.member.respository.MemberRepository;
import samsung.signature.common.exception.SignatureException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInfoUtil {
	private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
	private static final String NUMBER_PATTERN = "(010|011|016|017|018|019)-\\d{4}-\\d{4}";

	public static void chkDuplicateAndPatternByMemberId(
		final MemberRepository memberRepository,
		final String email,
		final String phoneNumber) {
		chkPattern(email, phoneNumber);
		if (memberRepository.existsByEmail(email)) {
			throw new SignatureException(MemberErrorCode.DUPLICATED_USER_EMAIL);
		} else if (memberRepository.existsByPhoneNumber(phoneNumber)) {
			throw new SignatureException(MemberErrorCode.DUPLICATED_USER_PHONE_NUMBER);
		}
	}

	public static void chkPattern(String email, String phoneNumber) {
		if (!isValidEmailAddress(email) || !isValidPhoneNumber(phoneNumber))
			throw new SignatureException(MemberErrorCode.INVALID_ID_PATTERN);
	}

	public static Member chkMemberInfo(
		final MemberRepository memberRepository,
		final PasswordEncoder passwordEncoder,
		final LoginForm loginForm) {
		// id 검증
		Member loginMember = chkMemberId(memberRepository, loginForm.id());
		// password 검증
		chkMemberPwd(passwordEncoder, loginForm, loginMember);
		return loginMember;
	}

	private static Member chkMemberId(
		final MemberRepository memberRepository,
		final String id) {
		if (isValidEmailAddress(id)) {
			return MemberServiceUtil.findByEmail(memberRepository, id);
		} else if (isValidPhoneNumber(id)) {
			return MemberServiceUtil.findByPhoneNumber(memberRepository, id);
		}
		throw new SignatureException(MemberErrorCode.INVALID_USER_IDENTITY);
	}

	private static void chkMemberPwd(PasswordEncoder passwordEncoder, LoginForm loginForm, Member loginMember) {
		if (!passwordEncoder.matches(loginForm.password(), loginMember.getPassword())) {
			throw new SignatureException(MemberErrorCode.INVALID_USER_IDENTITY);
		}
	}

	private static boolean isValidEmailAddress(String email) {
		return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
	}

	private static boolean isValidPhoneNumber(String phoneNumber) {
		return Pattern.compile(NUMBER_PATTERN).matcher(phoneNumber).matches();
	}
}
