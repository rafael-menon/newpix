package newpixserver.messages.json;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionJson {
    private Integer id;
    private Double valor_enviado;
    private UserJson usuario_enviador;
    private UserJson usuario_recebedor;
    private String criado_em;
    private String atualizado_em;

    public TransactionJson() {}

    public Double getValor_enviado() {
        return valor_enviado;
    }

    public void setValor_enviado(Double valor_enviado) {
        this.valor_enviado = valor_enviado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAtualizado_em() {
        return atualizado_em;
    }

    public void setAtualizado_em(String atualizado_em) {
        this.atualizado_em = atualizado_em;
    }

    public String getCriado_em() {
        return criado_em;
    }

    public void setCriado_em(String criado_em) {
        this.criado_em = criado_em;
    }

    public UserJson getUsuario_recebedor() {
        return usuario_recebedor;
    }

    public void setUsuario_recebedor(UserJson usuario_recebedor) {
        this.usuario_recebedor = usuario_recebedor;
    }

    public UserJson getUsuario_enviador() {
        return usuario_enviador;
    }

    public void setUsuario_enviador(UserJson usuario_enviador) {
        this.usuario_enviador = usuario_enviador;
    }
}
