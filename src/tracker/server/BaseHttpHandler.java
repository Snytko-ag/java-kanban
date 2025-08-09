package tracker.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.Adapter.DurationAdapter;
import tracker.Adapter.LocalDateTimeAdapter;
import tracker.service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler implements HttpHandler {
    protected TaskManager taskManager;
    protected Gson gson;

    public static Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = BaseHttpHandler.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String requestMethod = exchange.getRequestMethod();

            switch (requestMethod) {
                case "GET": {
                    handleGet(exchange);
                    break;
                }
                case "POST": {
                    handlePost(exchange);
                    break;
                }
                case "DELETE": {
                    handleDelete(exchange);
                    break;
                }
                default: {
                    sendNotFound(exchange, "Такого эндпоинта не существует");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGet(HttpExchange httpExchange) throws IOException {
        sendNotAllowed(httpExchange);
    }

    public void handlePost(HttpExchange httpExchange) throws IOException {
        sendNotAllowed(httpExchange);
    }

    public void handleDelete(HttpExchange httpExchange) throws IOException {
        sendNotAllowed(httpExchange);
    }

    protected void sendSuccess(HttpExchange exchange, String text) throws IOException {
        sendText(exchange, text, 200);
    }

    protected void sendSuccessUpdate(HttpExchange exchange, String text) throws IOException {
        sendText(exchange, text, 201);
    }

    protected void sendNotFound(HttpExchange exchange, String text) throws IOException {
        sendText(exchange, text, 404);
    }

    protected void sendHasInteractions(HttpExchange exchange, String text) throws IOException {
        sendText(exchange, text, 406);
    }

    protected void sendNotAllowed(HttpExchange exchange) throws IOException {
        sendText(exchange, "Доступ не разрешен", 405);
    }

    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {

        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
        exchange.close();
    }
}
