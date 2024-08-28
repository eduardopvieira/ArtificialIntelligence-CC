import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        AlgoritmoGenetico ag = new AlgoritmoGenetico(10, 15, 0.20);

        Individuo.calcularProbabilidades(ag.populacao);

        System.out.println("Primeira geração:");
        for (int index = 0; index < ag.populacao.size(); index++) {
            Individuo individuo = ag.populacao.get(index);
            System.out.println("Individuo " + index + " X=" + individuo.getX() + " Y=" + individuo.getY() + " fit="
                    + individuo.getFitness() + " probabilidade: " + individuo.getProbabilidade());
        }

        for (int geracao = 0; geracao < ag.maxGeracoes; geracao++) {
            ArrayList<Individuo> novaPopulacao = new ArrayList<>();

            for (int i = 0; i < ag.populacaoInicial; i++) {
                Individuo pai = Individuo.selecionarIndividuoPorProbabilidade(ag.populacao);

                Individuo mae;
                do { // Garante que a mãe seja diferente do pai
                    mae = Individuo.selecionarIndividuoPorProbabilidade(ag.populacao);
                } while (mae.equals(pai));

                Individuo filho = Individuo.cruzamento(pai, mae);
                Individuo.mutacao(filho, ag.taxaMutacao);
                novaPopulacao.add(filho);
            }

            System.out.println("Nova geração:");

            Individuo.calcularProbabilidades(novaPopulacao);

            for (int index = 0; index < novaPopulacao.size(); index++) {
                Individuo individuo = novaPopulacao.get(index);
                System.out.println("Individuo " + index + " X=" + individuo.getX() + " Y=" + individuo.getY() + " fit="
                        + individuo.getFitness() + " probabilidade: " + individuo.getProbabilidade());
            }

            // Atualiza a população com a nova geração
            ag.populacao = novaPopulacao;

            // Encontra e exibe o melhor indivíduo da geração atual
            Individuo superman = menorFitness(ag.populacao);
            System.out.println("Geração " + geracao + ": Melhor Fitness = " + superman.getFitness() + " para x = "
                    + superman.getX() + ", y = " + superman.getY());
        }
    }

    public static Individuo menorFitness(ArrayList<Individuo> populacao) {
        Individuo superman = populacao.get(0);
        for (int i = 1; i < populacao.size(); i++) {
            Individuo individuo = populacao.get(i);
            if (individuo.getFitness() < superman.getFitness()) {
                superman = individuo;
            }
        }
        return superman;
    }
}
