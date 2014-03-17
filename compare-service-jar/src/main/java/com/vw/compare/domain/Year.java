package com.vw.compare.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "modelYear", "make" })
public class Year {
    @XmlAttribute(name = "ModelYear", required = true)
    private String modelYear;
    @XmlElement(name = "Make")
    private List<Make> make;

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public List<Make> getMake() {
        return make;
    }

    public void setMake(List<Make> make) {
        this.make = make;
    }

    @Override
    public String toString() {
        return "Year [modelYear=" + modelYear + ", make=" + make + "]";
    }

}
