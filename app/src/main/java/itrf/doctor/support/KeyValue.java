package itrf.doctor.support;

import java.security.Key;

public class KeyValue {
    private int Id;
    private String Title;

    public KeyValue() {
    }

    public KeyValue(int id, String title) {
        Id = id;
        Title = title;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    //To display object title as a string in spinner
    @Override
    public String toString() {
        return Title;
    }
}
