package com.vw.compare.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Response")
public class MockResponse implements Response {

    private String data;

    public MockResponse() {

    }

    /**
     * @param data
     */
    public MockResponse(String data) {
        super();
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
