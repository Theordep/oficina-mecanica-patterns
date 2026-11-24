package br.com.oficina.composite;

public interface ItemOrdemServico {
    String getDescricao();
    double calcularValor();
    String exibir(int nivel);
}
