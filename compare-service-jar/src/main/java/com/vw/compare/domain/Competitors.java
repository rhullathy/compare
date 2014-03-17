package com.vw.compare.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Competitors")
@XmlType(name = "", propOrder = { "baseVehicle" })
public class Competitors implements Response {
	
   
    
    private List<Trim> baseVehicle;

    @XmlElement(name = "BaseVehicle")
    public List<Trim> getBaseVehicle() {
		return baseVehicle;
	}

	public void setBaseVehicle(List<Trim> baseVehicle) {
		this.baseVehicle = baseVehicle;
	}
	


}
