package com.vw.compare.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {   "tmDescription", "tmFlag","advantageText","advantageFlag","colData"  })
public class VehicleData {
    @XmlAttribute(name = "ColData")
    private String colData;
    @XmlAttribute(name = "AdvantageFlag")
    private String advantageFlag;
    @XmlAttribute(name = "AdvantageText")
    private String advantageText;
    @XmlAttribute(name = "TMFlag")
    private String tmFlag;
    @XmlAttribute(name = "TMDescription")
    private String tmDescription;
    @XmlTransient
    private String trimId;
    public String getColData() {
        return colData;
    }

    public void setColData(String colData) {
        this.colData = colData;
    }

    public String getAdvantageFlag() {
        return advantageFlag;
    }

    public void setAdvantageFlag(String advantageFlag) {
        this.advantageFlag = advantageFlag;
    }

    public String getAdvantageText() {
        return advantageText;
    }

    public void setAdvantageText(String advantageText) {
        this.advantageText = advantageText;
    }

    public String getTmFlag() {
        return tmFlag;
    }

    public void setTmFlag(String tmFlag) {
        this.tmFlag = tmFlag;
    }

    public String getTmDescription() {
        return tmDescription;
    }

    public void setTmDescription(String tmDescription) {
        this.tmDescription = tmDescription;
    }

    @Override
    public String toString() {
        return "VehicleData [colData=" + colData + ", advantageFlag=" + advantageFlag + ", advantageText="
                + advantageText + ", tmFlag=" + tmFlag + ", tmDescription=" + tmDescription + "]";
    }

	public void setTrimId(String trimId) {
		this.trimId = trimId;
	}

	public String getTrimId() {
		return trimId;
	}
}
