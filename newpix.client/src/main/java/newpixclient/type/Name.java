package newpixclient.type;

public class Name {
	private String name = null;
	
	public Name(String name) throws IllegalArgumentException {
		checkLength(name);
		this.name = name;
	}
	
	public Name(String name, String operation) throws IllegalArgumentException {
		if(operation.equals("update")) {
			this.name = checkNameUpdate(name);	
		} else {
			return;
		}
	}
	
	private String checkNameUpdate(String name) {
		if(getLength(name) == 0) {
			System.out.println("Name length = 0");
			return null;
		} 
	
		System.out.println("Name length > 0");
		return checkLength(name);	
	}
	
	private String checkLength(String name) {

		if(getLength(name) < 6) {
			System.out.println("Name length < 6");
			throw new IllegalArgumentException("O nome deve ter no mínimo 6 caracteres.");	
		}
		
		if(getLength(name) > 120) {
			System.out.println("Name length > 120");
			throw new IllegalArgumentException("O nome deve ter no máximo 120 caracteres.");
		}
			
		return name;
	}
	
	private void validateName(char[] characters) {
		int length = characters.length;
		
		if(characters[0] == ' ' || characters[length - 1] == ' ') {
			System.out.println("Invalid username: ending or starting username  with a space");
			throw new IllegalArgumentException("Nome de usuário inválido.");
		}
		
		for(int i = 0; i < length; i++) {
			//System.out.println(characters[i]);
			
			if(i == characters[length - 1]) {
				break;
			}
			
			if(characters[i] == ' ' && characters[i + 1] == ' ') {
				System.out.println("Invalid username: double spaces");
				throw new IllegalArgumentException("Nome de usuário inválido.");
			}
		}
	}

	private Integer getLength(String name) {
		if(name == null) {
			return 0;
		}
		
		char[] characters = name.toCharArray();
			
		int count = 0;
		for(char character : characters) {
			if(character != ' ') {
				count++;
			} 
		}
		
		if(count > 0) {
			validateName(characters);
		}

		return count;
	}

	public Integer getLength() {
		return getLength(this.name);
	}
	
	public String getString() {
		return name;
	}
}
