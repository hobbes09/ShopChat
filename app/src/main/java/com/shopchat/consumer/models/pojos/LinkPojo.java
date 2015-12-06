package com.shopchat.consumer.models.pojos;

/**
 * Created by sourin on 01/12/15.
 */
public class LinkPojo {

    private String rel;
    private String href;

    public LinkPojo() {
    }

    public LinkPojo(String rel, String href) {
        this.rel = rel;
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
