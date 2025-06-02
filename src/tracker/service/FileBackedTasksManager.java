package tracker.service;
import java.io.*;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import tracker.exception.ManagerSaveException;
import tracker.model.Subtask;
import tracker.model.Task;

import java.io.IOException;
import java.io.FileNotFoundException;
import tracker.model.*;


public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager   {

    protected Path path;

    public FileBackedTasksManager ( Path path) {
        super();
        this.path = path;
    }

    private void save()  {
        try (FileWriter fileWriter = new FileWriter(path.toString())){
            fileWriter.write("id,type,name,status,description,epic\n");
            List<Task> fileTask = new ArrayList<>();
            fileTask.addAll(getTasks());
            fileTask.addAll(getEpics());
            fileTask.addAll(getSubtask());

            for (Task task : fileTask) {
                fileWriter.write(toString(task));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи файла");
        }
    }

    public static FileBackedTasksManager  loadFromFile(Path path)  {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(Paths.get(path.toString()));
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path.getFileName().toString()))) {
            while (bufferedReader.ready()){
                String line = bufferedReader.readLine();
                if (line.startsWith("id") || line.isEmpty()) {
                    continue;
                }
                Task task = fileBackedTasksManager.fromString(line);
                TypeTask typeTask = task.getTypeTask();

                switch (typeTask) {
                    case TASK:
                        fileBackedTasksManager.addTasks(task);
                        break;
                    case EPIC:
                        fileBackedTasksManager.addEpics((Epic) task);
                        break;
                    case SUBTASK:
                        fileBackedTasksManager.addSubtask((Subtask) task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении из файла", e);
        }

        return fileBackedTasksManager;
    }

    private  String toString(Task task){

        String epicId = " ";

        if (task.getTypeTask().equals(TypeTask.SUBTASK)) {
            epicId = String.valueOf(((Subtask) task).getEpicId());

        }

        return task.getIdTask() + ","
             + task.getTypeTask() + ","
             + task.getNameTask() + ","
             + task.getDescriptionTask() + ","
             + task.getStatusTask() + ","

             +  epicId + "\n";
    }

    private Task fromString(String value) {
        String[] columns = value.split(",");
        String idTask = String.valueOf(columns[0]);
        TypeTask typeTask = TypeTask.valueOf(columns[1]);
        String nameTask = String.valueOf(columns[2]);
        String descriptionTask = String.valueOf(columns[3]);
        StatusTask statusTask = StatusTask.valueOf(columns[4]);
        String epicId = String.valueOf(columns[5]);

        Task task;

        switch (typeTask) {
            case TASK:
                task = new Task(Integer.parseInt(idTask), nameTask, descriptionTask, statusTask);
                break;
            case EPIC:
                 task = new Epic(Integer.parseInt(idTask), nameTask, descriptionTask, statusTask);
                 break;
            case SUBTASK:
                 task = new Subtask(Integer.parseInt(idTask), nameTask, descriptionTask, statusTask, Integer.parseInt(epicId));
                 break;
            default:
                throw new ManagerSaveException("Не определенный тип: " + typeTask);
        }

        return task;
    }

    //>>>>>>> ЗАДАЧИ >>>>>>>>>>>>>>>>
    @Override
    public void addTasks(Task taskIn) {
        super.addTasks(taskIn);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void updateTask(Task taskIn) {
        super.updateTask(taskIn);
        save();
    }

    //>>>>>>> ЭПИКИ >>>>>>>>>>>>>>>>
    @Override
    public void addEpics(Epic taskIn) {
        super.addEpics(taskIn);
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void updateEpic(Epic taskIn) {
        super.updateEpic(taskIn);
        save();
    }

    //>>>>>>> ПОДЗАДАЧИ >>>>>>>>>>>>>>>>
    @Override
    public void addSubtask(Subtask taskIn) {
        super.addSubtask(taskIn);
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void updateSubtask(Subtask taskIn) {
        super.updateSubtask(taskIn);
        save();
    }

}
