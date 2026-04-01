package com.sani.taskmanager.specification;

import org.springframework.data.jpa.domain.Specification;
import com.sani.taskmanager.model.Task;
import com.sani.taskmanager.model.TaskPriority;
import com.sani.taskmanager.model.TaskStatus;

import java.time.LocalDate;

public class TaskSpecification {

    public static Specification<Task> hasPriority(String priority) {
        return (root, query, cb) ->
                priority == null ? null :
                cb.equal(root.get("priority"), TaskPriority.valueOf(priority.toUpperCase()));
    }

    public static Specification<Task> hasStatus(String status) {
        return (root, query, cb) ->
                status == null ? null :
                cb.equal(root.get("status"), TaskStatus.valueOf(status.toUpperCase()));
    }

    public static Specification<Task> dueBefore(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null :
                cb.lessThanOrEqualTo(root.get("dueDate"), date);
    }

    public static Specification<Task> dueAfter(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null :
                cb.greaterThanOrEqualTo(root.get("dueDate"), date);
    }

    public static Specification<Task> containsText (String search) {
        return (root,query, cb) -> {
        if(search==null || search.trim().isEmpty()) 
            return null;
        String pattern = "%" + search.toLowerCase() + "%";
        return cb.or(
            cb.like(cb.lower(root.get("title")), pattern),
            cb.like(cb.lower(root.get("description")), pattern)
        );
    };

}
}