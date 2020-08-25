package server;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ServerCommand {

    public static String generateRandom() {
        Random rand = new Random();
        String operator = getRandomOperator(rand);
        int firstOperand = getRandomNumber(rand);
        int secondOperand = getRandomNumber(rand);
        return operator + " " + firstOperand + " " + secondOperand;
    }

    private static String getRandomOperator(Random rand) {
        List<String> operators = Arrays.asList("/", "-", "+", "*", "invalidOperator");
        return operators.get(rand.nextInt(operators.size()));
    }

    private static int getRandomNumber(Random rand) {
        return rand.nextInt(50) + 1;
    }

}