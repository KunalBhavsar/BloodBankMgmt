package co.project.bloodbankmgmt.models;

/**
 * Created by Kunal on 20/03/17.
 */

public class BloodGroup {
    private int id;
    private String title;

    public BloodGroup() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BloodGroup that = (BloodGroup) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
