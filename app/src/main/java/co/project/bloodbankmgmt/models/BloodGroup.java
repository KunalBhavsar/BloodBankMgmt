package co.project.bloodbankmgmt.models;

/**
 * Created by Kunal on 20/03/17.
 */

public class BloodGroup {
    private long id;
    private String title;

    public BloodGroup() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
