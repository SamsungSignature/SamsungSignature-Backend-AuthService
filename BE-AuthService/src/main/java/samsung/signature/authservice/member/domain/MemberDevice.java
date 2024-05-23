package samsung.signature.authservice.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import samsung.signature.authservice.global.domain.BaseTime;
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "member_devices_tbl")
public class MemberDevice extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "device_id")
	private String deviceId;

	@JoinColumn(name = "member_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Member member;
}
