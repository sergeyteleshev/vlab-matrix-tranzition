package vlab.server_java.check;

import jdk.nashorn.internal.runtime.ECMAException;
import org.json.JSONArray;
import org.json.JSONObject;
import rlcp.check.ConditionForChecking;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.CheckProcessor;
import rlcp.server.processor.check.PreCheckProcessor;
import rlcp.server.processor.check.PreCheckProcessor.PreCheckResult;
import rlcp.server.processor.check.PreCheckResultAwareCheckProcessor;

import java.math.BigDecimal;
import java.util.Arrays;

import static vlab.server_java.Consts.*;

/**
 * Simple CheckProcessor implementation. Supposed to be changed as needed to provide
 * necessary Check method support.
 */
public class CheckProcessorImpl implements PreCheckResultAwareCheckProcessor<String> {
    @Override
    public CheckingSingleConditionResult checkSingleCondition(ConditionForChecking condition, String instructions, GeneratingResult generatingResult) throws Exception {
        //do check logic here
        double points = 0;
        String comment = "";

        try
        {
            String code = generatingResult.getCode();
            JSONObject jsonCode = new JSONObject(code); // сгенерированный вариант
            JSONObject jsonInstructions = new JSONObject(instructions); // ответ пользователя

            double[][] initialMatrix = twoDimensionalJsonArrayToDouble(jsonCode.getJSONArray("initialMatrix"));
            double[][] serverCompositionMatrix = getCompositionMatrix(initialMatrix, initialMatrix);
            int[][] serverTranzitionMatrix = getTranzitionMatrix(initialMatrix, serverCompositionMatrix);
            int serverCompositionMatrixRows = serverCompositionMatrix.length;
            int serverCompositionMatrixColumns = serverCompositionMatrix[0].length;

            double[][] clientCompositionMatrix = twoDimensionalJsonArrayToDouble(jsonInstructions.getJSONArray("compositionMatrix"));
            int[][] clientTranzitionMatrix = twoDimensionalJsonArrayToInt(jsonInstructions.getJSONArray("tranzitionMatrix"));
            int clientCompositionMatrixColumns = jsonInstructions.getInt("compositionMatrixColumns");
            int clientCompositionMatrixRows = jsonInstructions.getInt("compositionMatrixRows");

            if(serverCompositionMatrixColumns == clientCompositionMatrixColumns)
            {
                points += compositionMatrixSizePoints / 2;
            }
            else
            {
                comment += "Неверное количество столбцов в матрице отношения. ";
            }

            if(serverCompositionMatrixRows == clientCompositionMatrixRows)
            {
                points += compositionMatrixSizePoints / 2;
            }
            else
            {
                comment += "Неверное количество строк в матрице отношения. ";
            }

            if(points == compositionMatrixSizePoints)
            {
                JSONObject compositionMatrixCheckAnswer = checkCompositionMatrix(serverCompositionMatrix, clientCompositionMatrix, compositionMatrixPoints);
                JSONObject tranzitionMatrixCheckAnswer = checkTranzitionMatrix(serverTranzitionMatrix, clientTranzitionMatrix, tranzitionMatrixPoints);

                double compositionMatrixPoints = roundDoubleToNDecimals(compositionMatrixCheckAnswer.getDouble("points"), 2);
                String compositionMatrixComment = compositionMatrixCheckAnswer.getString("comment");

                double tranzitionMatrixPoints = roundDoubleToNDecimals(tranzitionMatrixCheckAnswer.getDouble("points"), 2);
                String tranzitionMatrixComment = tranzitionMatrixCheckAnswer.getString("comment");

                points += compositionMatrixPoints + tranzitionMatrixPoints;
                comment += compositionMatrixComment + tranzitionMatrixComment;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new CheckingSingleConditionResult(BigDecimal.valueOf(points), comment);
    }

    private static int countNonZerosInDoubleTwoDimensionalArray(double[][] arr)
    {
        int countedNonZeros = 0;

        for(int i = 0; i < arr.length; i++)
        {
            for(int j = 0; j < arr[i].length; j++)
            {
                if(arr[i][j] != 0)
                    countedNonZeros++;
            }
        }

        return countedNonZeros;
    }

    private static int countNonZerosInIntTwoDimensionalArray(int[][] arr)
    {
        int countedNonZeros = 0;

        for(int i = 0; i < arr.length; i++)
        {
            for(int j = 0; j < arr[i].length; j++)
            {
                if(arr[i][j] != 0)
                    countedNonZeros++;
            }
        }

        return countedNonZeros;
    }

    static JSONObject checkTranzitionMatrix(int[][] serverAnswer, int[][] clientAnswer, double points)
    {
        int matrixColumnsAmount = serverAnswer.length;
        int matrixRowsAmount = serverAnswer[0].length;
        double clientPoints = 0;
        StringBuilder comment = new StringBuilder();
        JSONObject result = new JSONObject();
        int tranzitionMatrixNonZeroElements = countNonZerosInIntTwoDimensionalArray(serverAnswer);
        double deltaPoints = points / (tranzitionMatrixNonZeroElements);

        try
        {
            for(int i = 0; i < serverAnswer.length; i++)
            {
                for(int j = 0; j < serverAnswer[i].length; j++)
                {
                    if(serverAnswer[i][j] == clientAnswer[i][j])
                    {
                        if(serverAnswer[i][j] != 0)
                            clientPoints += deltaPoints;
                    }
                    else
                    {
                        comment.append("Неверное значение элемента TM[").append(Integer.toString(i + 1)).append(", ").append(Integer.toString(j + 1)).append("] матрицы транзитивности отношения: sys = ").append(serverAnswer[i][j]).append("; user = ").append(clientAnswer[i][j]).append(". ");
                    }
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            comment = new StringBuilder("Неверный размер матрицы.");
            clientPoints = 0;
        }

        result.put("points", clientPoints);
        result.put("comment", comment.toString());

        return result;
    }

    static JSONObject checkCompositionMatrix(double[][] serverAnswer, double[][] clientAnswer, double points)
    {
        double matrixColumnsAmount = serverAnswer.length;
        double matrixRowsAmount = serverAnswer[0].length;
        double clientPoints = 0;
        StringBuilder comment = new StringBuilder();
        JSONObject result = new JSONObject();
        int serverAnswerNonZerosElements = countNonZerosInDoubleTwoDimensionalArray(serverAnswer);
        double deltaPoints = points / (serverAnswerNonZerosElements);

        try
        {
            for(int i = 0; i < serverAnswer.length; i++)
            {
                for(int j = 0; j < serverAnswer[i].length; j++)
                {
                    if(serverAnswer[i][j] == clientAnswer[i][j])
                    {
                        if (serverAnswer[i][j] != 0)
                            clientPoints += deltaPoints;
                    }
                    else
                    {
                        comment.append("Неверное значение элемента CM[").append(Integer.toString(i + 1)).append(", ").append(Integer.toString(j + 1)).append("] матрицы композиции: sys = ").append(serverAnswer[i][j]).append("; user = ").append(clientAnswer[i][j]).append(". ");
                    }
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            comment = new StringBuilder("Неверный размер матрицы композиции. ");
            clientPoints = 0;
        }

        result.put("points", clientPoints);
        result.put("comment", comment.toString());

        return result;
    }

    private static int[][] getTranzitionMatrix(double [][] initialMatrix, double[][] compositionMatrix)
    {
        int[][] tranzitionMatrix = new int[initialMatrix.length][initialMatrix.length];

        for(int i = 0; i < initialMatrix.length; i++)
        {
            for(int j = 0; j < initialMatrix[i].length; j++)
            {
                if(compositionMatrix[i][j] > initialMatrix[i][j])
                    tranzitionMatrix[i][j] = 1;
                else
                    tranzitionMatrix[i][j] = 0;
            }
        }

        return tranzitionMatrix;
    }

    private static double[][] getCompositionMatrix(double[][] R1Set, double[][] R2Set)
    {
        double[][] R1R2Set = new double[R1Set.length][R2Set[0].length];
        for(int i = 0; i < R1Set.length; i++)
        {
            for (int j = 0; j < R2Set[0].length; j++)
            {
                double[] minElements = new double[R1Set[0].length];
                for(int k = 0; k < R1Set[0].length; k++)
                {
                    minElements[k] = Math.min(R1Set[i][k], R2Set[k][j]);
                }

                R1R2Set[i][j] = Arrays.stream(minElements).max().getAsDouble();
            }
        }

        return R1R2Set;
    }

    public static double[][] twoDimensionalJsonArrayToDouble(JSONArray arr)
    {
        double[][] result = new double[arr.length()][arr.getJSONArray(0).length()];

        for(int i = 0; i < arr.length(); i++)
        {
            for(int j = 0; j < arr.getJSONArray(i).length(); j++)
            {
                result[i][j] = arr.getJSONArray(i).getDouble(j);
            }
        }

        return result;
    }

    public static int[][] twoDimensionalJsonArrayToInt(JSONArray arr)
    {
        int[][] result = new int[arr.length()][arr.getJSONArray(0).length()];

        for(int i = 0; i < arr.length(); i++)
        {
            for(int j = 0; j < arr.getJSONArray(i).length(); j++)
            {
                result[i][j] = arr.getJSONArray(i).getInt(j);
            }
        }

        return result;
    }

    @Override
    public void setPreCheckResult(PreCheckResult<String> preCheckResult) {}
}
