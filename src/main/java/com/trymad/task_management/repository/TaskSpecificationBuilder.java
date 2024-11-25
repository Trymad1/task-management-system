package com.trymad.task_management.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.trymad.task_management.model.Task;
import com.trymad.task_management.model.TaskPriority;
import com.trymad.task_management.model.TaskStatus;

@Component
public class TaskSpecificationBuilder {

    private Specification<Task> authorIdSpec;
    private Specification<Task> executorIdSpec;
    private Specification<Task> statusSpec;
    private Specification<Task> prioritySpec;

    public TaskSpecificationBuilder authorId(Long id) {
        this.authorIdSpec = id == null ? null
                : Specification.where(
                        (root, query, builder) -> builder.equal(root.get("author").get("id"), id));
        return this;
    }

    public TaskSpecificationBuilder executorId(Long id) {
        this.executorIdSpec = id == null ? null
                : Specification.where(
                        (root, query, builder) -> builder.equal(root.get("executor").get("id"), id));
        return this;
    }

    public TaskSpecificationBuilder status(TaskStatus status) {
        this.statusSpec = status == null ? null
                : Specification.where(
                        (root, query, builder) -> builder.equal(root.get("status").get("value"), status));
        return this;
    }

    public TaskSpecificationBuilder priority(TaskPriority priority) {
        this.prioritySpec = priority == null ? null
                : Specification.where(
                        (root, query, builder) -> builder.equal(root.get("priority").get("value"), priority));
        return this;
    }

    public Specification<Task> build() {
        final Specification<Task> spec = Specification.allOf(
            authorIdSpec, executorIdSpec, statusSpec, prioritySpec);

        this.clearFields();
        return spec;
    }

    private void clearFields() {
        this.authorIdSpec = null;
        this.executorIdSpec = null;
        this.prioritySpec = null;
        this.statusSpec = null;
    }

}
