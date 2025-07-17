package com.pickcar.drivehistory.presentation.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoReverseGeocodeResponse {

    private List<Document> documents;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Document {
        @JsonProperty("region_1depth_name")
        private String region2depthName;
    }
}
