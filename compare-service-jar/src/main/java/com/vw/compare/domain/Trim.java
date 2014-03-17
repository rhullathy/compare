package com.vw.compare.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"trimID","imageURL","trimName","modelName","makeName","modelYear",
        "suggestedTrims","competitorVehicle" })
public class Trim {
    @XmlAttribute(name = "ModelYear")
    private String modelYear;
    @XmlAttribute(name = "MakeName")
    private String makeName;
    @XmlAttribute(name = "ModelName")
    private String modelName;
    @XmlAttribute(name = "TrimName")
    private String trimName;
    @XmlAttribute(name = "ImageURL")
    private String imageURL;
    @XmlAttribute(name = "TrimID")
    private String trimID;
    @XmlAttribute(name = "ModelID")
    private String modelID;
    @XmlElement(name = "SuggestedTrims")
    private SuggestedTrims suggestedTrims;
    @XmlTransient 
    private String substituteTrimId;    
    
    @XmlElement(name = "CompetitorVehicle")
    private List<Vehicle> competitorVehicle;

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

    public String getTrimName() {
        return trimName;
    }

    public void setTrimName(String trimName) {
        this.trimName = trimName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTrimID() {
        return trimID;
    }

    public void setTrimID(String trimID) {
        this.trimID = trimID;
    }

    public SuggestedTrims getSuggestedTrims() {
        return suggestedTrims;
    }

    public void setSuggestedTrims(SuggestedTrims suggestedTrims) {
        this.suggestedTrims = suggestedTrims;
    }

    @Override
    public String toString() {
        return "Trim [modelYear=" + modelYear + ", makeName=" + makeName + ", modelName=" + modelName + ", trimName="
                + trimName + ", imageURL=" + imageURL + ", trimID=" + trimID + ", suggestedTrims=" + suggestedTrims
                + "]";
    }

    public String getModelID() {
        return modelID;
    }

    public void setModelID(String modelID) {
        this.modelID = modelID;
    }

    public String getSubstituteTrimId() {
        return substituteTrimId;
    }

    public void setSubstituteTrimId(String substituteTrimId) {
        this.substituteTrimId = substituteTrimId;
    }
    
    public List<Vehicle> getCompetitorVehicle() {
        if(competitorVehicle == null)
            competitorVehicle = new ArrayList<Vehicle>();
        return competitorVehicle;
    }

    public void setCompetitorVehicle(List<Vehicle> competitorVehicle) {
        this.competitorVehicle = competitorVehicle;
    }

}
