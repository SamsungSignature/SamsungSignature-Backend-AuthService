package samsung.signature.authservice.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import samsung.signature.authservice.member.domain.Member;

@Builder
public record NewbieInfo(
	@JsonProperty("email")
	String email,
	@JsonProperty("phone_number")
	String phoneNumber,
	@JsonProperty("username")
	String username
) {
	public static NewbieInfo from(Member newbie) {
		return NewbieInfo.builder()
			.email(newbie.getEmail())
			.phoneNumber(newbie.getPhoneNumber())
			.username(newbie.getUserName())
			.build()
			;
	}
}
