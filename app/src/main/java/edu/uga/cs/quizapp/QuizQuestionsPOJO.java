package edu.uga.cs.quizapp;

public class QuizQuestionsPOJO {

    private int id;
    private int quizId;
    private int questionId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public QuizQuestionsPOJO(int id, int quizId, int questionId) {
        this.id = id;
        this.quizId = quizId;
        this.questionId = questionId;
    }

    public QuizQuestionsPOJO() {
    }

    @Override
    public String toString() {
        return "QuizQuestionsPOJO{" +
                "id=" + id +
                ", quizId=" + quizId +
                ", questionId=" + questionId +
                '}';
    }
}
