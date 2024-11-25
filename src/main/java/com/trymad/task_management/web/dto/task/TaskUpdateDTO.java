package com.trymad.task_management.web.dto.task;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonMerge;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonIgnoreProperties(ignoreUnknown = false)
public class TaskUpdateDTO {

    @JsonMerge
    // If executorId in request is not not specified, set to 0 id
    // this is necessary so that the value can be set to null
    // in service if this field equals 0, it dont change entity
    Long executorId = 0L;

    @JsonMerge
    String title;

    @JsonMerge
    String description;

    @JsonMerge
    String status;

    @JsonMerge
    String priority;

    @JsonAnySetter
    public void handleUnknown(String name, Object value) {
        throw new IllegalArgumentException("Invalid request field: " + name);
    }

}