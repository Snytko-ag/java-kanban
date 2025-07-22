package tracker.service;
import java.io.*;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import tracker.exception.ManagerSaveException;
import tracker.model.Subtask;
import tracker.model.Task;

import java.io.IOException;
import java.util.Objects;

import tracker.model.*;

import static tracker.model.Task.dateTimeFormatter;


public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager  {

    protected Path path;

    public FileBackedTasksManager(Path path) {
        super();
        this.path = path;
    }

    private void save()  {
        try (FileWriter fileWriter = new FileWriter(path.toString())) {
            fileWriter.write("id,type,name,status,description,dateStart,duration,epic\n");
            List<Task> fileTask = new ArrayList<>();
            fileTask.addAll(getTasks());
            fileTask.addAll(getEpics());
            fileTask.addAll(getSubtask());

            fileTask.forEach(task -> {
                try {

                    fileWriter.write(toString(task));
                } catch (IOException e) {
                    throw new ManagerSaveException("Ошибка при записи файла");
                }
            });

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи файла");
        }
    }

    public static FileBackedTasksManager  loadFromFile(Path path)  {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(Paths.get(path.toString()));
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path.getFileName().toString()))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.startsWith("id") || line.isEmpty()) {
                    continue;
                }
                Task task = fileBackedTasksManager.fromString(line);
                TypeTask typeTask = task.getTypeTask();

                switch (typeTask) {
                    case TASK    -> fileBackedTasksManager.addTasks(task);
                    case EPIC    ->  fileBackedTasksManager.addEpics((Epic) task);
                    case SUBTASK -> fileBackedTasksManager.addSubtask((Subtask) task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении из файла", e);
        }

        return fileBackedTasksManager;
    }

    private  String toString(Task task) {

        String epicId = "";
        String dateStart = "";
        String duration = "";

        if (task.getTypeTask().equals(TypeTask.SUBTASK)) {
            epicId = String.valueOf(((Subtask) task).getEpicId());
        }

        if (task.getDateStart() != null) {
            dateStart = task.getDateStart().format(dateTimeFormatter);
        }

        if (task.getDuration() != null) {
            duration = String.valueOf(task.getDuration().toMinutes());

        }

        return task.getIdTask() + ","
             + task.getTypeTask() + ","
             + task.getNameTask() + ","
             + task.getStatusTask() + ","
             + task.getDescriptionTask() + ","
             + dateStart + ","
             + duration + ","
             + epicId + "\n";
    }

    private Task fromString(String value) {
        String[] columns = value.split(",");
        String idTask = String.valueOf(columns[0]);
        TypeTask typeTask = TypeTask.valueOf(columns[1]);
        String nameTask = String.valueOf(columns[2]);
        StatusTask statusTask = StatusTask.valueOf(columns[3]);
        String descriptionTask = String.valueOf(columns[4]);

        LocalDateTime dateStart = null;
        Duration duration = null;
        if (columns.length > 5 && !Objects.equals(columns[5], "")) {
            dateStart = LocalDateTime.parse(columns[5], dateTimeFormatter);
        }
        if (columns.length > 7 && !Objects.equals(columns[6], "")) {
            duration = Duration.ofMinutes(Integer.parseInt(columns[6]));
        }

        Task task;

        switch (typeTask) {
            case TASK -> {
                task = new Task(Integer.parseInt(idTask), nameTask, descriptionTask, statusTask, dateStart, duration);
            }
            case EPIC -> {
                task = new Epic(Integer.parseInt(idTask), nameTask, descriptionTask, statusTask);
            }
            case SUBTASK -> {
                task = new Subtask(Integer.parseInt(idTask), nameTask, descriptionTask, statusTask, Integer.parseInt(columns[7]));
            }
            default -> {
                throw new ManagerSaveException("Не определенный тип: " + typeTask);
            }
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
