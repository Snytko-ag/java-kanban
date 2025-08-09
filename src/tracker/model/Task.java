package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected  int id;
    protected String name;
    protected String description;
    protected StatusTask status;
    protected Duration duration;
    protected LocalDateTime dateStart;
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = StatusTask.NEW;
    }

    public Task(String name, String description, LocalDateTime dateStart, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = StatusTask.NEW;
        this.dateStart = dateStart;
        this.duration = duration;
    }

    public Task(int id, String name, String description, LocalDateTime dateStart, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateStart = dateStart;
        this.duration = duration;
    }

    public Task(int id, String name, String description, StatusTask status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(int id, String name, String description, StatusTask status, LocalDateTime dateStart, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.dateStart = dateStart;
        this.duration = duration;
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

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getDateEnd() {

        if (dateStart != null && duration != null) {
            return dateStart.plus(duration);
        } else return  null;
    }

    public static <T extends Task> int compareTime(T a, T b) {
        return a.getDateStart().isBefore(b.getDateStart()) ? -1 : 1;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", dateStart=" + dateStart +
                ", duration=" + duration +
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
