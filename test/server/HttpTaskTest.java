package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.server.BaseHttpHandler;
import tracker.server.HttpTaskServer;

import tracker.service.Managers;
import tracker.service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class HttpTaskTest {

    protected TaskManager manager = Managers.getDefault();

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    HttpTaskServer httpTaskServer;
    Gson gson = BaseHttpHandler.getGson();
    String url = "http://localhost:8080";

    @BeforeEach
    void beforeEach() throws IOException {
        httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.startServer();
        Task task1 = new Task("задача1", "собрать коробки", LocalDateTime.parse("20.07.2025 00:00", dateTimeFormatter), Duration.ofMinutes(100L));
        Task task2 = new Task("задача2", "собрать книги", LocalDateTime.parse("21.07.2025 00:00", dateTimeFormatter), Duration.ofMinutes(100L));
        Epic epic1 = new Epic("epic1", "перезд");
        Epic epic2 = new Epic("epic2", "регистрация");
        manager.addTasks(task1);//1
        manager.addTasks(task2);//2
        manager.addEpics(epic1);//3
        manager.addEpics(epic2);//4
        Subtask subtask2 = new Subtask("подзадача2", "погрузить вещи", epic1.getIdTask(),LocalDateTime.parse("23.07.2025 00:00", dateTimeFormatter), Duration.ofMinutes(10L));
        manager.addSubtask(subtask2);//6
    }

    @AfterEach
    void afterEach() {
        httpTaskServer.stopServer();
    }

    @Test
    public void shouldReturnAllTasks() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI urlTask = URI.create(url+"/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                                 .uri(urlTask)
                                 .GET()
                                 .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        assertNotNull(tasks, "Не выводятся задачи");
        assertEquals(manager.getTasks(), tasks, "Некорректное количество задач");
        assertEquals(200, response.statusCode(), "Не совпадает статус код");
    }

    @Test
    public void shouldCreateNewTask() throws IOException, InterruptedException {
        Task task3 = new Task("задача3", "собрать статуэтки", LocalDateTime.parse("25.07.2025 00:00", dateTimeFormatter), Duration.ofMinutes(100L));
        String taskJson = gson.toJson(task3);
        HttpClient client = HttpClient.newHttpClient();
        URI urlTask = URI.create(url+"/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                                .uri(urlTask)
                                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Не совпадает статус код");
        assertEquals(3, manager.getTasks().size(), "Список задач не корректный");
    }

    @Test
    public void shouldDeleteTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI urlTask = URI.create(url + "/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                                 .uri(urlTask)
                                 .DELETE()
                                 .build();
        assertEquals(2, manager.getTasks().size(), "Список задач не корректный");
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Статусы ответа не совпадает");
        assertEquals(1, manager.getTasks().size(), "Список задач не корректный");
    }

    @Test
    public void shouldReturnEpicById() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI urlEpic = URI.create(url+"/epics/3");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(urlEpic)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic = gson.fromJson(response.body(), new TypeToken<Epic>() {}.getType());
        assertEquals(manager.searchEpicById(3), epic, "Эпики не совпадают");
        assertEquals(200, response.statusCode(), "Не совпадает статус код");
    }


}
