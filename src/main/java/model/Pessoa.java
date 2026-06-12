package model;

public abstract class Pessoa {

    private int id;
    private String nome;
    private String login;
    private String senha;
    private String role;
    
    public int getId() {
        return id; }
    public void setId(int id) { 
        this.id = id; }

    public String getRole() {
        return role; }
    public void setRole(String role) { 
        this.role = role; }


    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}