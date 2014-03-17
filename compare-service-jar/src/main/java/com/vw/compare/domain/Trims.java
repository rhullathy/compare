package com.vw.compare.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Trims")
@XmlType(name = "")
public class Trims implements Response {
   
    private List<Year> year;

   @XmlElement(name = "Year")   
    public List<Year> getYear() {
        return year;
    }

    public void setYear(List<Year> year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Trims [year=" + year + "]";
    }

}
