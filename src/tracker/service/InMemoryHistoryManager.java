package tracker.service;
import tracker.model.Task;
import tracker.model.Node;

import java.util.*;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager {

    private Node<T> head;
    private Node<T> tail;
    private final Map<Integer, Node<T>> historyTasks;

    public InMemoryHistoryManager() {
        this.historyTasks = new HashMap<>();
    }


    public void addHistory(T tasks) {
        if (tasks == null) {
            return;
        }
        if (historyTasks.containsKey(tasks.getIdTask())) {
            Node<T> node = historyTasks.remove((tasks.getIdTask()));
            this.removeNode(node);
        }
        this.linkLast((T) tasks);
    }



   private void removeNode(Node<T> node) {
        Node<T> next = node.getNext();
        Node<T> prev = node.getPrev();
        if (prev == null) {
            head = next;
        } else {
            prev.setNext(next);
            node.setNext(null);
        }
        if (next == null) {
            tail = prev;
        } else {
            next.setPrev(prev);
            node.setNext(null);
        }
        node.setTask(null);
    }


    @Override
    public void remove(int id) {
        if (historyTasks.containsKey(id)) {
            Node<T> node = historyTasks.get(id);
            this.removeNode(node);
            historyTasks.remove(id);
        }
    }

    @Override
    public List<T> getHistory() {
        List<T> history = new ArrayList<>();
        List<Node<T>> nodes = this.getTasks();
        for (Node<T> node : nodes) {
            history.add((T) node.getTask());
        }

        return history;
    }

    private List<Node<T>> getTasks() {
        List<Node<T>> nodes = new ArrayList<>();
        Node<T> node = head;
        while (node != null) {
            nodes.add(node);
            node = node.getNext();
        }

        return nodes;
    }

    private void linkLast(T task) {
        final Node<T> oldTail = tail;
        final Node<T> newNode = new Node<T>(task, oldTail,  null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
        int taskId = task.getIdTask();
        historyTasks.put(taskId, newNode);
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
