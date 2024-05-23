package samsung.signature.authservice.encryption.rsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.SneakyThrows;

@Component
public class PsaProvider {
	private static final int KEY_SIZE = 2048;
	private final SecureRandom secureRandom;
	private final KeyPairGenerator keyPairGenerator;

	@SneakyThrows
	public PsaProvider() {
		this.secureRandom = new SecureRandom();
		this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");
	}

	public Map<String, String> createAsyKeyPair() {
		Map<String, String> asyKeyPair = new HashMap<>();
		keyPairGenerator.initialize(KEY_SIZE, secureRandom);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		asyKeyPair.put("publicKey", Base64.getEncoder().encodeToString(publicKey.getEncoded()));
		asyKeyPair.put("privateKey", Base64.getEncoder().encodeToString(privateKey.getEncoded()));

		return asyKeyPair;
	}
}
