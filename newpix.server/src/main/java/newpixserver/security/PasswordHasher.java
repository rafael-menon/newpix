package newpixserver.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
	static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
    public static String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
    
    public static boolean checkPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }
}
