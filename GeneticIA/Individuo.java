import java.util.ArrayList;
import java.util.Random;

public class Individuo {
    private int x, y;
    private double fitness, probabilidade;

    public Individuo(int x, int y, double fitness) {
        this.x = x;
        this.y = y;
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getProbabilidade() {
        return probabilidade;
    }

    public void setProbabilidade(double probabilidade) {
        this.probabilidade = probabilidade;
    }

    public static Individuo criarIndividuoAleatorio() {
        Random random = new Random();

        // valor de x e y entre 0 e 7
        int x = random.nextInt(8);
        int y = random.nextInt(8);
        return new Individuo(x, y, funcaoXY(x, y));
    }

    private static double funcaoXY(int x, int y) {
        return Math.sqrt(Math.pow(x, 3) + 2 * Math.pow(y, 4));
    }

    public static ArrayList<Individuo> calcularProbabilidades(ArrayList<Individuo> populacao) {
        double somaInversoFitness = 0.0;
        final double VALOR_MINIMO_FITNESS = 0.0001; // Valor mínimo para evitar divisão por zero

        // Calcula a soma dos inversos dos fitness, com tratamento para fitness zero
        for (Individuo individuo : populacao) {
            double fitness = individuo.getFitness();
            if (fitness <= 0) {
                // Se o fitness for zero ou negativo, trata como um valor muito pequeno
                fitness = VALOR_MINIMO_FITNESS;
            }
            somaInversoFitness += 1.0 / fitness;
        }

        // Calcula a probabilidade de cada indivíduo e a define
        for (Individuo individuo : populacao) {
            double fitness = individuo.getFitness();
            if (fitness <= 0) {
                // Se o fitness for zero ou negativo, trata como um valor muito pequeno
                fitness = VALOR_MINIMO_FITNESS;
            }
            double probabilidade = (1.0 / fitness) / somaInversoFitness;
            individuo.setProbabilidade(probabilidade * 100); // Multiplique por 100 para probabilidade em percentual
                                                             // (opcional)
        }

        return populacao;
    }

    public static Individuo selecionarIndividuoPorProbabilidade(ArrayList<Individuo> populacao) {
        Random random = new Random();
        double somaProbabilidades = 0.0;

        // Soma total das probabilidades
        for (Individuo individuo : populacao) {
            somaProbabilidades += individuo.getProbabilidade();
        }

        if (somaProbabilidades <= 0) {
            // Retorna um indivíduo aleatório
            return populacao.get(random.nextInt(populacao.size()));
        }

        // Entre 0 e a soma das probabilidades
        double valorAleatorio = random.nextDouble() * somaProbabilidades;

        double somaParcial = 0.0;

        // Seleciona o indivíduo baseado na roleta proporcional
        for (Individuo individuo : populacao) {
            somaParcial += individuo.getProbabilidade();
            if (somaParcial >= valorAleatorio) {
                return individuo;
            }
        }

        // Caso não encontre (teoricamente não deveria acontecer), retorne o último
        // indivíduo
        return populacao.get(populacao.size() - 1);
    }

    public static Individuo cruzamento(Individuo pai, Individuo mae) {
        Random random = new Random();

        // Gera um ponto de corte aleatório entre 0 e 2 (para 3 bits)
        int pontoDeCorte = random.nextInt(3);

        // Obtém os cromossomos dos pais
        int xPai = pai.getX();
        int xMae = mae.getX();
        int yPai = pai.getY();
        int yMae = mae.getY();

        // Cruzamento por ponto de corte para x
        int xFilho = (xPai & ((1 << pontoDeCorte) - 1)) | (xMae & ~((1 << pontoDeCorte) - 1));

        // Cruzamento por ponto de corte para y
        int yFilho = (yPai & ((1 << pontoDeCorte) - 1)) | (yMae & ~((1 << pontoDeCorte) - 1));

        // Cria um novo indivíduo com os cromossomos cruzados
        return new Individuo(xFilho, yFilho, funcaoXY(xFilho, yFilho));
    }

    public static void mutacao(Individuo individuo, double taxaMutacao) {
        Random random = new Random();

        if (random.nextDouble() < taxaMutacao) {
            System.out.println("Mutação aplicada!");

            boolean mutarX = random.nextBoolean();

            if (mutarX) {// Aplica a mutação em x

                int x = individuo.getX();
                int bitIndex = random.nextInt(3);
                x ^= (1 << bitIndex);

                // Garante que o valor de x esteja dentro do intervalo permitido
                x = Math.max(0, Math.min(x, 7));

                individuo.setX(x);
            } else {
                int y = individuo.getY();
                int bitIndex = random.nextInt(3);
                y ^= (1 << bitIndex);

                y = Math.max(0, Math.min(y, 7));

                // Atualiza o valor mutado
                individuo.setY(y);
            }
            individuo.setFitness(funcaoXY(individuo.getX(), individuo.getY()));
        }
    }

    private void setX(int xMutado) {
        this.x = xMutado;
    }

    private void setY(int yMutado) {
        this.y = yMutado;
    }

    private void setFitness(double fitness) {
        this.fitness = fitness;
    }

}