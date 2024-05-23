package samsung.signature.authservice.encryption.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Component
public class JwtTokenProperties {
	@Value("${jwt.secret-key}")
	private String secretKey;
	@Value("${jwt.expired.access}")
	private long accessTokenExpireTime;
	@Value("${jwt.expired.refresh}")
	private long refreshTokenExpireTime;

}
