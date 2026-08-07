package Strategy;

import com.authencation_service.TokenClaims;

public interface TokenStrategy {
	  String generate(TokenClaims claims);
	    boolean validate(String token);
	    String extractSubject(String token);
}
