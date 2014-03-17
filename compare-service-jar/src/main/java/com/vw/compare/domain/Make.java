package com.vw.compare.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "year", "makeName",  "model" })
public class Make {
    @XmlAttribute(name = "MakeName")
    private String makeName;
    @XmlAttribute(name = "Year")
    private String year;
    @XmlElement(name = "Model")
    private List<Model> model;

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<Model> getModel() {
        return model;
    }

    public void setModel(List<Model> model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Make [makeName=" + makeName + ", year=" + year + ", model=" + model + "]";
    }

}
