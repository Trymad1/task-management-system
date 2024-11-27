package com.trymad.task_management.web.dto.task;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonMerge;
import com.trymad.task_management.web.validation.TaskPriorityCheck;
import com.trymad.task_management.web.validation.TaskStatusCheck;

import jakarta.validation.constraints.Size;
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
    @Size(max = 40, message = "maximum title length is 24")
    String title;

    @JsonMerge
    @Size(max = 40, message = "maximum description length is 255")
    String description;

    @JsonMerge
    @TaskStatusCheck(allowNull = true)
    String status;

    @JsonMerge
    @TaskPriorityCheck(allowNull = true)
    String priority;

    @JsonAnySetter
    public void handleUnknown(String name, Object value) {
        throw new IllegalArgumentException("Invalid request field: " + name);
    }

}