package com.vw.compare.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "substituteTrimID",  "substituteTrimName", "substituteUsed" })
public class PhotoGalleryInfo {
    @XmlAttribute(name = "SubstituteUsed")
    private String substituteUsed;
    @XmlAttribute(name = "SubstituteTrimName")
    private String substituteTrimName;
    @XmlAttribute(name = "SubstituteTrimID")
    private String substituteTrimID;

    public String getSubstituteUsed() {
        return substituteUsed;
    }

    public void setSubstituteUsed(String substituteUsed) {
        this.substituteUsed = substituteUsed;
    }

    public String getSubstituteTrimName() {
        return substituteTrimName;
    }

    public void setSubstituteTrimName(String substituteTrimName) {
        this.substituteTrimName = substituteTrimName;
    }

    public String getSubstituteTrimID() {
        return substituteTrimID;
    }

    public void setSubstituteTrimID(String substituteTrimID) {
        this.substituteTrimID = substituteTrimID;
    }

    @Override
    public String toString() {
        return "PhotoGalleryInfo [substituteUsed=" + substituteUsed + ", substituteTrimName=" + substituteTrimName
                + ", substituteTrimID=" + substituteTrimID + "]";
    }

}
