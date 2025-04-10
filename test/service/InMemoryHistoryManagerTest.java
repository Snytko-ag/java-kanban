package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.model.StatusTask;
import tracker.service.TaskManager;
import tracker.service.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class InMemoryHistoryManagerTest {

    private TaskManager manager = Managers.getDefault();

    @BeforeEach
    void beforeEach() {
        Task task1 = new Task("задача1", "собрать коробки");
        Epic epic1 = new Epic("epic1", "перезд");
        manager.addTasks(task1);
        manager.addEpics(epic1);
        Subtask subtask1 = new Subtask("подзадача1", "собрать вещи", epic1.getIdTask());
        manager.addSubtask(subtask1);
    }

    @Test
    void shouldBeTasksAddedToHistory() {
        manager.searchTaskById(1);
        manager.searchEpicById(2);
        manager.searchSubtaskById(3);

        final List<Task> history = manager.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(3, history.size(), "Не верное количество записей в истории");
        assertEquals("задача1", history.get(0).getNameTask(), "Наименование не совпадает.");
        assertEquals("перезд", history.get(1).getDescriptionTask(), "Описание не совпадает.");

    }
}
