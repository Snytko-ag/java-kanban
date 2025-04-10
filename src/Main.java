import tracker.model.Epic;
import tracker.model.StatusTask;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.service.InMemoryHistoryManager;
import tracker.service.InMemoryTaskManager;
import tracker.service.Managers;
import tracker.service.TaskManager;

public class Main {

    public static void main(String[] args) {


        TaskManager manager = Managers.getDefault();



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

        System.out.println("<<<<<<<Получение списка всех задач>>>>>>>");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtask());

        System.out.println("<<<<<<<Получение по идентификатору>>>>>>>");
        System.out.println(manager.searchTaskById(task1.getIdTask()));
        System.out.println(manager.searchEpicById(epic2.getIdTask()));
        System.out.println(manager.searchSubtaskById(subtask3.getIdTask()));

        System.out.println("<<<<<<<Получение списка всех подзадач определённого эпика>>>>>>>");
        System.out.println(manager.getSubtaskByEpic(epic2.getIdTask()));

        System.out.println("<<<<<<<Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра>>>>>>>");
        //Task task3 = new Task(task2.getIdTask(), "задача2", "собрать книги",  StatusTask.DONE);
        //manager.updateTask(task3);
        //System.out.println(manager.searchTaskById(task2.getIdTask()));
        Epic epic3 = new Epic(epic1.getIdTask(), "epic1", "перезд обновленная версия",  StatusTask.NEW);
        manager.updateEpic(epic3);
        System.out.println(manager.searchEpicById(epic1.getIdTask()));
        Subtask subtask5 = new Subtask(subtask3.getIdTask(), "подзадача3", "найти документы",  StatusTask.DONE, epic2.getIdTask());
        Subtask subtask6 = new Subtask(subtask4.getIdTask(), "подзадача4", "сходить в паспортный стол",  StatusTask.IN_PROGRESS, epic2.getIdTask());
        manager.updateSubtask(subtask5);
        manager.updateSubtask(subtask6);
        //System.out.println(manager.searchSubtaskById(subtask3.getIdTask()));
        //System.out.println(manager.searchSubtaskById(subtask4.getIdTask()));
        System.out.println(manager.searchEpicById(subtask6.getEpicId()));


        System.out.println("<<<<<<<Удаление по идентификатору>>>>>>>");
        //проверяем что task1 есть
        System.out.println(manager.searchTaskById(task1.getIdTask()));
        //удаляем task1
        manager.deleteTaskById(task1.getIdTask());
        //проверяем, что нет task1
        System.out.println(manager.searchTaskById(task1.getIdTask()));
        //проверяем что есть epic1
        System.out.println(manager.searchEpicById(epic1.getIdTask()));
        //проверяем что есть подзадачи у epic1
        System.out.println(manager.getSubtaskByEpic(epic1.getIdTask()));
        //удаляем epic1
        manager.deleteEpicById(epic1.getIdTask());
        //проверяем что нет epic1
        System.out.println(manager.searchEpicById(epic1.getIdTask()));
        //проверяем что нет подзадач у epic1
        System.out.println(manager.getSubtaskByEpic(epic1.getIdTask()));
        //проверяем что есть subtask3
        System.out.println(manager.searchSubtaskById(subtask3.getIdTask()));
        //удаляем subtask3
        manager.deleteSubtaskById(subtask3.getIdTask());
        //проверяем что нет subtask3
        System.out.println(manager.searchSubtaskById(subtask3.getIdTask()));

        System.out.println("<<<<<<<Удаление всех задач>>>>>>>");
        manager.clearTasks();
        manager.clearEpics();
        manager.clearSubtasks();
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtask());

        System.out.println("history = " + manager.getHistory());

    }
}

