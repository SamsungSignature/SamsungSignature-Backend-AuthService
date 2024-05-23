package samsung.signature.authservice.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginForm(
	@JsonProperty("id")
	String id,

	@JsonProperty("password")
	String password
) {
}
