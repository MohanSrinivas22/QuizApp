package edu.uga.cs.quizapp;

import java.util.List;

/**
 * A class to manage quiz result, date, quizId.
 */
public class ResultManager {


    private static int quizId;
    private static Long cumulativeResult = 0L;
    private static String date;

    private static List<StateCapitalPOJO> list;

    public static List<StateCapitalPOJO> getList() {
        return list;
    }

    public static void setList(List<StateCapitalPOJO> list) {
        ResultManager.list = list;
    }

    private static int position;

    public static int getPosition() {
        return position;
    }

    public static void setPosition(int position) {
        ResultManager.position = position;
    }


    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        ResultManager.date = date;
    }

    public static int getQuizId() {
        return quizId;
    }


    public static void setQuizId(int quizId) {
        ResultManager.quizId = quizId;
    }

    public static Long getCumulativeResult() {
        return cumulativeResult;
    }

    public static void addToCumulativeResult(Long result) {
        cumulativeResult += result;
    }

    public static void resetCumulativeResult() {
        cumulativeResult = 0L;
    }
    public static void resetQuizId() {
        quizId = 0;
    }
    public static void resetQuizDate() {
        date = "";
    }
}
