package service;

import model.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Aqui roda toda a logica do sistema onde o user Gerencia clubes, campeonatos, partidas, grupos e apostas.
 */
public class SistemaGerenciador {

    private static final int MAX_GRUPOS = 5;
    private static SistemaGerenciador instance;

    private final List<Pessoa> pessoas;
    private final List<Clube> clubes;
    private final List<Campeonato> campeonatos;
    private final List<Partida> partidas;
    private final List<GrupoAposta> grupos;
    private final List<Aposta> apostas;
    private Pessoa pessoaLogada;

    private SistemaGerenciador() {
        pessoas = new ArrayList<>();
        clubes = new ArrayList<>();
        campeonatos = new ArrayList<>();
        partidas = new ArrayList<>();
        grupos = new ArrayList<>();
        apostas = new ArrayList<>();

        // Administrador padrão do sistema
        pessoas.add(new Administrador("Administrador", "admin", "admin123"));
    }

    public static SistemaGerenciador getInstance() {
        if (instance == null) {
            instance = new SistemaGerenciador();
        }
        return instance;
    }

    // =========================================================================
    // AUTENTICAÇÃO
    // =========================================================================

    public Pessoa login(String login, String senha) {
        return pessoas.stream()
                .filter(p -> p.autenticar(login, senha))
                .findFirst()
                .orElse(null);
    }

    public void logout() {
        pessoaLogada = null;
    }

    public void cadastrarUsuario(String nome, String login, String senha) throws Exception {
        if (nome == null || nome.isBlank()) throw new Exception("Nome não pode ser vazio!");
        if (login == null || login.isBlank()) throw new Exception("Login não pode ser vazio!");
        if (senha == null || senha.isBlank()) throw new Exception("Senha não pode ser vazia!");

        boolean loginExistente = pessoas.stream()
                .anyMatch(p -> p.getLogin().equalsIgnoreCase(login));
        if (loginExistente) throw new Exception("Login '" + login + "' já está em uso!");

        pessoas.add(new Usuario(nome, login, senha));
    }

    // =========================================================================
    // CLUBES
    // =========================================================================

    public void cadastrarClube(String nome, String cidade) throws Exception {
        if (nome == null || nome.isBlank()) throw new Exception("Nome do clube não pode ser vazio!");
        if (cidade == null || cidade.isBlank()) throw new Exception("Cidade não pode ser vazia!");

        boolean jaExiste = clubes.stream()
                .anyMatch(c -> c.getNome().equalsIgnoreCase(nome));
        if (jaExiste) throw new Exception("Clube '" + nome + "' já está cadastrado!");

        clubes.add(new Clube(nome, cidade));
    }

    public List<Clube> getClubes() {
        return Collections.unmodifiableList(clubes);
    }

    // =========================================================================
    // CAMPEONATOS
    // =========================================================================

    public void cadastrarCampeonato(String nome) throws Exception {
        if (nome == null || nome.isBlank()) throw new Exception("Nome do campeonato não pode ser vazio!");

        boolean jaExiste = campeonatos.stream()
                .anyMatch(c -> c.getNome().equalsIgnoreCase(nome));
        if (jaExiste) throw new Exception("Campeonato '" + nome + "' já está cadastrado!");

        campeonatos.add(new Campeonato(nome));
    }

    public void adicionarClubeAoCampeonato(Campeonato campeonato, Clube clube) throws Exception {
        boolean jaAdicionado = campeonato.listar().contains(clube);
        if (jaAdicionado) {
            throw new Exception("O clube '" + clube.getNome() + "' já está neste campeonato!");
        }
        campeonato.adicionar(clube);
    }

    public List<Campeonato> getCampeonatos() {
        return Collections.unmodifiableList(campeonatos);
    }

    // =========================================================================
    // PARTIDAS
    // =========================================================================

    public void cadastrarPartida(Campeonato campeonato, Clube mandante, Clube visitante,
                                  LocalDateTime dataHora) throws Exception {
        if (campeonato == null) throw new Exception("Selecione um campeonato!");
        if (mandante == null || visitante == null) throw new Exception("Selecione os clubes!");
        if (mandante.equals(visitante)) throw new Exception("Os clubes mandante e visitante devem ser diferentes!");
        if (dataHora == null) throw new Exception("Informe a data e hora da partida!");

        if (!campeonato.listar().contains(mandante))
            throw new Exception("Clube mandante não pertence a este campeonato!");
        if (!campeonato.listar().contains(visitante))
            throw new Exception("Clube visitante não pertence a este campeonato!");

        partidas.add(new Partida(mandante, visitante, dataHora, campeonato));
    }

    public List<Partida> getPartidas() {
        return Collections.unmodifiableList(partidas);
    }

    public List<Partida> getPartidasSemResultado() {
        return partidas.stream()
                .filter(p -> !p.temResultado())
                .collect(Collectors.toList());
    }

    public List<Partida> getPartidasDisponiveisParaAposta() {
        return partidas.stream()
                .filter(p -> !p.temResultado() && p.podeApostar())
                .collect(Collectors.toList());
    }

    // =========================================================================
    // GRUPOS DE APOSTA
    // =========================================================================

    public void criarGrupo(String nome, Usuario criador) throws Exception {
        if (nome == null || nome.isBlank()) throw new Exception("Nome do grupo não pode ser vazio!");
        if (grupos.size() >= MAX_GRUPOS)
            throw new Exception("Limite de " + MAX_GRUPOS + " grupos atingido!");

        boolean jaExiste = grupos.stream()
                .anyMatch(g -> g.getNome().equalsIgnoreCase(nome));
        if (jaExiste) throw new Exception("Já existe um grupo chamado '" + nome + "'!");

        grupos.add(new GrupoAposta(nome, criador));
    }

    public void entrarNoGrupo(GrupoAposta grupo, Usuario usuario) throws Exception {
        grupo.adicionar(usuario);
    }

    public List<GrupoAposta> getGrupos() {
        return Collections.unmodifiableList(grupos);
    }

    public List<GrupoAposta> getGruposDoUsuario(Usuario usuario) {
        return grupos.stream()
                .filter(g -> g.contemParticipante(usuario))
                .collect(Collectors.toList());
    }

    // =========================================================================
    // APOSTAS
    // =========================================================================

    public void registrarAposta(Usuario usuario, Partida partida, GrupoAposta grupo,
                                 int golsMandante, int golsVisitante) throws Exception {
        if (usuario == null) throw new Exception("Usuário inválido!");
        if (partida == null) throw new Exception("Selecione uma partida!");
        if (grupo == null) throw new Exception("Selecione um grupo!");

        if (!grupo.contemParticipante(usuario))
            throw new Exception("Você não é participante do grupo '" + grupo.getNome() + "'!");

        if (partida.temResultado())
            throw new Exception("Esta partida já possui resultado e não aceita apostas!");

        if (!partida.podeApostar())
            throw new Exception("Não é possível apostar! O prazo encerrou 20 minutos antes do início da partida.");

        boolean jaApostou = apostas.stream()
                .anyMatch(a -> a.getUsuario().equals(usuario)
                        && a.getPartida().equals(partida)
                        && a.getGrupo().equals(grupo));
        if (jaApostou)
            throw new Exception("Você já registrou uma aposta nesta partida para o grupo '" + grupo.getNome() + "'!");

        if (golsMandante < 0 || golsVisitante < 0)
            throw new Exception("Placar não pode ser negativo!");

        apostas.add(new Aposta(usuario, partida, grupo, golsMandante, golsVisitante));
    }

    public List<Aposta> getApostasDoUsuario(Usuario usuario) {
        return apostas.stream()
                .filter(a -> a.getUsuario().equals(usuario))
                .collect(Collectors.toList());
    }

    public List<Aposta> getApostasDaPartida(Partida partida) {
        return apostas.stream()
                .filter(a -> a.getPartida().equals(partida))
                .collect(Collectors.toList());
    }

    // =========================================================================
    // RESULTADOS
    // =========================================================================

    public int registrarResultado(Partida partida, int golsMandante, int golsVisitante) throws Exception {
        if (partida == null) throw new Exception("Selecione uma partida!");
        if (partida.temResultado()) throw new Exception("Esta partida já possui resultado registrado!");
        if (golsMandante < 0 || golsVisitante < 0) throw new Exception("Placar não pode ser negativo!");

        ResultadoPartida resultado = new ResultadoPartida(golsMandante, golsVisitante);
        partida.setResultado(resultado);

        // Calcular pontos para todas as apostas desta partida
        long totalApostasCalculadas = apostas.stream()
                .filter(a -> a.getPartida().equals(partida))
                .peek(a -> a.calcularPontos(resultado))
                .count();

        return (int) totalApostasCalculadas;
    }

    // =========================================================================
    // CLASSIFICAÇÃO
    // =========================================================================

    public List<Map.Entry<Usuario, Integer>> getClassificacaoGrupo(GrupoAposta grupo) {
        Map<Usuario, Integer> pontosPorUsuario = new LinkedHashMap<>();

        for (Usuario u : grupo.listar()) {
            int total = apostas.stream()
                    .filter(a -> a.getUsuario().equals(u)
                            && a.getGrupo().equals(grupo)
                            && a.isCalculada())
                    .mapToInt(Aposta::getPontos)
                    .sum();
            pontosPorUsuario.put(u, total);
        }

        List<Map.Entry<Usuario, Integer>> classificacao = new ArrayList<>(pontosPorUsuario.entrySet());
        classificacao.sort((a, b) -> b.getValue() - a.getValue());
        return classificacao;
    }

    // =========================================================================
    // UTILITÁRIOS
    // =========================================================================

    public Pessoa getPessoaLogada() { return pessoaLogada; }
    public void setPessoaLogada(Pessoa pessoaLogada) { this.pessoaLogada = pessoaLogada; }

    public boolean isAdmin() {
        return pessoaLogada instanceof Administrador;
    }
}
