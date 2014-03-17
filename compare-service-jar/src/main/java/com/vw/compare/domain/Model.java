package com.vw.compare.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "modelID","modelName","makeName", "modelYear",   "trim" })
public class Model {
    @XmlAttribute(name = "ModelYear")
    private String modelYear;
    @XmlAttribute(name = "MakeName")
    private String makeName;
    @XmlAttribute(name = "ModelName")
    private String modelName;
    @XmlAttribute(name = "ModelID")
    private String modelID;
    @XmlElement(name = "Trim")
    private List<Trim> trim;

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelID() {
        return modelID;
    }

    public void setModelID(String modelID) {
        this.modelID = modelID;
    }

    public List<Trim> getTrim() {
        return trim;
    }

    public void setTrim(List<Trim> trim) {
        this.trim = trim;
    }

    @Override
    public String toString() {
        return "Model [modelYear=" + modelYear + ", makeName=" + makeName + ", modelName=" + modelName + ", modelID="
                + modelID + ", trim=" + trim + "]";
    }

}
