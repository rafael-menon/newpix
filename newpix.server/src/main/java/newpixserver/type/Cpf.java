package newpixserver.type;

public class Cpf {
	private String cpf;
	
	public Cpf(String cpf) {
		verifyCpf(cpf);
		this.cpf = cpf;
	}
	
	private void verifyCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser vazio.");
        }

		if(cpf.length() != 14)
			throw new IllegalArgumentException("CPF inválido."); 
		
		int count = 0;
		char[] charsCpf = cpf.toCharArray();
		for(char charCpf : charsCpf) {
			if(count != 3 && count != 7 && count != 11 && (!Character.isDigit(charCpf))) {
				throw new IllegalArgumentException("CPF inválido."); 
			}
			
			if(((count == 3 || count == 7) && charsCpf[count] != '.') || (count == 11 && charsCpf[11] != '-')) {
				throw new IllegalArgumentException("CPF inválido."); 
			}
			
			count++;
		}
	}
	
	public String getString() {
		return cpf;
	}
}
