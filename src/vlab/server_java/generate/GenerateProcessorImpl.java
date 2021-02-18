package vlab.server_java.generate;

import org.json.JSONObject;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.generate.GenerateProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static vlab.server_java.Consts.initialMatrixMaxElement;
import static vlab.server_java.Consts.initialMatrixMinElement;
import static vlab.server_java.check.CheckProcessorImpl.getCompositionMatrix;

/**
 * Simple GenerateProcessor implementation. Supposed to be changed as needed to
 * provide necessary Generate method support.
 */
public class GenerateProcessorImpl implements GenerateProcessor {
    @Override
    public GeneratingResult generate(String condition) {
        //do Generate logic here
        String text = "Ваш вариант загружен в виртуальный стенд.";
        String code = "";
        String instructions = "";
        JSONObject generatedVariant = new JSONObject();

        try
        {
            //для того чтобы сервер данные подтянулись из кадров на de
    //        int n = Integer.parseInt(condition.split(",")[0]);
    //        int d = Integer.parseInt(condition.split(",")[1]);

            //для ВЛТ, так как там нет кадров
            int n = 4; //размерность матрицы
            int d = 3; //количество ненулевых элементов матрицы

            double[][] initialMatrix = generateInitialMatrix(n, d);

    //        double[][] initialMatrix = {
    //                {0, 0.3, 0.6, 0.5},
    //                {0, 0, 0, 0},
    //                {0, 0, 0, 0},
    //                {0, 0.6, 0.2, 0},
    //        };

//            double[][] initialMatrix = {
//                    {0, 1, 0, 1},
//                    {0, 0, 0, 0},
//                    {0, 1, 0, 1},
//                    {0, 1, 0, 0},
//            };
//
//            double[][] initialMatrix = {
//                {1, 1, 0, 0.7},
//                {0, 0.5, 0, 0},
//                {0, 0.9, 1, 0.5},
//                {0, 0.8, 0, 1},
//            };


            generatedVariant.put("initialMatrix", initialMatrix);
            generatedVariant.put("n", n);
            generatedVariant.put("d", d);
            double[][] compM = getCompositionMatrix(initialMatrix, initialMatrix);
            text += " " + Arrays.deepToString(compM);

            code = generatedVariant.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


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

            if(i == j || initialMatrix[i][j] != 0 || initialMatrix[j][i] != 0)
                continue;

            if(initialMatrix[i][j] == 0.0 && initialMatrix[j][i] == 0.0)
            {
                initialMatrix[i][j] = roundDoubleToNDecimals(generateRandomDoubleRange(initialMatrixMinElement, initialMatrixMaxElement), 1);
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
