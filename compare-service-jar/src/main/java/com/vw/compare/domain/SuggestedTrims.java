package com.vw.compare.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "trim" })
public class SuggestedTrims {
    @XmlElement(name = "Trim")
    private List<Trim> trim;

    public List<Trim> getTrim() {
        return trim;
    }

    public void setTrim(List<Trim> trim) {
        this.trim = trim;
    }

    @Override
    public String toString() {
        return "SuggestedTrims [trim=" + trim + "]";
    }

}
