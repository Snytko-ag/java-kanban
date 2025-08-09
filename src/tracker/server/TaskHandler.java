package tracker.server;

import com.sun.net.httpserver.HttpExchange;
import tracker.exception.NotFoundException;
import tracker.exception.TaskConflictException;
import tracker.model.Task;
import tracker.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handleGet(HttpExchange exchange) throws IOException {
        String[] urlParts = exchange.getRequestURI().getPath().split("/");
        if (urlParts[1].equals("tasks")) {
            try {
                String response = "";
                if (urlParts.length == 2) {
                    response = gson.toJson(taskManager.getTasks());
                    sendSuccess(exchange, response);
                }
                if (urlParts.length == 3) {
                    int taskId = Integer.parseInt(urlParts[2]);
                    if (taskId > 0 && taskManager.searchTaskById(taskId) != null) {
                        response = gson.toJson(taskManager.searchTaskById(taskId));
                        sendSuccess(exchange, response);
                    } else {
                        String result = String.format("Задача %s не найдена", taskId);
                        sendNotFound(exchange, result);
                    }
                }
            } catch (NotFoundException e) {
                sendNotFound(exchange, "Нет такой задачи");
            }
        }
    }

    @Override
    public void handlePost(HttpExchange exchange) throws IOException {
        String[] urlParts = exchange.getRequestURI().getPath().split("/");
        if (urlParts[1].equals("tasks")) {
            try {
                InputStream inputStream = exchange.getRequestBody();
                String taskString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Task task = gson.fromJson(taskString, Task.class);
                if (urlParts.length == 2) {
                    taskManager.addTasks(task);
                    sendSuccessUpdate(exchange, "Новая задача создана");
                }
                if (urlParts.length == 3) {
                    int taskId = Integer.parseInt(urlParts[2]);

                    if (taskId > 0 && taskManager.searchTaskById(taskId) != null) {
                        taskManager.updateTask(task);
                        String result = String.format("Задача %s успешно обновлена", taskId);
                        sendSuccessUpdate(exchange, result);
                    } else {
                        String result = String.format("Задача %s не найдена", taskId);
                        sendNotFound(exchange, result);
                    }
                }
            } catch (NotFoundException e) {
                sendNotFound(exchange, "Нет такой задачи");
            } catch (TaskConflictException e) {
                sendHasInteractions(exchange, "В эти даты уже запланирована другая задача");
            }
        }
    }

    @Override
    public void handleDelete(HttpExchange httpExchange) throws IOException {
        String[] urlParts = httpExchange.getRequestURI().getPath().split("/");
        if (urlParts[1].equals("tasks")) {
            try {
                if (urlParts.length == 2) {
                    taskManager.clearTasks();
                    sendSuccess(httpExchange, "Все задачи удалены");
                }
                if (urlParts.length == 3) {
                    int taskId = Integer.parseInt(urlParts[2]);
                    if (taskId > 0 && taskManager.searchTaskById(taskId) != null) {
                        taskManager.deleteTaskById(taskId);
                        String result = String.format("Задача %s удалена", taskId);
                        sendSuccess(httpExchange, result);
                    } else {
                        String result = String.format("Задача %s не найдена", taskId);
                        sendNotFound(httpExchange, result);
                    }
                }
            } catch (NotFoundException e) {
                sendNotFound(httpExchange, "Нет такой задачи");
            }
        }
    }
}