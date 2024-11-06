package edu.uga.cs.quizapp;

public class QuizResultPOJO {

    private int id;
    private long result;
    private String date;

    public QuizResultPOJO() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getResult() {
        return result;
    }

    public void setResult(long result) {
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public QuizResultPOJO(long result, String date) {
        this.result = result;
        this.date = date;
    }

    @Override
    public String toString() {
        return "QuizResultPOJO{" +
                "id=" + id +
                ", result=" + result +
                ", date='" + date + '\'' +
                '}';
    }
}
