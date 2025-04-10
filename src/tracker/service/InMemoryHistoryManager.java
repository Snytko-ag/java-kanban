package tracker.service;
import tracker.model.Task;

import java.util.*;

public class InMemoryHistoryManager <T extends Task> implements HistoryManager{

    final HashMap<Integer, T> historyTasks;

    public InMemoryHistoryManager() {
        this.historyTasks = new HashMap<>();
    }

    public void addHistory(T tasks) {
        if (tasks == null){
            return;
        }
        if (historyTasks.size() > 11){
            historyTasks.remove(0);
        }
        historyTasks.put(tasks.getIdTask(),tasks);
    }

    @Override
    public List<T> getHistory() {
        List<T> taskList = new ArrayList<>(historyTasks.values());
        return taskList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager<?> that = (InMemoryHistoryManager<?>) o;
        return Objects.equals(historyTasks, that.historyTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(historyTasks);
    }


    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "historyTasks=" + historyTasks +
                '}';
    }
}
