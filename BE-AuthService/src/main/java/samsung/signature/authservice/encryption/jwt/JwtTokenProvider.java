package samsung.signature.authservice.encryption.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import samsung.signature.authservice.member.domain.Member;
import samsung.signature.authservice.member.dto.response.TokenInfo;

@Component
public class JwtTokenProvider {
	private final JwtTokenProperties jwtTokenProperties;
	private final Key key;

	@Autowired
	public JwtTokenProvider(final JwtTokenProperties jwtTokenProperties) {
		this.jwtTokenProperties = jwtTokenProperties;
		byte[] keyBytes = Decoders.BASE64.decode(jwtTokenProperties.getSecretKey());
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public TokenInfo createTokenInfo(Member member, String uid) {
		long now = System.currentTimeMillis();
		return TokenInfo.builder()
			.accessToken(createJWT(member, uid, now + jwtTokenProperties.getAccessTokenExpireTime()))
			.refreshToken(createJWT(member, uid, now + jwtTokenProperties.getRefreshTokenExpireTime()))
			.createdAt(now)
			.refreshExpiresIn(jwtTokenProperties.getRefreshTokenExpireTime())
			.accessExpiresIn(jwtTokenProperties.getAccessTokenExpireTime())
			.build();
	}

	private String createJWT(Member member, String uid, long date) {
		return Jwts.builder()
			.setSubject(String.valueOf(member.getId()))
			.claim("uid", uid)
			.signWith(key, SignatureAlgorithm.HS512)
			.setExpiration(new Date(date))
			.compact();
	}

}
