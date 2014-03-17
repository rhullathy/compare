package com.vw.compare.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "heading" })
public class ComparisonData {

    @XmlElement(name = "Heading")
    private List<Heading> heading;

    public List<Heading> getHeading() {
        return heading;
    }

    public void setHeading(List<Heading> heading) {
        this.heading = heading;
    }

    @Override
    public String toString() {
        return "ComparisionData [heading=" + heading + "]";
    }

}
