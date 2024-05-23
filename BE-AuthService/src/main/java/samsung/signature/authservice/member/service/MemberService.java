package samsung.signature.authservice.member.service;

import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import samsung.signature.authservice.encryption.jwt.JwtTokenProvider;
import samsung.signature.authservice.encryption.jwt.JwtUtils;
import samsung.signature.authservice.encryption.rsa.PsaProvider;
import samsung.signature.authservice.member.domain.Member;
import samsung.signature.authservice.member.dto.request.JoinForm;
import samsung.signature.authservice.member.dto.request.LoginForm;
import samsung.signature.authservice.member.dto.response.NewbieInfo;
import samsung.signature.authservice.member.dto.response.TokenInfo;
import samsung.signature.authservice.member.respository.MemberRepository;
import samsung.signature.authservice.member.util.MemberInfoUtil;
import samsung.signature.authservice.member.util.MemberServiceUtil;
import samsung.signature.authservice.privatekey.domain.PrivateKey;
import samsung.signature.authservice.privatekey.repository.PrivateKeyRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
	private final ObjectMapper objectMapper;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final RedisTemplate<byte[], byte[]> redisTemplate;
	private final JwtTokenProvider jwtTokenProvider;
	private final PsaProvider psaProvider;
	private final PrivateKeyRepository privateKeyRepository;

	@Transactional
	public NewbieInfo register(final JoinForm joinForm) {
		MemberInfoUtil.chkDuplicateAndPatternByMemberId(
			memberRepository,
			joinForm.email(),
			joinForm.phoneNumber()
		);
		String encodedPassword = passwordEncoder.encode(joinForm.password());
		Map<String, String> asyKeyPair = psaProvider.createAsyKeyPair();
		Member newbie = saveNewMember(joinForm, encodedPassword, asyKeyPair);
		return NewbieInfo.from(newbie);
	}

	private Member saveNewMember(
		final JoinForm joinForm,
		final String encodedPassword,
		final Map<String, String> asyKeyPair
	) {
		Member newbie = Member.of(joinForm, encodedPassword, asyKeyPair.get("publicKey"));
		memberRepository.save(newbie);
		privateKeyRepository.save(PrivateKey.of(newbie.getId(), asyKeyPair.get("privateKey")));
		return newbie;
	}

	@Transactional
	public TokenInfo login(final String UID, final LoginForm loginForm) {
		// 사용자 인증
		Member loginMember = MemberInfoUtil.chkMemberInfo(
			memberRepository,
			passwordEncoder,
			loginForm
		);
		return JwtUtils.generateJWTTokens(
			redisTemplate,
			objectMapper,
			jwtTokenProvider,
			loginMember,
			UID
		);
	}

	public void validateId(final String email, final String phoneNumber) {
		MemberInfoUtil.chkDuplicateAndPatternByMemberId(memberRepository, email, phoneNumber);
	}

	public void logout(
		final String accessToken,
		final long expiredTime,
		final String refreshToken) {
		// accessToken blackList 추가
		JwtUtils.invalidateTokens(
			redisTemplate,
			objectMapper,
			expiredTime,
			accessToken,
			refreshToken
		);

	}

	public TokenInfo reissue(
		final long memberId,
		final long expiredTime,
		final String accessToken,
		final String refreshToken,
		final String UID
	) {
		Member loginMember = MemberServiceUtil.findById(memberRepository, memberId);
		return JwtUtils.reissueTokens(
			redisTemplate,
			objectMapper,
			jwtTokenProvider,
			expiredTime,
			accessToken,
			refreshToken,
			loginMember,
			UID
		);
	}
}
