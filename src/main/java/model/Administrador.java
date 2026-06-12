package model;

public class Administrador extends Pessoa {

    public Administrador() {
        super();
        this.setRole("Administrador");
    }

    public Administrador(String nome, String login, String senha) {
        super();
        this.setNome(nome);
        this.setLogin(login);
        this.setSenha(senha);
        this.setRole("Administrador");
    }
}