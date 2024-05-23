package samsung.signature.authservice.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samsung.signature.authservice.global.domain.BaseTime;
import samsung.signature.authservice.member.dto.request.JoinForm;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Entity
@Table(name = "members_tbl")
//TODO: email, phonenumber index 추가
public class Member extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "username", length = 10)
	private String userName;

	@Column(name = "password", length = 255)
	private String password;

	@Email
	@Column(name = "email")
	private String email;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "public_key", length = 392)
	private String publicKey;

	public static Member of(
		final JoinForm joinForm,
		final String encodedPassword,
		final String publicKey
	) {
		return Member.builder()
				.userName(joinForm.username())
				.password(encodedPassword)
				.email(joinForm.email())
				.phoneNumber(joinForm.phoneNumber())
				.publicKey(publicKey)
				.build();
	}

}
