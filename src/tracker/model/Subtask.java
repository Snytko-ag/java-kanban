package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }
    public Subtask(String name, String description, int epicId, LocalDateTime dateStart, Duration duration) {
        super(name, description, dateStart, duration);
        this.epicId = epicId;
    }

    public Subtask(int id,  String name, String description, int epicId, LocalDateTime dateStart, Duration duration) {
        super(id, name, description, dateStart, duration);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, StatusTask status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, StatusTask status, LocalDateTime dateStart, Duration duration, int epicId) {
        super(id, name, description, status, dateStart, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TypeTask getTypeTask() {
        return TypeTask.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "dataStart=" + getDateStart() +
                ", duration=" + getDuration()+
                ", epicId=" + epicId +

                "} " + super.toString();
    }
}