package service;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.service.Managers;
import tracker.service.TaskManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

class TaskManagerTest {

    protected TaskManager manager = Managers.getDefault();

    @BeforeEach
    void beforeEach() {
        Task task1 = new Task("задача1", "собрать коробки");
        Task task2 = new Task("задача2", "собрать книги");
        Epic epic1 = new Epic("epic1", "перезд");
        Epic epic2 = new Epic("epic2", "регистрация");
        manager.addTasks(task1);
        manager.addTasks(task2);
        manager.addEpics(epic1);
        manager.addEpics(epic2);
        Subtask subtask1 = new Subtask("подзадача1", "собрать вещи", epic1.getIdTask());
        Subtask subtask2 = new Subtask("подзадача2", "погрузить вещи", epic1.getIdTask());
        Subtask subtask3 = new Subtask("подзадача3", "найти документы", epic2.getIdTask());
        Subtask subtask4 = new Subtask("подзадача4", "сходить в паспортный стол", epic2.getIdTask());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
    }

    @Test
    public void shouldBeListOfAllTasks() {
        ArrayList<Task> allTasks = manager.getTasks();
        assertEquals(2, allTasks.size());
    }

    @Test
    void shouldBeListOfAllEpics() {
        ArrayList<Epic> epics = manager.getEpics();
        assertEquals(2, epics.size());
    }

    @Test
    void shouldBeListOfAllSubtasks() {
        ArrayList<Subtask> subtasks = manager.getSubtask();
        assertEquals(4, subtasks.size());
    }

    @Test
    void shouldBeTaskWithId1() {
        Task task = manager.searchTaskById(1);
        assertNotNull(task, "Задача отсутствует");
        assertEquals(1, task.getIdTask());
    }

    @Test
    void shouldBeSubtaskWithId5() {
        Subtask subtask1 = manager.searchSubtaskById(5);
        assertNotNull(subtask1, "Подзадача отсутствует");
    }

    @Test
    void shouldBeEpicWithId3() {
        Epic epic1 = manager.searchEpicById(3);
        assertNotNull(epic1, "Эпик отсутствует");
    }

    @Test
    void shouldBeSubtasksByEpicId3() {
        Epic epic1 = manager.searchEpicById(3);
        ArrayList<Subtask> listSubtasks  = manager.getSubtaskByEpic(epic1.getIdTask());
        assertEquals(2, listSubtasks.size());
    }

    @Test
    void shouldBeRemovedTaskById() {
        Task task = manager.searchTaskById(1);
        assertEquals(1, task.getIdTask());
        manager.deleteTaskById(1);
        assertNull(manager.searchTaskById(1));
    }

    @Test
    void shouldBeRemovedSubtaskById() {
        Subtask subtask1 = manager.searchSubtaskById(5);
        assertEquals(5, subtask1.getIdTask());
        manager.deleteSubtaskById(5);
        assertNull(manager.searchSubtaskById(5));
    }

    @Test
    void shouldBeRemovedEpicById() {
        Epic epic = manager.searchEpicById(4);
        List<Subtask> epicSubtasks = manager.getSubtaskByEpic(epic.getIdTask());
        assertEquals(2, epicSubtasks.size());
        manager.deleteEpicById(4);
        assertNull(manager.searchEpicById(4));
        //проверяем что все подзадачи тоже удалились
        List<Subtask> epicSubtasksDel = manager.getSubtaskByEpic(epic.getIdTask());
        assertEquals(0, epicSubtasksDel.size());
    }

    @Test
    void shouldBeRemovedAllTasks() {
        manager.clearTasks();
        assertEquals(0, manager.getTasks().size());
    }

    @Test
    void shouldBeRemovedAllSubtasks() {
        manager.clearSubtasks();
        assertEquals(0, manager.getSubtask().size());
    }

    @Test
    void shouldBeRemovedAllEpics() {
        assertEquals(2, manager.getEpics().size());
        assertEquals(4, manager.getSubtask().size());
        manager.clearEpics();
        assertEquals(0, manager.getEpics().size());
        assertEquals(0, manager.getSubtask().size());
    }



}
