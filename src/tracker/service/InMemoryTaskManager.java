package tracker.service;

import tracker.model.Epic;
import tracker.model.StatusTask;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>(); //задачи
    private final HashMap<Integer, Epic> epics = new HashMap<>(); //эпики
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    private int countId = 0;

    //Генерирует уникальный ID
    public int getNewId() {
        countId++;
        return countId;
    }
    //>>>>>>> ЗАДАЧИ >>>>>>>>>>>>>>>>
    //Добавляем задачу
    @Override
    public void addTasks(Task taskIn) {
        taskIn.setIdTask(getNewId());
        int id = taskIn.getIdTask();
        tasks.put(id, taskIn);
    }
    //вывод списка задач
    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskList = new ArrayList<>(tasks.values());
        return taskList;
    }
    //Удаляем все задачи
    @Override
    public void clearTasks() {
        //удаляем все задачи из истории
        for (Task task : tasks.values()){
            historyManager.remove(task.getIdTask());
        }
        tasks.clear();

    }

    //поиск по идентификатору задачи
    @Override
    public Task searchTaskById(int id){
        Task task = tasks.get(id);
        historyManager.addHistory(task);
        return task;
    }
    //удаление задачи по идентификатору
    @Override
    public void deleteTaskById(int id){
        tasks.remove(id);
        //удаляем задачу из истории
        historyManager.remove(id);
    }
    //обновление задачи
    @Override
    public void updateTask(Task taskIn) {
        boolean isTaskExist = tasks.containsKey(taskIn.getIdTask());

        if (isTaskExist) {
            tasks.put(taskIn.getIdTask(), taskIn);
        } else {
            String warningText = String.format("Задачи с id %d не существует", taskIn.getIdTask());
            System.out.println(warningText);
        }
    }

    //>>>>>>> ЭПИКИ >>>>>>>>>>>>>>>>
    //добавляем Эпик
    @Override
    public void addEpics(Epic taskIn) {
        taskIn.setIdTask(getNewId());
        int id = taskIn.getIdTask();
        epics.put(id, taskIn);
    }
    //вывод списка эпиков
    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicList = new ArrayList<>(epics.values());
        return epicList;
    }
    //удаляем все эпики и сразу все подзадачи
    @Override
    public void clearEpics() {
        //удаляем эпики из истории
        for (Epic epic : epics.values()){
            historyManager.remove(epic.getIdTask());
        }
        epics.clear();
        clearSubtasks();
    }
    //поиск по идентификатору эпика
    @Override
    public Epic searchEpicById(int id){
        Epic epic = epics.get(id);
        historyManager.addHistory(epic);
        return epic;

    }
    //удаление эпика по идентификатору
    //удаление всех подзадач, связанных с этим эпиком
    @Override
    public void deleteEpicById(int id){
        ArrayList<Integer> subtaskList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()){
            if (subtask.getEpicId() == id){
                subtaskList.add(subtask.getIdTask());
            }
        }
        // удаляем эпики из истории
        historyManager.remove(id);
        for (Integer i : subtaskList){
            //удаляем подзадачи из истории
            historyManager.remove(i);
            subtasks.remove(i);
        }
        epics.remove(id);
    }
    //обновление Эпика
    @Override
    public void updateEpic(Epic taskIn) {
        boolean isEpicExist = epics.containsKey(taskIn.getIdTask());

        if (isEpicExist) {
            epics.put(taskIn.getIdTask(), taskIn);
            //если вдруг решили изменить статус
            checkStatus(taskIn.getIdTask());

        } else {
            String warningText = String.format("Эпика с id %d не существует", taskIn.getIdTask());
            System.out.println(warningText);
        }
    }

    //>>>>>>> ПОДЗАДАЧИ >>>>>>>>>>>>>>>>
    //Добавляем подзадачу
    @Override
    public void addSubtask(Subtask taskIn) {
        int epicId = taskIn.getEpicId();
        if (!epics.containsKey(epicId)){
            System.out.println("Передан пустой или не существующий Эпик");
            return;
        }
        taskIn.setIdTask(getNewId());
        int id = taskIn.getIdTask();
        subtasks.put(id, taskIn);
        checkStatus(taskIn.getEpicId());
    }
    //вывод списка подзадач
    @Override
    public ArrayList<Subtask> getSubtask() {
        ArrayList<Subtask> subtaskList = new ArrayList<>(subtasks.values());
        return subtaskList;
    }
    //удаляем все подзадачи
    @Override
    public void clearSubtasks() {
        for (Subtask subtask : subtasks.values()){
            historyManager.remove(subtask.getIdTask());
        }
        subtasks.clear();
    }
    //поиск по идентификатору подзадачу
    @Override
    public Subtask searchSubtaskById(int id){
        Subtask subtask = subtasks.get(id);
        historyManager.addHistory(subtask);
        return subtask;

    }
    //удаление подзадачи по идентификатору
    @Override
    public void deleteSubtaskById(int id){
        historyManager.remove(id);
        subtasks.remove(id);
    }
    //Получение списка всех подзадач определённого эпика.
    @Override
    public ArrayList<Subtask> getSubtaskByEpic(int epicId){
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()){
            if (subtask.getEpicId() == epicId){
                subtaskList.add(subtasks.get(subtask.getIdTask()));
            }
        }
        return subtaskList;
    }
    //обновление подзадачи
    @Override
    public void updateSubtask(Subtask taskIn) {
        boolean isSubtaskExist = subtasks.containsKey(taskIn.getIdTask());

        if (isSubtaskExist) {
            subtasks.put(taskIn.getIdTask(), taskIn);
            //если вдруг решили изменить статус
            checkStatus(taskIn.getEpicId());

        } else {
            String warningText = String.format("Подзадачи с id %d не существует", taskIn.getIdTask());
            System.out.println(warningText);
        }
    }

    @Override
    public void checkStatus(int epicId){
        ArrayList<Subtask> subtasksList = getSubtaskByEpic(epicId);

        int countDone = 0;
        int countNew = 0;
        for (Subtask i : subtasksList){
            if (i.getStatusTask() == StatusTask.NEW){
                countNew++;
            } else if (i.getStatusTask() == StatusTask.DONE){
                countDone++;
            }
        }

        if (countDone == subtasksList.size()){
            epics.get(epicId).setStatusTask(StatusTask.DONE);
        } else if (countNew == subtasksList.size() ){
            epics.get(epicId).setStatusTask(StatusTask.NEW);
        } else {
            epics.get(epicId).setStatusTask(StatusTask.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


}