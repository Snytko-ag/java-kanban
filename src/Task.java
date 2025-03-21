public class Task {
    private final int id;
    private final String name;
    private final String description;
    private StatusTask status;

    public Task(int id, String name, String description, StatusTask status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getIdTask() {
        return id;
    }

    public StatusTask getStatusTask() {
        return status;
    }

    public void setStatusTask(StatusTask status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
