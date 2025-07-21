package tracker.service;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {


    //Добавляем задачу
    void addTasks(Task taskIn);

    ArrayList<Task> getTasks();

    //поиск по идентификатору задачи
    Task searchTaskById(int id);

    //Удаляем все задачи
    void clearTasks();

    //удаление задачи по идентификатору
    void deleteTaskById(int id);

    //обновление задачи
    void updateTask(Task taskIn);

    //>>>>>>> ЭПИКИ >>>>>>>>>>>>>>>>
    //добавляем Эпик
    void addEpics(Epic taskIn);

    //вывод списка эпиков
    ArrayList<Epic> getEpics();

    //удаляем все эпики и сразу все подзадачи
    void clearEpics();

    //поиск по идентификатору эпика
    Epic searchEpicById(int id);

    //удаление эпика по идентификатору
    //удаление всех подзадач, связанных с этим эпиком
    void deleteEpicById(int id);

    //обновление Эпика
    void updateEpic(Epic taskIn);

    //>>>>>>> ПОДЗАДАЧИ >>>>>>>>>>>>>>>>
    //Добавляем подзадачу
    void addSubtask(Subtask taskIn);

    //вывод списка подзадач
    ArrayList<Subtask> getSubtask();

    //удаляем все подзадачи
    void clearSubtasks();

    //поиск по идентификатору подзадачу
    Subtask searchSubtaskById(int id);

    //удаление подзадачи по идентификатору
    void deleteSubtaskById(int id);

    //Получение списка всех подзадач определённого эпика.
    ArrayList<Subtask> getSubtaskByEpic(int epicId);

    //обновление подзадачи
    void updateSubtask(Subtask taskIn);

    void checkStatus(int epicId);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();


}
