package tech.outspace.papershare.utils.encrypt;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PassEncoder {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static BCryptPasswordEncoder instance() {
        return encoder;
    }

    public static String genSalt() {
        return BCrypt.gensalt();
    }


}
