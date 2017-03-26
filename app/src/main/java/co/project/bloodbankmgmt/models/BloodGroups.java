package co.project.bloodbankmgmt.models;

import java.io.Serializable;

/**
 * Created by Shraddha on 26/3/17.
 */

public class BloodGroups implements Serializable {

    private long id;

    private String title;

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
