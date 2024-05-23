package samsung.signature.authservice.encryption.jwt;

import java.nio.charset.StandardCharsets;

import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import samsung.signature.authservice.member.domain.Member;
import samsung.signature.authservice.member.dto.response.TokenInfo;
import samsung.signature.common.utils.RedisUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {
	public static void invalidateTokens(
		final RedisTemplate<byte[], byte[]> redisTemplate,
		final ObjectMapper objectMapper,
		final long accessExpiresIn,
		final String accessToken,
		final String refreshToken) {
		// accessToken blackList 추가
		saveToken(
			redisTemplate,
			objectMapper,
			accessExpiresIn,
			accessToken
		);
		// refreshToken 삭제
		removeRefreshToken(redisTemplate, refreshToken);
	}

	public static TokenInfo generateJWTTokens(
		final RedisTemplate<byte[], byte[]> redisTemplate,
		final ObjectMapper objectMapper,
		final JwtTokenProvider jwtTokenProvider,
		final Member member,
		final String UID) {
		// tokens 생성
		TokenInfo tokenInfo = jwtTokenProvider.createTokenInfo(member, UID);
		// refresh Token 저장
		saveToken(
			redisTemplate,
			objectMapper,
			tokenInfo.refreshExpiresIn(),
			tokenInfo.refreshToken()
		);
		return tokenInfo;
	}

	public static TokenInfo reissueTokens(
		final RedisTemplate<byte[], byte[]> redisTemplate,
		final ObjectMapper objectMapper,
		final JwtTokenProvider jwtTokenProvider,
		final long accessExpiresIn,
		final String accessToken,
		final String refreshToken,
		final Member member,
		final String UID
	) {
		invalidateTokens(redisTemplate, objectMapper, accessExpiresIn, accessToken, refreshToken);
		return generateJWTTokens(redisTemplate, objectMapper, jwtTokenProvider, member, UID);
	}

	private static void removeRefreshToken(
		final RedisTemplate<byte[], byte[]> redisTemplate,
		final String refreshToken
	) {
		redisTemplate.delete(refreshToken.getBytes(StandardCharsets.UTF_8));
	}

	private static void saveToken(
		final RedisTemplate<byte[], byte[]> redisTemplate,
		final ObjectMapper objectMapper,
		final long expiresIn,
		final String jwtToken) {

		RedisUtils.putWithExpiredTime(
			redisTemplate,
			objectMapper,
			expiresIn,
			"",
			jwtToken
		);

	}
}
