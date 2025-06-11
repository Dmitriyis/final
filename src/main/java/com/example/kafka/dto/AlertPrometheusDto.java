package com.example.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class AlertPrometheusDto {

    @JsonProperty("version")
    private String version;

    @JsonProperty("groupKey")
    private String groupKey;

    @JsonProperty("status")
    private String status;

    @JsonProperty("receiver")
    private String receiver;

    @JsonProperty("groupLabels")
    private Labels groupLabels;

    @JsonProperty("commonLabels")
    private Labels commonLabels;

    @JsonProperty("commonAnnotations")
    private Annotations commonAnnotations;

    @JsonProperty("alerts")
    private List<Alert> alerts;

    @Data
    public static class Labels {
        @JsonProperty("alertname")
        private String alertname;

        @JsonProperty("cluster")
        private String cluster;

        @JsonProperty("instance")
        private String instance;

        @JsonProperty("severity")
        private String severity;
    }

    @Data
    public static class Annotations {
        @JsonProperty("summary")
        private String summary;

        @JsonProperty("description")
        private String description;
    }

    @Data
    public static class Alert {
        @JsonProperty("status")
        private String status;

        @JsonProperty("labels")
        private Labels labels;

        @JsonProperty("annotations")
        private Annotations annotations;

        @JsonProperty("startsAt")
        private ZonedDateTime startsAt;

        @JsonProperty("endsAt")
        private ZonedDateTime endsAt;

        @JsonProperty("generatorURL")
        private String generatorURL;
    }
}
