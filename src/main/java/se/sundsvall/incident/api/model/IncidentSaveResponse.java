package se.sundsvall.incident.api.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(setterPrefix = "with", builderClassName = "Builder")
@JsonDeserialize(builder = IncidentSaveResponse.Builder.class)
@AllArgsConstructor
public class IncidentSaveResponse {

    private String status;
    private String incidentId;

}
