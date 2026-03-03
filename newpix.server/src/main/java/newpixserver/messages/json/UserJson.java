package newpixserver.messages.json;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserJson {
	private String token;
	private String cpf;
	private String nome;	
	private String senha;
	private Double saldo;

	public UserJson(String cpf, Double saldo, String nome) {
        this.cpf = cpf;
        this.saldo = saldo;
        this.nome = nome;
    }

    public UserJson(String nome, String cpf) {
        this.cpf = cpf;
        this.nome = nome;
    }

    public UserJson() {}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

}

