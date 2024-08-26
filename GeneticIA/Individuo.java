import java.util.ArrayList;
import java.util.Random;

public class Individuo {
    private int x;
    private int y;
    private double fitness;

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

    // Seleção dos melhores indivíduos aleatórios para cruzamento
    public static Individuo selecionarIndividuo(ArrayList<Individuo> populacao) {
        Random random = new Random();
        Individuo individuo1;
        Individuo individuo2;

        // Seleciona o primeiro indivíduo aleatoriamente
        individuo1 = populacao.get(random.nextInt(populacao.size()));
        // System.out.println("Individuo 1: " + individuo1.getX() + " " + individuo1.getY());

        // Seleciona o segundo indivíduo aleatoriamente, garantindo que seja diferente do primeiro
        do {
            individuo2 = populacao.get(random.nextInt(populacao.size()));
        } while (individuo2.equals(individuo1));

        // System.out.println("Individuo 2: " + individuo2.getX() + " " + individuo2.getY());

        // Retorna o melhor indivíduo com base no fitness
        return (individuo1.getFitness() < individuo2.getFitness()) ? individuo1 : individuo2;
    }

    public static Individuo cruzamento(Individuo pai, Individuo mae) {
        Random random = new Random();
        int pontoCorte = random.nextInt(3) + 1; // Ponto de corte entre 1 e 3

        int xFilho = (pai.getX() & (7 << pontoCorte)) | (mae.getX() & ~(7 << pontoCorte));
        int yFilho = (pai.getY() & (7 << pontoCorte)) | (mae.getY() & ~(7 << pontoCorte));

        return new Individuo(xFilho, yFilho, funcaoXY(xFilho, yFilho));
    }

    // Aplica a mutação em um indivíduo
    public static void mutacao(Individuo individuo, double taxaMutacao) {
        Random random = new Random();

        // Verifica se a mutação deve ser aplicada
        if (random.nextDouble() < taxaMutacao) {
            System.out.println("Mutação aplicada!");

            // Aplica a mutação em x e y (bit flip)
            int xMutado = individuo.getX() ^ (1 << random.nextInt(3)); // Muta um dos bits de x
            int yMutado = individuo.getY() ^ (1 << random.nextInt(3)); // Muta um dos bits de y

            // Atualiza os valores mutados
            individuo.setX(xMutado);
            individuo.setY(yMutado);
            individuo.setFitness(funcaoXY(xMutado, yMutado));
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