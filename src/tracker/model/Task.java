package tracker.model;

public class Task {
    private /*final*/ int id;
    private final String name;
    private final String description;
    private StatusTask status;

   // private int countId = 0;
    //Генерирует уникальный ID
    /*public int getNewId() {
        countId++;
        return countId;
    }*/

    public Task( String name, String description) {
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

    @Override
    public String toString() {
        return "tasktracker.model.model.Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
