package model;

public class Administrador extends Pessoa {

    public Administrador() {
        super();
    }

    public Administrador(String nome, String login, String senha) {
        super(nome, login, senha);
    }

    @Override
    public String getTipo() {
        return "Administrador";
    }
}