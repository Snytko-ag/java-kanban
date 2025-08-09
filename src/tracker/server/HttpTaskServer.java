package tracker.server;

import com.sun.net.httpserver.HttpServer;
import tracker.service.Managers;
import tracker.service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        try {
            // создаём и сразу привязываем HTTP-сервер к порту
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            // связываем конкретный путь и его обработчик
            httpServer.createContext("/tasks", new TaskHandler(taskManager));
            httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
            httpServer.createContext("/epics", new EpicHandler(taskManager));
            httpServer.createContext("/history", new HistoryHandler(taskManager));
            httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));

        } catch (IOException exception) {
            throw new RuntimeException("Ошибка создания сервера на порту " + PORT);
        }
    }

    public void startServer() {
        httpServer.start();
        System.out.printf("Сервер запущен на %s порту ", PORT);
    }

    public void stopServer() {
        httpServer.stop(1);
        System.out.printf("Сервер остановлен на %s порту ", PORT);
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        taskServer.startServer();
        taskServer.stopServer();
    }
}
