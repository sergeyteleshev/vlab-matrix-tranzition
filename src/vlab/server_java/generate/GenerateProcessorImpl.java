package vlab.server_java.generate;

import org.json.JSONObject;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.generate.GenerateProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

import static vlab.server_java.Consts.initialMatrixMaxElement;
import static vlab.server_java.Consts.initialMatrixMinElement;

/**
 * Simple GenerateProcessor implementation. Supposed to be changed as needed to
 * provide necessary Generate method support.
 */
public class GenerateProcessorImpl implements GenerateProcessor {
    @Override
    public GeneratingResult generate(String condition) {
        //do Generate logic here
        String text = "text";
        String code = "code";
        String instructions = "instructions";
        JSONObject generatedVariant = new JSONObject();

        int n = 8; //размерность матрицы
        int d = 15; //количество ненулевых элементов матрицы

        double[][] initialMatrix = generateInitialMatrix(n, d);

        generatedVariant.put("initialMatrix", initialMatrix);
        generatedVariant.put("n", n);
        generatedVariant.put("d", d);

        code = generatedVariant.toString();

        return new GeneratingResult(text, code, instructions);
    }

    public double[][] generateInitialMatrix(int n, int d)
    {
        double[][] initialMatrix = new double[n][n];
        int amountOfAddedNonZeroElements = 0;

        while (amountOfAddedNonZeroElements < d)
        {
            int i = generateRandomIntRange(0, n - 1);
            int j = generateRandomIntRange(0, n - 1);

            if(i == j)
                continue;

            if(initialMatrix[i][j] == 0 && initialMatrix[j][i] == 0)
            {
                initialMatrix[i][j] = roundDoubleToNDecimals(generateRandomDoubleRange(initialMatrixMinElement, initialMatrixMaxElement), 2);
                amountOfAddedNonZeroElements++;
            }
        }

        return initialMatrix;
    }

    private static double roundDoubleToNDecimals(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double generateRandomDoubleRange(double min, double max)
    {
        return Math.random() * (max - min) + min;
    }

    public static int generateRandomIntRange(int min, int max)
    {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
