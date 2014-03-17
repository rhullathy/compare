package com.vw.compare.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "trimID","imageURL","trimName","modelName","makeName","modelYear",
        "photoGalleryInfo" })
public class Vehicle {
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
    @XmlElement(name = "PhotoGalleryInfo")
    private PhotoGalleryInfo photoGalleryInfo;

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

    public PhotoGalleryInfo getPhotoGalleryInfo() {
        return photoGalleryInfo;
    }

    public void setPhotoGalleryInfo(PhotoGalleryInfo photoGalleryInfo) {
        this.photoGalleryInfo = photoGalleryInfo;
    }

    @Override
    public String toString() {
        return "Vehicle [modelYear=" + modelYear + ", makeName=" + makeName + ", modelName=" + modelName
                + ", trimName=" + trimName + ", imageURL=" + imageURL + ", trimID=" + trimID + ", photoGalleryInfo="
                + photoGalleryInfo + "]";
    }

}
