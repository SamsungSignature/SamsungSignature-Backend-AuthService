package samsung.signature.authservice.member.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import samsung.signature.authservice.member.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	boolean existsByPhoneNumber(String phoneNumber);
	boolean existsByEmail(String email);
	Optional<Member> findByEmail(String email);
	Optional<Member> findByPhoneNumber(String phoneNumber);

}
