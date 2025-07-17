package com.pickcar.dailyreport.domain;

import com.pickcar.drivehistory.domain.Region1Depth;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DestinationCountStat {
    private Integer rank;
    private Region1Depth destination;
    private Long visitCount;
}
