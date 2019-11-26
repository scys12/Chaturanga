package chaturanga.menu;

public class ActionEvent {
    private Object source;

    public ActionEvent(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}
