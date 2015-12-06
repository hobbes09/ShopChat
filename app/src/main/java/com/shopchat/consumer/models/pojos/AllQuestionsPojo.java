package com.shopchat.consumer.models.pojos;

/**
 * Created by sourin on 01/12/15.
 */
public class AllQuestionsPojo {

    private LinkPojo[] links;
    private QuestionPojo[] content;
    private PagePojo page;

    public AllQuestionsPojo(LinkPojo[] links, QuestionPojo[] content, PagePojo page) {
        this.links = links;
        this.content = content;
        this.page = page;
    }

    public AllQuestionsPojo(){
    }

    public LinkPojo[] getLinks() {
        return links;
    }

    public void setLinks(LinkPojo[] links) {
        this.links = links;
    }

    public QuestionPojo[] getContent() {
        return content;
    }

    public void setContent(QuestionPojo[] content) {
        this.content = content;
    }

    public PagePojo getPage() {
        return page;
    }

    public void setPage(PagePojo page) {
        this.page = page;
    }
}
