package tracker.model;

public class Epic  extends Task {

    public Epic( String name, String description) {

        super(name, description);
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    public Epic(int id, String name, String description, StatusTask status) {
        super(id, name, description, status);
    }


}