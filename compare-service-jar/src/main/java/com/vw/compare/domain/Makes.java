package com.vw.compare.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Makes")
@XmlType(name = "", propOrder = { "make" })
public class Makes implements Response {
   
    private List<Make> make;

    @XmlElement(name = "Make")    
    public List<Make> getMake() {
        if(make == null)
            make = new ArrayList<Make>();
        return make;
    }

    public void setMake(List<Make> make) {
        this.make = make;
    }

    @Override
    public String toString() {
        return "Makes [make=" + make + "]";
    }

}
