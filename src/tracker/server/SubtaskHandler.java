package tracker.server;

import com.sun.net.httpserver.HttpExchange;
import tracker.exception.NotFoundException;
import tracker.model.Subtask;
import tracker.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;

public class SubtaskHandler extends BaseHttpHandler {
    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handleGet(HttpExchange exchange) throws IOException {
        String[] urlParts = exchange.getRequestURI().getPath().split("/");
        if (urlParts[1].equals("subtasks")) {
            try {
                String response = "";
                if (urlParts.length == 2) {
                    response = gson.toJson(taskManager.getSubtask());
                }
                if (urlParts.length == 3) {
                    int taskId = Integer.parseInt(urlParts[2]);
                    if (taskId > 0 && taskManager.searchSubtaskById(taskId) != null) {
                        response = gson.toJson(taskManager.searchSubtaskById(taskId));
                    } else {
                        sendNotFound(exchange, "Подзадача " + taskId + " не найдена");
                    }
                }
                sendSuccess(exchange, response);
            } catch (NotFoundException e) {
                sendNotFound(exchange, "Нет такой подзадача");
            }
        }
    }

    @Override
    public void handlePost(HttpExchange exchange) throws IOException {
        String[] urlParts = exchange.getRequestURI().getPath().split("/");
        if (urlParts[1].equals("subtasks")) {
            try {
                InputStream inputStream = exchange.getRequestBody();
                String subtaskString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Subtask subtask = gson.fromJson(subtaskString, Subtask.class);
                if (urlParts.length == 2) {
                    taskManager.addSubtask(subtask);
                    sendSuccessUpdate(exchange, "Новая подзадача создана");
                }
                if (urlParts.length == 3) {
                    int taskId = Integer.parseInt(urlParts[2]);
                    if (taskId > 0 && taskManager.searchSubtaskById(taskId) != null) {
                        taskManager.updateSubtask(subtask);
                        sendSuccessUpdate(exchange, "Подзадача " + taskId + " успешно обновлена");
                    } else {
                        sendNotFound(exchange, "Подзадача " + taskId + " не найдена");
                    }
                }
            } catch (NotFoundException e) {
                sendNotFound(exchange, "Нет такой подзадачи");
            } catch (DateTimeException e) {
                sendHasInteractions(exchange, "В эти даты уже запланирована другая задача");
            }
        }
    }

    @Override
    public void handleDelete(HttpExchange exchange) throws IOException {
        String[] urlParts = exchange.getRequestURI().getPath().split("/");
        if (urlParts[1].equals("subtasks")) {
            try {
                if (urlParts.length == 2) {
                    taskManager.clearSubtasks();
                    sendSuccess(exchange, "Все подзадачи удалены");
                }
                if (urlParts.length == 3) {
                    int taskId = Integer.parseInt(urlParts[2]);
                    if (taskId > 0 && taskManager.searchSubtaskById(taskId) != null) {
                        taskManager.deleteSubtaskById(taskId);
                        sendSuccess(exchange, "Подзадача " + taskId + " удалена");
                    } else {
                        sendNotFound(exchange, "Подзадача " + taskId + " не найдена");
                    }
                }
            } catch (NotFoundException e) {
                sendNotFound(exchange, "Нет такой подзадачи");
            }
        }
    }
}
