import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tracker.Adapter.DurationAdapter;
import tracker.Adapter.LocalDateTimeAdapter;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.service.FileBackedTasksManager;

import tracker.service.*;


import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import static tracker.model.Task.dateTimeFormatter;


public class Main {

    public static void main(String[] args) {

        TaskManager manager = new FileBackedTasksManager(Paths.get("test.csv"));


        Task task1 = new Task("задача1", "собрать коробки", LocalDateTime.parse("20.07.2025 00:00", dateTimeFormatter), Duration.ofMinutes(100L));
        Task task2 = new Task("задача2", "собрать книги",  LocalDateTime.parse("20.07.2026 01:00", dateTimeFormatter), Duration.ofMinutes(100L));

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        System.out.println(gson.toJson(task1));
        System.out.println(gson.toJson(task2));

        Epic epic1 = new Epic("epic1", "перезд");
        Epic epic2 = new Epic("epic2", "регистрация");



        manager.addTasks(task1);
        manager.addTasks(task2);

        manager.addEpics(epic1);
        manager.addEpics(epic2);

        Subtask subtask1 = new Subtask("подзадача1", "собрать вещи", epic1.getIdTask(),LocalDateTime.parse("21.07.2025 00:00", dateTimeFormatter), Duration.ofMinutes(10L));
        Subtask subtask2 = new Subtask("подзадача2", "погрузить вещи", epic1.getIdTask(),LocalDateTime.parse("22.07.2025 00:00", dateTimeFormatter), Duration.ofMinutes(10L));
        Subtask subtask22 = new Subtask("подзадача22", "найти документы22", epic1.getIdTask(),LocalDateTime.parse("22.07.2024 00:00", dateTimeFormatter), Duration.ofMinutes(10L));

        Subtask subtask3 = new Subtask("подзадача3", "найти документы", epic2.getIdTask());
        Subtask subtask4 = new Subtask("подзадача4", "сходить в паспортный стол", epic2.getIdTask());


        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask22);

        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);

        System.out.println("treeSet = " + manager.getPrioritizedTasks());

        System.out.println("<<<<<<<Получение списка всех задач>>>>>>>");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtask());

        System.out.println("<<<<<<<Получение списка всех подзадач определённого эпика>>>>>>>");
        System.out.println(manager.getSubtaskByEpic(epic2.getIdTask()));


        /*for (Task task : manager.getTasks()) {
            System.out.println("чтение из файла: " + task);
        }
        System.out.println("id = " + manager.searchTaskById(1).getTypeTask());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtask());

        */
    }
}

