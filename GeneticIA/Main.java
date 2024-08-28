import java.util.ArrayList;
import java.util.Comparator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Main extends ApplicationFrame {

    public Main(String title) {
        super(title);
    }

    public static void main(String[] args) {
        AlgoritmoGenetico ag = new AlgoritmoGenetico(10, 15, 0.10);

        Individuo.calcularProbabilidades(ag.populacao);

        System.out.println("Primeira geração:");
        imprimirPopulacao(ag.populacao);

        ArrayList<Individuo> melhoresIndividuos = new ArrayList<>();
        Individuo melhorInicial = menorFitness(ag.populacao);
        melhoresIndividuos.add(melhorInicial);

        for (int geracao = 0; geracao < ag.maxGeracoes; geracao++) {
            ArrayList<Individuo> novaPopulacao = new ArrayList<>();

            for (int i = 0; i < ag.populacaoInicial; i++) {
                Individuo pai = Individuo.selecionarIndividuoPorProbabilidade(ag.populacao);

                Individuo mae;
                do {
                    mae = Individuo.selecionarIndividuoPorProbabilidade(ag.populacao);
                } while (mae.equals(pai));

                Individuo filho = Individuo.cruzamento(pai, mae);
                Individuo.mutacao(filho, ag.taxaMutacao);
                novaPopulacao.add(filho);
            }

            System.out.println("Nova geração:");
            Individuo.calcularProbabilidades(novaPopulacao);
            imprimirPopulacao(novaPopulacao);

            // Atualiza a população com a nova geração
            ag.populacao = novaPopulacao;

            // Encontra e exibe o melhor indivíduo da geração atual
            Individuo superman = menorFitness(ag.populacao);
            melhoresIndividuos.add(superman);
            System.out.println("Geração " + geracao + ": Melhor Fitness = " + superman.getFitness() + " para x = "
                    + superman.getX() + ", y = " + superman.getY());
        }

        // Plota os melhores indivíduos de cada geração
        plotarMelhoresIndividuos(melhoresIndividuos);
    }

    private static void imprimirPopulacao(ArrayList<Individuo> populacao) {
        for (int index = 0; index < populacao.size(); index++) {
            Individuo individuo = populacao.get(index);
            System.out.println("Individuo " + index + " X=" + individuo.getX() + " Y=" + individuo.getY() + " fit="
                    + individuo.getFitness() + " probabilidade: " + individuo.getProbabilidade());
        }
    }

    public static Individuo menorFitness(ArrayList<Individuo> populacao) {
        return populacao.stream().min(Comparator.comparingDouble(Individuo::getFitness)).orElseThrow();
    }

    public static void plotarMelhoresIndividuos(ArrayList<Individuo> melhoresIndividuos) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < melhoresIndividuos.size(); i++) {
            Individuo individuo = melhoresIndividuos.get(i);
            dataset.addValue(individuo.getFitness(), "Fitness", Integer.toString(i));
        }
    
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Melhores Individuos por Geracao",
                "Geração",
                "Fitness",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
    
        Main chart = new Main("Algoritmo Genético");
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        chart.setContentPane(chartPanel);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
}