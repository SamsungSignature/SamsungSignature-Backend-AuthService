package samsung.signature.authservice.member.util;

import lombok.NoArgsConstructor;
import samsung.signature.authservice.member.domain.Member;
import samsung.signature.authservice.member.exception.MemberErrorCode;
import samsung.signature.authservice.member.respository.MemberRepository;
import samsung.signature.common.exception.SignatureException;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class MemberServiceUtil {
	public static Member findByEmail(
		final MemberRepository memberRepository,
		final String email
	) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new SignatureException(MemberErrorCode.INVALID_USER_IDENTITY));
	}

	public static Member findByPhoneNumber(
		final MemberRepository memberRepository,
		final String phoneNumber
	) {
		return memberRepository.findByPhoneNumber(phoneNumber)
			.orElseThrow(() -> new SignatureException(MemberErrorCode.INVALID_USER_IDENTITY));
	}

	public static Member findById(
		final MemberRepository memberRepository,
		final long memberId
	) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new SignatureException(MemberErrorCode.INVALID_USER_IDENTITY));
	}
}
