package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import tracker.service.TaskManager;
import tracker.service.Managers;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class InMemoryHistoryManagerTest {

    private TaskManager manager = Managers.getDefault();

    @BeforeEach
    void beforeEach() {
        Task task1 = new Task("задача1", "собрать коробки");//id 1
        Epic epic1 = new Epic("epic1", "перезд");//id 2
        Epic epic2 = new Epic("epic2", "перезд2");//id 3
        manager.addTasks(task1);
        manager.addEpics(epic1);
        manager.addEpics(epic2);
        Subtask subtask1 = new Subtask("подзадача1", "собрать вещи", epic1.getIdTask());//id 4
        Subtask subtask2 = new Subtask("подзадача2", "собрать вещи2", epic1.getIdTask());//id 5
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask1);
    }

    @Test
    void addingTaskToHistory() {
        manager.searchTaskById(1);
        manager.searchEpicById(2);
        manager.searchSubtaskById(4);
        manager.searchSubtaskById(4);//проверяем, что нет дублей в истории

        final List<Task> history = manager.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(3, history.size(), "Неверное количество записей в истории");
        assertEquals("задача1", history.get(0).getNameTask(), "Наименование не совпадает.");
        assertEquals("перезд", history.get(1).getDescriptionTask(), "Описание не совпадает.");

    }

    @Test
    void removeTaskToHistory() {
        manager.searchTaskById(1);
        manager.searchEpicById(2);
        manager.searchEpicById(3);
        manager.searchSubtaskById(4);

        manager.deleteTaskById(1);
        manager.deleteEpicById(2);
        final List<Task> history = manager.getHistory();

        assertEquals(1, history.size(), "Неверное количество записей в истории");
    }

    @Test
    void removeAllTaskToHistory() {
        manager.searchTaskById(1);
        manager.searchEpicById(2);
        manager.searchEpicById(3);
        manager.searchSubtaskById(4);

        manager.clearTasks();
        manager.clearEpics();
        manager.clearSubtasks();
        final List<Task> history = manager.getHistory();

        assertEquals(0, history.size(), "Неверное количество записей в истории");
    }



}
