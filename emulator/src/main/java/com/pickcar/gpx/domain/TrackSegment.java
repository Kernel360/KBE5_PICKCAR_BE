package com.pickcar.gpx.domain;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter
public class TrackSegment {

    @XmlElement(name = "trkpt")
    private List<TrackPoint> trackPoints;

}
