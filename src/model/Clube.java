package model;

public class Clube {

    private String nome;
    private String cidade;

    public Clube() {}

    public Clube(String nome, String cidade) {
        this.nome = nome;
        this.cidade = cidade;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    @Override
    public String toString() {
        return nome + " (" + cidade + ")";
    }
}