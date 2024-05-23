package samsung.signature.authservice.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import samsung.signature.common.exception.SignatureAdvice;

@Configuration
public class SignatureConfig {
	@Bean
	public SignatureAdvice signatureAdvice() {
		return new SignatureAdvice();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
