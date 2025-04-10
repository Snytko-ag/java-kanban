package service;

import org.junit.jupiter.api.Test;
import tracker.service.Managers;
import tracker.service.TaskManager;
import tracker.service.HistoryManager;
import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void shouldBeHistoryManagerCreated() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);
    }

    @Test
    void shouldBeInMemoryTaskManagerCreated() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);
    }
}