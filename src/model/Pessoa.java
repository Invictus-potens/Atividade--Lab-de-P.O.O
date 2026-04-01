package model;

public abstract class Pessoa {

    private String nome;
    private String login;
    private String senha;

    public Pessoa() {}

    public Pessoa(String nome, String login, String senha) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public abstract String getTipo();

    public boolean autenticar(String login, String senha) {
        return this.login != null && this.login.equals(login)
                && this.senha != null && this.senha.equals(senha);
    }

    @Override
    public String toString() {
        return nome + " (" + getTipo() + ")";
    }
}