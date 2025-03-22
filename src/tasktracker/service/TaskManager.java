package tasktracker.service;

import tasktracker.model.Epic;
import tasktracker.model.StatusTask;
import tasktracker.model.Subtask;
import tasktracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>(); //задачи
    private final HashMap<Integer, Epic> epics = new HashMap<>(); //эпики
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>(); //подзадачи

    private int countId = 0;

    //Генерирует уникальный ID
    public int getNewId() {
        countId++;
        return countId;
    }
    //>>>>>>> ЗАДАЧИ >>>>>>>>>>>>>>>>
    //Добавляем задачу tasktracker.model.model.Task
    public void addTasks(Task taskIn) {
        tasks.put(taskIn.getIdTask(), taskIn);
    }
    //вывод списка задач
    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskList = new ArrayList<>(tasks.values());
        return taskList;
    }
    //Удаляем все задачи
    public void clearTasks() {
        tasks.clear();
    }
    //поиск по идентификатору задачи
    public ArrayList<Task> searchTaskById(int id){
        ArrayList<Task> rezult = new ArrayList<>();
        if (tasks.containsKey(id)){
            rezult.add(tasks.get(id));
        } else System.out.println("Не найдена задача с id = " +id);
        return rezult;
    }
    //удаление задачи по идентификатору
    public void deleteTaskById(int id){
        tasks.remove(id);
    }
    //обновление задачи
    public void updateTask(Task taskIn) {
        boolean isTaskExist = tasks.containsKey(taskIn.getIdTask());

        if (isTaskExist) {
            tasks.put(taskIn.getIdTask(), taskIn);
        } else {
            System.out.println("Задачи с id " + taskIn.getIdTask() + " не существует");
        }
    }

    //>>>>>>> ЭПИКИ >>>>>>>>>>>>>>>>
    //добавляем Эпик
    public void addEpics(Epic taskIn) {
        epics.put(taskIn.getIdTask(), taskIn);
    }
    //вывод списка эпиков
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicList = new ArrayList<>(epics.values());
        return epicList;
    }
    //удаляем все эпики и сразу все подзадачи
    public void clearEpics() { //
        epics.clear();
        clearSubtasks();
    }
    //поиск по идентификатору эпика
    public ArrayList<Epic> searchEpicById(int id){
        ArrayList<Epic> rezult = new ArrayList<>();
        if (epics.containsKey(id)){
            rezult.add(epics.get(id));
        } else System.out.println("Не найден эпик с id = " +id);
        return rezult;
    }
    //удаление эпика по идентификатору
    //удаление всех подзадач, связанных с этим эпиком
    public void deleteEpicById(int id){
        ArrayList<Integer> subtaskList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()){
            if (subtask.getEpicId() == id){
                subtaskList.add(subtask.getIdTask());
            }
        }
        for (Integer i : subtaskList){
            subtasks.remove(i);
        }
        epics.remove(id);
    }
    //обновление Эпика
    public void updateEpic(Epic taskIn) {
        boolean isEpicExist = epics.containsKey(taskIn.getIdTask());

        if (isEpicExist) {
            epics.put(taskIn.getIdTask(), taskIn);
            //если вдруг решили изменить статус
            checkStatus(taskIn.getIdTask());

        } else {
            System.out.println("Эпика с id " + taskIn.getIdTask() + " не существует");
        }
    }

    //>>>>>>> ПОДЗАДАЧИ >>>>>>>>>>>>>>>>
    //Добавляем подзадачу
    public void addSubtask(Subtask taskIn) {
        subtasks.put(taskIn.getIdTask(), taskIn);
        checkStatus(taskIn.getEpicId());
        //System.out.println("statusTask = " + statusTask);
    }
    //вывод списка подзадач
    public ArrayList<Subtask> getSubtask() {
        ArrayList<Subtask> subtaskList = new ArrayList<>(subtasks.values());
        return subtaskList;
    }
    //удаляем все подзадачи
    public void clearSubtasks() {
        subtasks.clear();
    }
    //поиск по идентификатору подзадачу
    public ArrayList<Subtask> searchSubtaskById(int id){
        ArrayList<Subtask> rezult = new ArrayList<>();
        if (subtasks.containsKey(id)){
            rezult.add(subtasks.get(id));
        } else System.out.println("Не найдена подзадача с id = " +id);
        return rezult;
    }
    //удаление подзадачи по идентификатору
    public void deleteSubtaskById(int id){
        subtasks.remove(id);
    }
    //Получение списка всех подзадач определённого эпика.
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
    public void updateSubtask(Subtask taskIn) {
        boolean isSubtaskExist = subtasks.containsKey(taskIn.getIdTask());

        if (isSubtaskExist) {
            subtasks.put(taskIn.getIdTask(), taskIn);
            //если вдруг решили изменить статус
            checkStatus(taskIn.getEpicId());

        } else {
            System.out.println("Подзадачи с id " + taskIn.getIdTask() + " не существует");
        }
    }

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
        } else if (countNew == subtasksList.size() || countNew == 0){
            epics.get(epicId).setStatusTask(StatusTask.NEW);
        } else {
            epics.get(epicId).setStatusTask(StatusTask.IN_PROGRESS);
        }
    }
}