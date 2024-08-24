import java.util.ArrayList;
import java.util.Random;

public class AlgoritmoGenetico {
    public int populacaoInicial;
    private int geneTamanho = 6; // 3 bits de x e 3 de y
    public int maxGeracoes;
    public double taxaMutacao;

    public ArrayList<Individuo> populacao = new ArrayList<>();

    public AlgoritmoGenetico(int populacaoInicial, int maxGeracoes, double taxaMutacao) {
        this.populacaoInicial = populacaoInicial;
        this.maxGeracoes = maxGeracoes;
        this.taxaMutacao = taxaMutacao;

        for (int i = 0; i < populacaoInicial; i++) {
            populacao.add(Individuo.criarIndividuoAleatorio());
        }

    }
}