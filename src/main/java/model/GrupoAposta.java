package model;

import interfaces.Gerenciavel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrupoAposta implements Gerenciavel<Usuario> {

    private static final int MAX_PARTICIPANTES = 5;

    private String nome;
    private Usuario criador;
    private List<Usuario> participantes;

    public GrupoAposta() {
        this.participantes = new ArrayList<>();
    }

    public GrupoAposta(String nome, Usuario criador) {
        this();
        this.nome = nome;
        this.criador = criador;
        participantes.add(criador);
    }

    @Override
    public void adicionar(Usuario usuario) throws Exception {
        if (participantes.size() >= MAX_PARTICIPANTES) {
            throw new Exception("O grupo '" + nome + "' já atingiu o limite de " + MAX_PARTICIPANTES + " participantes!");
        }
        if (participantes.contains(usuario)) {
            throw new Exception("O usuário '" + usuario.getNome() + "' já é participante deste grupo!");
        }
        participantes.add(usuario);
    }

    @Override
    public List<Usuario> listar() {
        return Collections.unmodifiableList(participantes);
    }

    @Override
    public int getTamanhoMaximo() {
        return MAX_PARTICIPANTES;
    }

    public boolean contemParticipante(Usuario usuario) {
        return participantes.contains(usuario);
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Usuario getCriador() { return criador; }
    public void setCriador(Usuario criador) { this.criador = criador; }

    @Override
    public String toString() {
        return nome + " (" + participantes.size() + "/" + MAX_PARTICIPANTES + " membros)";
    }
}