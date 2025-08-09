package tracker.server;

import com.sun.net.httpserver.HttpExchange;
import tracker.exception.NotFoundException;
import tracker.service.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {
    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handleGet(HttpExchange httpExchange) throws IOException {
        try {
            String prioritized = gson.toJson(taskManager.getPrioritizedTasks());
            sendSuccess(httpExchange, prioritized);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange, "Отсортированный список не найден");
        }
    }
}
