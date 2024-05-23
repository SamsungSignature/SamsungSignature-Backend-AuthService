package samsung.signature.authservice.member.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record TokenInfo(
	@JsonProperty("access_token")
	String accessToken,
	@JsonProperty("created_at")
	long createdAt,
	@JsonProperty("expires_in")
	long accessExpiresIn,
	@JsonIgnore
	long refreshExpiresIn,
	@JsonIgnore
	String refreshToken
) {
}
