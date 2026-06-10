package model;

public class Usuario extends Pessoa {

    public Usuario() {
        super();
    }

    public Usuario(String nome, String login, String senha) {
        super(nome, login, senha);
    }

    @Override
    public String getTipo() {
        return "Usuário";
    }
}