package newpixclient.type;

public class Password {
	private String password;
	
	public Password(char[] passwordChar) throws IllegalArgumentException {
		String passwordString = new String(passwordChar);
		this.password = checkLength(passwordString);
	}
	
	public Password(char[] passwordChar, String operation) throws IllegalArgumentException {	
		String passwordString = new String(passwordChar);
		
	    if(operation.equals("update")) {
	    	this.password = checkPasswordUpdate(passwordString);
		} else {
			return;
		}
	}
	
	private String checkPasswordUpdate(String password) {
		if(getLength(password) == 0) {
			System.out.println("Password length = 0");
			return null;
		} 
		
		System.out.println("Password length > 0");
		return checkLength(password);	
	}
	
	private String checkLength(String password) {
		if(getLength(password) < 6) {
			System.out.println("Password length < 6");
			throw new IllegalArgumentException("A senha deve ter no mínimo 6 caracteres.");	
		}
		
		if(getLength(password) > 120) {
			System.out.println("Password length > 6");
			throw new IllegalArgumentException("A senha deve ter no máximo 120 caracteres.");
		}
		
		return password;
	}
	
	
	private Integer getLength(String password) {
		if(password == null) {
			return 0;
		}
		
		char[] digits = password.toCharArray();
			
		int count = 0;
		for(char digit : digits) {
			if(digit == ' ') {
				System.out.println("Invalid password: space in password");
				throw new IllegalArgumentException("Caractere inválido utilizado.");
			} 
			count++;
		}
		
		return count;
	}
	
	public Integer getLength() {
		return getLength(this.password);
	}
	
	public String getString() {
		return password;
	}
}
