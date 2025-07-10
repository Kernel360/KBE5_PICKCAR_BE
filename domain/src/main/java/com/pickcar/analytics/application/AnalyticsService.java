package com.pickcar.analytics.application;

import com.pickcar.analytics.presentation.dto.response.StaticAnalyticsResponse;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    public StaticAnalyticsResponse getStaticAnalytics() {

    /* TODO:
            Integer totalVehicleCount,
            Integer reservedVehicleCount,
            Integer notOperableVehicleCount,
            Integer expectedReturnCount,
            Integer delayedCount
     */

        return new StaticAnalyticsResponse(null, null, null, null, null);
    }


}
