package tracker.service;
import tracker.model.Task;

import java.util.List;

 public  interface HistoryManager<T extends Task>  {
    List<T> getHistory();

    void remove(int id);

}
