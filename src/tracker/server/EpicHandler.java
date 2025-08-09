package tracker.server;

import com.sun.net.httpserver.HttpExchange;
import tracker.exception.NotFoundException;
import tracker.model.Epic;
import tracker.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handleGet(HttpExchange exchange) throws IOException {
        String[] urlParts = exchange.getRequestURI().getPath().split("/");
        if (urlParts[1].equals("epics")) {
            try {
                String response = "";
                if (urlParts.length == 2) {
                    response = gson.toJson(taskManager.getEpics());
                    sendSuccess(exchange, response);
                }
                if (urlParts.length == 3) {
                    int taskId = Integer.parseInt(urlParts[2]);
                    if (taskId > 0 && taskManager.searchEpicById(taskId) != null) {
                        response = gson.toJson(taskManager.searchEpicById(taskId));
                        sendSuccess(exchange, response);
                    } else {
                        String result = String.format("Эпик %s не найден", taskId);
                        sendNotFound(exchange, result);
                    }
                }
            } catch (NotFoundException e) {
                sendNotFound(exchange, "Нет такого Эпика");
            }
        }
    }

    @Override
    public void handlePost(HttpExchange exchange) throws IOException {
        String[] urlParts = exchange.getRequestURI().getPath().split("/");
        if (urlParts[1].equals("epics")) {
            try {
                InputStream inputStream = exchange.getRequestBody();
                String epicString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Epic epic = gson.fromJson(epicString, Epic.class);
                if (urlParts.length == 2) {
                    taskManager.addEpics(epic);
                    sendSuccessUpdate(exchange, "Новый Эпик создан");
                }
                if (urlParts.length == 3) {
                    int taskId = Integer.parseInt(urlParts[2]);
                    if (taskId > 0 && taskManager.searchEpicById(taskId) != null) {
                        taskManager.updateEpic(epic);
                        String result = String.format("Эпик %s успешно обновлен", taskId);
                        sendSuccessUpdate(exchange, result);
                    } else {
                        String result = String.format("Эпик %s не найден", taskId);
                        sendNotFound(exchange, result);
                    }
                }
            } catch (NotFoundException e) {
                sendNotFound(exchange, "Эпик не найден");
            } catch (DateTimeException e) {
                sendHasInteractions(exchange, "В эти даты уже запланирована другая задача");
            }
        }
    }

    @Override
    public void handleDelete(HttpExchange exchange) throws IOException {
        String[] urlParts = exchange.getRequestURI().getPath().split("/");
        if (urlParts[1].equals("epics")) {
            try {
                if (urlParts.length == 2) {
                    taskManager.clearEpics();
                    sendSuccess(exchange, "Все эпики удалены");
                }
                if (urlParts.length == 3) {
                    int taskId = Integer.parseInt(urlParts[2]);
                    if (taskId > 0 && taskManager.searchEpicById(taskId) != null) {
                        taskManager.deleteEpicById(taskId);
                        String result = String.format("Эпик %s удален", taskId);
                        sendSuccess(exchange, result);
                    }  else {
                        String result = String.format("Эпик %s не найден", taskId);
                        sendNotFound(exchange, result);
                    }
                }
            } catch (NotFoundException e) {
                sendNotFound(exchange, "Нет такого Эпика");
            }
        }
    }
}
