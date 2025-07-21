package tracker.model;

import java.time.LocalDateTime;

public class Epic  extends Task {

    private LocalDateTime dateEnd;

    public Epic(String name, String description) {
        super(name, description);
    }


    public Epic(int id, String name, String description, StatusTask status) {
        super(id, name, description, status);
    }

    public void setDateEnd (LocalDateTime dateEnd){
        this.dateEnd = dateEnd;
    }

    public LocalDateTime getDateEnd(){
        return dateEnd;
    }

    @Override
    public TypeTask getTypeTask() {
        return TypeTask.EPIC;
    }

    public String toString(){
       return
        "Epic{" +
            "dataStart=" + getDateStart() +
            ", dateEnd=" + getDateEnd() +
            ", duration=" + getDuration()+
            "} " + super.toString();
    }


}