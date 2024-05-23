package samsung.signature.authservice.privatekey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import samsung.signature.authservice.privatekey.domain.PrivateKey;

@Repository
public interface PrivateKeyRepository extends JpaRepository<PrivateKey, Long> {
}
