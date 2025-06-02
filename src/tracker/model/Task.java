package tracker.model;

import java.util.Objects;

public class Task {
    private  int id;
    private final String name;
    private final String description;
    private StatusTask status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = StatusTask.NEW;
    }

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Task(int id, String name, String description, StatusTask status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }



    public int getIdTask() {
        return id;
    }

    public void setIdTask(int id) {
        this.id = id;
    }

    public StatusTask getStatusTask() {
        return status;
    }

    public String getNameTask() {
        return name;
    }

    public String getDescriptionTask() {
        return description;
    }

    public void setStatusTask(StatusTask status) {
        this.status = status;
    }

    public TypeTask getTypeTask() {
        return TypeTask.TASK;
    }

    @Override
    public String toString() {
        return "tasktracker.model.model.Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }
}
