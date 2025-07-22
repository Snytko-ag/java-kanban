package tracker.service;
import tracker.exception.TaskConflictException;
import tracker.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;// = new HashMap<>(); //задачи
    private final HashMap<Integer, Epic> epics;// = new HashMap<>(); //эпики
    private final HashMap<Integer, Subtask> subtasks;// = new HashMap<>();
    //private final TaskManager taskManager;

    private final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    private int countId = 0;

    private Set<Task> treeSet = new TreeSet<>(Task::compareTime);

    public  InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        //this.historyManager = historyManager;
    }

    //Генерирует уникальный ID
    public int getNewId() {
        countId++;
        return countId;
    }
    //>>>>>>> ЗАДАЧИ >>>>>>>>>>>>>>>>

    //Добавляем задачу
    @Override
    public void addTasks(Task taskIn) {
        checkCrossedTime(taskIn);
        taskIn.setIdTask(getNewId());
        int id = taskIn.getIdTask();
        tasks.put(id, taskIn);

        addToTreeSet(taskIn);
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
        // Удаляем все задачи из истории
        tasks.values().stream()
                .forEach(task -> historyManager.remove(task.getIdTask()));

        // Очищаем коллекцию задач
        tasks.clear();
        removeTypeFromTreeSet(TypeTask.TASK);
    }

    //поиск по идентификатору задачи
    @Override
    public Task searchTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.addHistory(task);
        return task;
    }

    //удаление задачи по идентификатору
    @Override
    public void deleteTaskById(int id) {
        Task removedTask = tasks.get(id);
        tasks.remove(id);
        //удаляем задачу из истории
        historyManager.remove(id);
        removeFromTreeSet(removedTask);
    }

    //обновление задачи
    @Override
    public void updateTask(Task taskIn) {
        boolean isTaskExist = tasks.containsKey(taskIn.getIdTask());

        if (isTaskExist) {
            tasks.put(taskIn.getIdTask(), taskIn);
            checkCrossedTime(taskIn);
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
        epics.values().forEach(epic ->  historyManager.remove(epic.getIdTask()));
        epics.clear();
        clearSubtasks();
        removeTypeFromTreeSet(TypeTask.SUBTASK);
    }

    //поиск по идентификатору эпика
    @Override
    public Epic searchEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.addHistory(epic);
        return epic;

    }

    //удаление эпика по идентификатору
    //удаление всех подзадач, связанных с этим эпиком

    @Override
    public void deleteEpicById(int id) {
        // Получаем идентификаторы подзадач, связанных с данным эпиком
        List<Integer> subtaskIdsToRemove = subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == id)
                .map(Subtask::getIdTask)
                .collect(Collectors.toList());

        // Удаляем эпик из истории
        historyManager.remove(id);

        // Удаляем каждую подзадачу и её историю
        subtaskIdsToRemove.forEach(subtaskId -> {
            historyManager.remove(subtaskId);
            subtasks.remove(subtaskId);
        });

        // Удаляем сам эпик
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
        if (!epics.containsKey(epicId)) {
            System.out.println("Передан пустой или не существующий Эпик");
            return;
        }
        if (taskIn.getDateStart() != null && taskIn.getDateEnd() != null) {
            checkCrossedTime(taskIn);
        }
        taskIn.setIdTask(getNewId());
        int id = taskIn.getIdTask();
        subtasks.put(id, taskIn);
        checkStatus(taskIn.getEpicId());

        if (taskIn.getDateStart() != null && taskIn.getDateEnd() != null) {
            addToTreeSet(taskIn);
        }
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
        subtasks.values().forEach(subtask -> historyManager.remove(subtask.getIdTask()));
        subtasks.clear();
        removeTypeFromTreeSet(TypeTask.SUBTASK);
    }

    //поиск по идентификатору подзадачу
    @Override
    public Subtask searchSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.addHistory(subtask);
        return subtask;

    }

    //удаление подзадачи по идентификатору
    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = searchSubtaskById(id);
        historyManager.remove(id);
        subtasks.remove(id);
        removeFromTreeSet(subtask);
    }

    //Получение списка всех подзадач определённого эпика.
    @Override
    public ArrayList<Subtask> getSubtaskByEpic(int epicId) {

        List<Subtask> subtaskList = subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epicId)
                .map(subtask -> subtasks.get(subtask.getIdTask()))
                .collect(Collectors.toList());
        return (ArrayList<Subtask>) subtaskList;
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
    public void checkStatus(int epicId) {
        List<Subtask> subtasksList = getSubtaskByEpic(epicId);

        boolean allNew = subtasksList.stream()
                .allMatch(subtask -> subtask.getStatusTask() == StatusTask.NEW);
        boolean allDone = subtasksList.stream()
                .allMatch(subtask -> subtask.getStatusTask() == StatusTask.DONE);

        if (allNew) {
            epics.get(epicId).setStatusTask(StatusTask.NEW);
        } else if (allDone) {
            epics.get(epicId).setStatusTask(StatusTask.DONE);
        } else {
            epics.get(epicId).setStatusTask(StatusTask.IN_PROGRESS);
        }

        setDateEpic(epicId);
    }

    protected void setDateEpic(int epicId) {
        List<Subtask> subtasksList = getSubtaskByEpic(epicId);

        LocalDateTime dateStart = subtasksList.stream()
                .map(Task::getDateStart)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime dateEnd = subtasksList.stream()
                .map(Task::getDateEnd)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        Duration duration = subtasksList.stream()
                .map(Task::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);

        if (dateStart != null) {
            epics.get(epicId).setDateStart(dateStart);
        }
        if (dateEnd != null) {
            epics.get(epicId).setDateEnd(dateEnd);
        }
        if (!duration.isZero()) {
            epics.get(epicId).setDuration(duration);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(treeSet);
    }

    public void addToTreeSet(Task task) {
        if (task.getDateStart() != null) {
            treeSet.add(task);
        }
    }

    public <T extends Task> void updateTreeSet(T task) {
        if (task == null) {
            return;
        }

        Task oldTask = (task.getTypeTask() == TypeTask.TASK ? tasks : subtasks).get(task.getIdTask());
        removeFromTreeSet(oldTask);
        addToTreeSet(task);
    }

    protected List<Task> removeTypeFromTreeSet(TypeTask taskType) {
        return treeSet.stream().filter(task -> task.getTypeTask() != taskType).toList();
    }

    public <T extends Task> void removeFromTreeSet(T task) {
        treeSet.remove(task);
    }

    public <T extends Task> void checkCrossedTime(T task) {
        LocalDateTime startTime = task.getDateStart();
        LocalDateTime endTime = task.getDateEnd();

        if (startTime == null || endTime == null) {
            //throw new ManagerSaveException("Эта задача не участвует в планировании");
            return;
        }

        boolean isCrossed = treeSet.stream()
                .filter(treeTask -> treeTask.getIdTask()  != task.getIdTask())
                .anyMatch(treeTask -> {
                            LocalDateTime treeTaskStart = treeTask.getDateStart();
                            LocalDateTime treeTaskEnd = treeTask.getDateEnd();
                            if (treeTaskEnd.isAfter(startTime) && treeTaskEnd.isBefore(endTime)) {
                                return true;
                            }
                    return (treeTaskStart.isAfter(startTime) || treeTaskStart.isEqual(startTime)) && !treeTaskStart.isAfter(endTime);
                });
        if (isCrossed) {
            throw new TaskConflictException("В эти даты уже запланирована другая задача");
        }
    }
}