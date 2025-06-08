package com.pickcar.gpx.domain;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement(name = "gpx")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter
public class GpxData {

    @XmlElement(name = "trk")
    private Track track;
}
