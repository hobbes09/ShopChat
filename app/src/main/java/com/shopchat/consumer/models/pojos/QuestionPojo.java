package com.shopchat.consumer.models.pojos;

/**
 * Created by sourin on 01/12/15.
 */
public class QuestionPojo {

    private String id;
    private long lastModifiedDate;
    private ProductPojo product;
    private String questionText;
    private AnswerPojo[] answers;
    private String parentQuestion;
    private String[] links;

    public QuestionPojo(String id, long lastModifiedDate, ProductPojo product, String questionText, AnswerPojo[] answers, String parentQuestion, String[] links) {
        this.id = id;
        this.lastModifiedDate = lastModifiedDate;
        this.product = product;
        this.questionText = questionText;
        this.answers = answers;
        this.parentQuestion = parentQuestion;
        this.links = links;
    }

    public QuestionPojo(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(long lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public ProductPojo getProduct() {
        return product;
    }

    public void setProduct(ProductPojo product) {
        this.product = product;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public AnswerPojo[] getAnswers() {
        return answers;
    }

    public void setAnswers(AnswerPojo[] answers) {
        this.answers = answers;
    }

    public String getParentQuestion() {
        return parentQuestion;
    }

    public void setParentQuestion(String parentQuestion) {
        this.parentQuestion = parentQuestion;
    }

    public String[] getLinks() {
        return links;
    }

    public void setLinks(String[] links) {
        this.links = links;
    }
}
