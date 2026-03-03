package newpixserver.type;

public class Password {
	private String password;
	
    public Password(String password) {
    	verifyPassword(password);
    	this.password = password;
    }
    
    private void verifyPassword(String password) {
    	if(password.length() < 6) 
    		throw new IllegalArgumentException("A senha deve ter mais de 6 caracteres");
    	
    	if(password.length() > 120) 
    		throw new IllegalArgumentException("A senha deve ter menos de 120 caracteres");
    }
    
    public String getString() {
    	return password;
    }
}
