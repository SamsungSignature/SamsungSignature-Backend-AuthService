package samsung.signature.authservice.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JoinForm(

	@JsonProperty("email")
	String email,

	@JsonProperty("password")
	String password,

	@JsonProperty("phone_number")
	String phoneNumber,

	@JsonProperty("username")
	String username
) {
}
