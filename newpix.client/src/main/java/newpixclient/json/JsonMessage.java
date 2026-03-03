package newpixclient.json;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonMessage {
	private String operacao;
	private Boolean status;
    private String operacao_enviada;
	private String info;
	private String nome;
	private String cpf;
	private String senha;
	private Double saldo;
	private String token;
    private String cpf_destino;
    private Double valor_enviado;
    private Double valor;
    private String data_inicial;
    private String data_final;
	private UserJson usuario;
	
	public JsonMessage() {}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public Boolean getStatus() {
		return status;
	}

	public String getData_inicial() {
		return data_inicial;
	}

	public void setData_inicial(String data_inicial) {
		this.data_inicial = data_inicial;
	}

	public String getData_final() {
		return data_final;
	}

	public void setData_final(String data_final) {
		this.data_final = data_final;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public Double getSaldo() {
		return saldo;
	}
	
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserJson getUsuario() {
		return usuario;
	}

	public void setUsuario(UserJson usuario) {
		this.usuario = usuario;
	}

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getCpf_destino() {
        return cpf_destino;
    }

    public void setCpf_destino(String cpf_destino) {
        this.cpf_destino = cpf_destino;
    }

    public Double getValor_enviado() {
        return valor_enviado;
    }

    public void setValor_enviado(Double valor_enviado) {
        this.valor_enviado = valor_enviado;
    }

    public String getOperacao_enviada() {
        return operacao_enviada;
    }

    public void setOperacao_enviada(String operacao_enviada) {
        this.operacao_enviada = operacao_enviada;
    }
}
