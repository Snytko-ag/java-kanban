package tracker.model;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, String name, String description, StatusTask status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "tasktracker.model.model.Subtask{" +
                "epicId=" + epicId +
                "} " + super.toString();
    }
}