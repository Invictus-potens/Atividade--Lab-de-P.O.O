package model;

import interfaces.Gerenciavel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Campeonato implements Gerenciavel<Clube> {

    private static final int MAX_CLUBES = 8;

    private String nome;
    private List<Clube> clubes;

    public Campeonato() {
        this.clubes = new ArrayList<>();
    }

    public Campeonato(String nome) {
        this();
        this.nome = nome;
    }

    @Override
    public void adicionar(Clube clube) throws Exception {
        if (clubes.size() >= MAX_CLUBES) {
            throw new Exception("O campeonato '" + nome + "' já atingiu o limite de " + MAX_CLUBES + " clubes!");
        }
        clubes.add(clube);
    }

    @Override
    public List<Clube> listar() {
        return Collections.unmodifiableList(clubes);
    }

    @Override
    public int getTamanhoMaximo() {
        return MAX_CLUBES;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    @Override
    public String toString() {
        return nome + " (" + clubes.size() + "/" + MAX_CLUBES + " clubes)";
    }
}