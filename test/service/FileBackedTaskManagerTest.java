package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.*;
import tracker.service.FileBackedTasksManager;
import tracker.service.TaskManager;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest  {

    Path tempFile;

    @BeforeEach
    void beforeEach() throws IOException {
        tempFile = Paths.get("test2.csv");
    }

    @Test
    void creationFile() {
        TaskManager manager = new FileBackedTasksManager(tempFile);
        assertTrue(Files.exists(tempFile));
    }

    @Test
    void loadFromFile() {
        TaskManager manager = new FileBackedTasksManager(tempFile);
        Task task1 = new Task("задача1", "собрать коробки");
        Epic epic1 = new Epic("epic1", "перезд");
        manager.addTasks(task1);
        manager.addEpics(epic1);
        Subtask subtask1 = new Subtask("подзадача1", "собрать вещи", epic1.getIdTask());
        manager.addSubtask(subtask1);

        TaskManager manager2 = FileBackedTasksManager.loadFromFile(tempFile);

        assertEquals(TypeTask.TASK, manager2.searchTaskById(1).getTypeTask());
        assertEquals("epic1", manager2.searchEpicById(2).getNameTask());
        assertEquals(StatusTask.NEW, manager2.searchSubtaskById(3).getStatusTask());
        assertTrue(Files.exists(tempFile));
    }

}
