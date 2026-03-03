package newpixserver.type;

public class Name {
	private String name;
	
    public Name(String name) {
    	verifyName(name);
    	this.name = name;
    }
    
    private void verifyName(String name) {
    	if(name.length() < 6)
    		throw new IllegalArgumentException("O nome deve ter mais de 6 caracteres");
    	
    	if(name.length() > 120)
    		throw new IllegalArgumentException("O nome deve ter menos de 120 caracteres");
    }
    
    public String getString() {
    	return name;
    }
}
