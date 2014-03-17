package com.vw.compare.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ModelTrims")
@XmlType(name = "", propOrder = { "trim" })
public class ModelTrims implements Response {
    private List<Trim> trim;
    
    @XmlElement(name = "Trim")    
    public List<Trim> getTrim() {
        return trim;
    }

    public void setTrim(List<Trim> trim) {
        this.trim = trim;
    }

    @Override
    public String toString() {
        return "ModelTrims [trim=" + trim + "]";
    }

}
