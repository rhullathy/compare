package com.vw.compare.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Comparison")
@XmlType(name = "", propOrder = { "primaryVehicle", "competitorVehicle", "comparisonData" })
public class Comparison implements Response {
    
    private Vehicle primaryVehicle;
    
    private List<Vehicle> competitorVehicle;
    
    private ComparisonData comparisonData;
    
    @XmlElement(name = "PrimaryVehicle")
    public Vehicle getPrimaryVehicle() {
        return primaryVehicle;
    }

    public void setPrimaryVehicle(Vehicle primaryVehicle) {
        this.primaryVehicle = primaryVehicle;
    }
    @XmlElement(name = "CompetitorVehicle")
    public List<Vehicle> getCompetitorVehicle() {
        if(competitorVehicle == null)
            competitorVehicle = new ArrayList<Vehicle>();
        return competitorVehicle;
    }

    public void setCompetitorVehicle(List<Vehicle> competitorVehicle) {
        this.competitorVehicle = competitorVehicle;
    }
    @XmlElement(name = "ComparisonData")
    public ComparisonData getComparisonData() {
        return comparisonData;
    }

    public void setComparisonData(ComparisonData comparisionData) {
        this.comparisonData = comparisionData;
    }

    @Override
    public String toString() {
        return "Comparison [primaryVehicle=" + primaryVehicle + ", competitorVehicle=" + competitorVehicle
                + ", comparisionData=" + comparisonData + "]";
    }

}
