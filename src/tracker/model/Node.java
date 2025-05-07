package tracker.model;

import java.util.Objects;

public class  Node <T extends Task> {

     T task;
     Node<T> prev;
     Node<T> next;

    public Node(T task, Node<T> prev,  Node<T> next) {
        this.task = task;
        this.prev = prev;
        this.next = next;

    }


    public T getTask() {
        return task;
    }

    public void setTask(T task) {
        this.task = task;
    }

    public Node<T> getPrev() {
        return prev;
    }

    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node{" +
                "task=" + task +
                ", prev=" + prev +
                ", next=" + next +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(getTask(), node.getTask()) && Objects.equals(getPrev(), node.getPrev()) && Objects.equals(getNext(), node.getNext());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTask(), getPrev(), getNext());
    }
}
