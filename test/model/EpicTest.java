package model;
import tracker.model.Epic;
import tracker.model.StatusTask;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.service.Managers;
import tracker.service.TaskManager;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EpicTest {
    TaskManager taskManager = Managers.getDefault();

    //проверяем что все новые задачи создаются в статусе NEW
    @Test
    void taskCreatedWithStatusNew() {

        Task task1 = new Task("задача1", "собрать коробки");
        Epic epic1 = new Epic("epic1", "перезд");

        taskManager.addTasks(task1);
        taskManager.addEpics(epic1);
        Subtask subtask1 = new Subtask("подзадача1", "собрать вещи", epic1.getIdTask());
        taskManager.addSubtask(subtask1);

        StatusTask statusTask = task1.getStatusTask();
        StatusTask statusEpic = epic1.getStatusTask();
        StatusTask statusSubtask = subtask1.getStatusTask();
        assertEquals(StatusTask.NEW, statusTask, "Статус задачи не верный");
        assertEquals(StatusTask.NEW, statusEpic, "Статус Эпика не верный");
        assertEquals(StatusTask.NEW, statusSubtask, "Статус подзадачи не верный");
    }

    //проверяем что эпик меняет статус на DONE, когда все задачи имеют статус DONE
    @Test
    public void epicHasDoneStatusWhenAllSubTasksAreDone() {
        Epic epic1 = new Epic("epic1", "перезд");
        taskManager.addEpics(epic1);
        Subtask subtask1 = new Subtask("подзадача1", "собрать вещи", epic1.getIdTask());
        Subtask subtask2 = new Subtask("подзадача2", "погрузить вещи", epic1.getIdTask());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        taskManager.updateSubtask(new Subtask(subtask1.getIdTask(), "подзадача1", "собрать вещи", StatusTask.DONE, epic1.getIdTask()));
        taskManager.updateSubtask(new Subtask(subtask2.getIdTask(), "подзадача2", "погрузить вещи", StatusTask.DONE, epic1.getIdTask()));
        assertEquals(StatusTask.DONE, epic1.getStatusTask(), "Статус Эпика не верный");
    }

    //проверяем что эпик меняет статус на IN_PROGRESS, когда хоть одна подзадача переходит в статус IN_PROGRESS
    @Test
    public void epicHasInProgressStatusWhenAllSubTasksAreInProgress() {
        Epic epic1 = new Epic("epic1", "перезд");
        taskManager.addEpics(epic1);
        Subtask subtask1 = new Subtask("подзадача1", "собрать вещи", epic1.getIdTask());
        Subtask subtask2 = new Subtask("подзадача2", "погрузить вещи", epic1.getIdTask());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        taskManager.updateSubtask(new Subtask(subtask1.getIdTask(), "подзадача1", "собрать вещи", StatusTask.DONE, epic1.getIdTask()));
        taskManager.updateSubtask(new Subtask(subtask2.getIdTask(), "подзадача2", "погрузить вещи", StatusTask.IN_PROGRESS, epic1.getIdTask()));
        assertEquals(StatusTask.IN_PROGRESS, epic1.getStatusTask(), "Статус Эпика не верный");
    }

}