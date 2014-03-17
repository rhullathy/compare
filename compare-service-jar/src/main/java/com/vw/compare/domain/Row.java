package com.vw.compare.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "glossaryText", "rowLabel",  "vehicleData" })
public class Row {
    @XmlAttribute(name = "RowLabel")
    private String rowLabel;
    @XmlAttribute(name = "GlossaryText")
    private String glossaryText;
    
    @XmlElement(name = "VehicleData")
    private List<VehicleData> vehicleData;

    public String getRowLabel() {
        return rowLabel;
    }

    public void setRowLabel(String rowLabel) {
        this.rowLabel = rowLabel;
    }

    public String getGlossaryText() {
        return glossaryText;
    }

    public void setGlossaryText(String glossaryText) {
        this.glossaryText = glossaryText;
    }

    public List<VehicleData> getVehicleData() {
        return vehicleData;
    }

    public void setVehicleData(List<VehicleData> vehicleData) {
        this.vehicleData = vehicleData;
    }

    @Override
    public String toString() {
        return "Row [rowLabel=" + rowLabel + ", glossaryText=" + glossaryText + ", vehicleData=" + vehicleData + "]";
    }

}
