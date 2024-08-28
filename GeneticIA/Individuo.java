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

        for (Individuo individuo : populacao) {
            double fitness = individuo.getFitness();
            if (fitness <= 0) {
                // Se o fitness for zero ou negativo, trata como um valor muito pequeno
                fitness = VALOR_MINIMO_FITNESS;
            }
            double probabilidade = (1.0 / fitness) / somaInversoFitness;
            individuo.setProbabilidade(probabilidade * 100); // em percentual
        }

        return populacao;
    }

    public static Individuo selecionarIndividuoPorProbabilidade(ArrayList<Individuo> populacao) {
        Random random = new Random();
        double somaProbabilidades = 0.0;

        for (Individuo individuo : populacao) {
            somaProbabilidades += individuo.getProbabilidade();
        }

        if (somaProbabilidades <= 0) {
            // Retorna um qualquer
            return populacao.get(random.nextInt(populacao.size()));
        }

        // Entre 0 e a soma das probabilidades
        double valorAleatorio = random.nextDouble() * somaProbabilidades;

        double somaParcial = 0.0;

        // Seleciona o indivíduo baseado na roleta de probabilidades
        for (Individuo individuo : populacao) {
            somaParcial += individuo.getProbabilidade();
            if (somaParcial >= valorAleatorio) {
                return individuo;
            }
        }
        // Isso aumenta a chance de retornar os individos que tem probabilidade maior

        // Exception do acaso, aleatório
        return populacao.get(random.nextInt(populacao.size()));
    }

    public static Individuo cruzamento(Individuo pai, Individuo mae) {
        Random random = new Random();

        // Gera um ponto de corte aleatório entre 1 a 2 (garantir que usa bits dos dois
        // pais)
        int pontoDeCorte = random.nextInt(2) + 1;

        // Cromossomos dos pais
        int xPai = pai.getX();
        int xMae = mae.getX();
        int yPai = pai.getY();
        int yMae = mae.getY();

        int mascaraPontoDeCorte = (1 << pontoDeCorte) - 1; // Ou a máscara é 001 ou 110

        int parteXDoPai = xPai & mascaraPontoDeCorte;
        int parteYDoPai = yPai & mascaraPontoDeCorte;

        int parteXDaMae = xMae & ~mascaraPontoDeCorte;
        int parteYDaMae = yMae & ~mascaraPontoDeCorte;

        int xFilho = parteXDoPai | parteXDaMae;
        int yFilho = parteYDoPai | parteYDaMae;

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

                // 001 010 100 com Xor modifica um de alguma posição

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