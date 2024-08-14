package Classes;

import java.util.Random;

public class MLP {
    private double[][] pesosEntradaOculta; // * Matriz de pesos entre a camada de entrada e a camada oculta
    private double[][] pesosOcultaSaida; // * Matriz de pesos entre a camada oculta e a camada de saída
    private double[] biasOculta; // * Vetor de bias para os neurônios da camada oculta
    private double[] biasSaida; // * Vetor de bias para os neurônios da camada de saída
    private double[] camadaOculta; // * Vetor que armazena as ativações dos neurônios da camada oculta
    private double[] camadaSaida; // * Vetor que armazena as ativações dos neurônios da camada de saída

    // ! Função de ativação sigmoid, usada para normalizar as entradas entre 0 e 1
    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    // ! Derivada da função sigmoid, usada para calcular os ajustes dos pesos
    // durante
    // ! a retropropagação
    private double derivadaSigmoid(double x) {
        return x * (1 - x);
    }

    // ! Construtor da classe RedeNeural, que inicializa pesos, bias e outras
    // ! variáveis
    public MLP(int tamanhoEntrada, int tamanhoOculta, int tamanhoSaida) {
        Random rand = new Random();

        // Inicializa a matriz de pesos entre a camada de entrada e a camada oculta
        pesosEntradaOculta = new double[tamanhoEntrada][tamanhoOculta];

        // Inicializa a matriz de pesos entre a camada oculta e a camada de saída
        pesosOcultaSaida = new double[tamanhoOculta][tamanhoSaida];

        // Inicializa o vetor de bias para a camada oculta
        biasOculta = new double[tamanhoOculta];

        // Inicializa o vetor de bias para a camada de saída
        biasSaida = new double[tamanhoSaida];

        // ! Atribui valores aleatórios aos pesos entre a camada de entrada e a camada
        // ! oculta
        for (int i = 0; i < tamanhoEntrada; i++) {
            for (int j = 0; j < tamanhoOculta; j++) {
                pesosEntradaOculta[i][j] = rand.nextDouble() * 2 - 1; // Valores entre -1 e 1
            }
        }

        // ! Atribui valores aleatórios aos pesos entre a camada oculta e a camada de
        // ! saída
        for (int i = 0; i < tamanhoOculta; i++) {
            for (int j = 0; j < tamanhoSaida; j++) {
                pesosOcultaSaida[i][j] = rand.nextDouble() * 2 - 1; // Valores entre -1 e 1
            }
            biasOculta[i] = rand.nextDouble() * 2 - 1; // Atribui valores aleatórios aos bias da camada oculta
        }

        // ! Atribui valores aleatórios aos bias da camada de saída
        for (int i = 0; i < tamanhoSaida; i++) {
            biasSaida[i] = rand.nextDouble() * 2 - 1; // Valores entre -1 e 1
        }
    }

    // ! Método para a propagação para frente (ou feedforward pros mais íntimos)
    public double[] propagacaoFrente(double[] entradas) {
        camadaOculta = new double[biasOculta.length]; // * Inicializa o vetor de ativação da camada oculta
        camadaSaida = new double[biasSaida.length]; // * Inicializa o vetor de ativação da camada de saída

        // Calcula as ativações da camada oculta
        for (int i = 0; i < biasOculta.length; i++) {
            double soma = 0;
            for (int j = 0; j < entradas.length; j++) {
                soma += entradas[j] * pesosEntradaOculta[j][i]; // ? Cálculo: Soma ponderada das entradas e pesos
            }
            camadaOculta[i] = sigmoid(soma + biasOculta[i]); // Passa pela função sigmoid e adiciona o bias
        }

        // Calcula as ativações da camada de saída
        for (int i = 0; i < biasSaida.length; i++) {
            double soma = 0;
            for (int j = 0; j < camadaOculta.length; j++) {
                soma += camadaOculta[j] * pesosOcultaSaida[j][i]; // Soma ponderada da camada oculta e pesos
            }
            camadaSaida[i] = sigmoid(soma + biasSaida[i]); // Passa pela função sigmoid e adiciona o bias
        }

        return camadaSaida; // Retorna as ativações da camada de saída
    }

    // ! Método para a retropropagação (backpropagation)
    public void retropropagacao(double[] entradas, double[] saidaEsperada, double taxaAprendizado) {
        double[] errosSaida = new double[camadaSaida.length]; // Vetor de erros para a camada de saída
        double[] deltasSaida = new double[camadaSaida.length]; // Vetor de deltas para a camada de saída
        double[] errosOculta = new double[camadaOculta.length]; // Vetor de erros para a camada oculta
        double[] deltasOculta = new double[camadaOculta.length];// Vetor de deltas para a camada oculta

        // Calcula os erros e deltas da camada de saída
        for (int i = 0; i < camadaSaida.length; i++) {
            errosSaida[i] = saidaEsperada[i] - camadaSaida[i]; // ? Cálculo: Erro = saída esperada - saída atual
            deltasSaida[i] = errosSaida[i] * derivadaSigmoid(camadaSaida[i]); // ? Cálculo: Delta = erro * derivada
                                                                              // sigmoid
        }

        // ! Calcula os erros e deltas da camada oculta
        for (int i = 0; i < camadaOculta.length; i++) {
            double soma = 0;
            for (int j = 0; j < camadaSaida.length; j++) {
                soma += deltasSaida[j] * pesosOcultaSaida[i][j]; // Propaga o erro para a camada oculta
            }
            errosOculta[i] = soma;
            deltasOculta[i] = errosOculta[i] * derivadaSigmoid(camadaOculta[i]); // ? Cálculo: Delta = erro * derivada
                                                                                 // sigmoid
        }

        // ! Atualiza os pesos e bias entre a camada oculta e a camada de saída
        for (int i = 0; i < pesosOcultaSaida.length; i++) {
            for (int j = 0; j < pesosOcultaSaida[i].length; j++) {
                pesosOcultaSaida[i][j] += taxaAprendizado * deltasSaida[j] * camadaOculta[i]; // Ajusta o peso
            }
        }
        for (int i = 0; i < biasSaida.length; i++) {
            biasSaida[i] += taxaAprendizado * deltasSaida[i]; // Ajusta o bias
        }

        // ! Atualiza os pesos e bias entre a camada de entrada e a camada oculta
        for (int i = 0; i < pesosEntradaOculta.length; i++) {
            for (int j = 0; j < pesosEntradaOculta[i].length; j++) {
                pesosEntradaOculta[i][j] += taxaAprendizado * deltasOculta[j] * entradas[i]; // Ajusta o peso
            }
        }
        for (int i = 0; i < biasOculta.length; i++) {
            biasOculta[i] += taxaAprendizado * deltasOculta[i]; // Ajusta o bias
        }
    }

    // ! Método para treinar a rede neural
    public void treinar(double[][] entradasTreinamento, double[][] saidasTreinamento, double taxaAprendizado,
            int epocas) {
        for (int e = 0; e < epocas; e++) {
            for (int i = 0; i < entradasTreinamento.length; i++) {
                propagacaoFrente(entradasTreinamento[i]); // Passo de feedforward
                retropropagacao(entradasTreinamento[i], saidasTreinamento[i], taxaAprendizado); // Passo de
                                                                                                // backpropagation
            }
        }
    }
}