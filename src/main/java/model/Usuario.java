package model;

public class Usuario extends Pessoa {

    public Usuario() {
        super();
        this.setRole("Usuário");
    }

    public Usuario(String nome, String login, String senha) {
        super();
        this.setNome(nome);
        this.setLogin(login);
        this.setSenha(senha);
        this.setRole("Usuário");
    }
}