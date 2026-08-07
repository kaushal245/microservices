package Strategy;

import com.authencation_service.OtpPayload;
import com.authencation_service.immutable.OtpType;

public interface OtpGenerator {
	 OtpType supports();
	 OtpPayload generate(String recipient);
}
