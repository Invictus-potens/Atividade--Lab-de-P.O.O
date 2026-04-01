package interfaces;

import java.util.List;

/**
 * Interface genérica para coleções com limite máximo de elementos.
 * Demonstra o uso de generics e contrato de gerenciamento.
 *
 * @param <T> Tipo dos elementos gerenciados
 */
public interface Gerenciavel<T> {

    /**
     * Adiciona um elemento à coleção, respeitando o limite máximo.
     * @throws Exception se o limite for atingido ou o elemento for duplicado
     */
    void adicionar(T item) throws Exception;

    /**
     * Retorna a lista imutável de elementos.
     */
    List<T> listar();

    /**
     * Retorna o tamanho máximo permitido para esta coleção.
     */
    int getTamanhoMaximo();
}
