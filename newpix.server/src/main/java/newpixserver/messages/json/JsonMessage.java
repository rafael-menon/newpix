package newpixserver.messages.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonMessage {
    private String operacao;
    private String operacao_enviada;
    private Boolean status;
    private String token;
    private String info;
    private List<TransactionJson> transacoes;
    private UserJson usuario;

    private JsonMessage(Builder builder) {
        this.operacao = builder.operacao;
        this.operacao_enviada = builder.operacao_enviada;
        this.status = builder.status;
        this.token = builder.token;
        this.info = builder.info;
        this.transacoes = builder.transacoes;
        this.usuario = builder.usuario;
    }

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

    public String getOperacaoEnviada() { return operacao_enviada; }

    public void setOperacaoEnviada() { this.operacao_enviada = operacao_enviada; }

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

    public List<TransactionJson> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<TransactionJson> transacoes) {
        this.transacoes = transacoes;
    }

    public UserJson getUsuario() {
		return usuario;
	}

	public void setUsuario(UserJson usuario) {
		this.usuario = usuario;
	}

    public static class Builder {
        private String operacao;
        private String operacao_enviada;
        private Boolean status;
        private String token;
        private String info;
        private List<TransactionJson> transacoes;
        private UserJson usuario;

        public Builder(String operacao, Boolean status) {
            this.operacao = operacao;
            this.status = status;
        }

        public Builder info(String info) {
            this.info = info;
            return this;
        }

        public Builder withToken(String token) {
            this.token = token;
            return this;
        }

        public Builder withUser(UserJson usuario) {
            this.usuario = usuario;
            return this;
        }

        public Builder withTransactions(List<TransactionJson> transacoes) {
            this.transacoes = transacoes;
            return this;
        }

        public Builder withSentOperation(String operacaoEnviada) {
            this.operacao_enviada = operacaoEnviada;
            return this;
        }

        public JsonMessage build() {
            return new JsonMessage(this);
        }
    }
}
