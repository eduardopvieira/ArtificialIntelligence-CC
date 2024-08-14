
import Classes.MLP;

public class Main {
    // Teste da rede neural
    public static void main(String[] args) {
        double[][] entradasTreinamento = {
                { 1, 0 },
                { 1, 1 },
                { 0, 0 },
                { 0, 1 }
        };

        double[][] saidasTreinamento = {
                { 1 },
                { 0 },
                { 0 },
                { 1 }
        };

        MLP rn = new MLP(2, 3, 1);
        rn.treinar(entradasTreinamento, saidasTreinamento, 0.2, 10000);

        for (double[] entrada : entradasTreinamento) {
            double[] saida = rn.propagacaoFrente(entrada);
            System.out.println("Entrada: " + entrada[0] + ", " + entrada[1] + " -> Saída prevista: " + saida[0]);
        }
    }
}
