package co.project.bloodbankmgmt.models;

import java.io.Serializable;

/**
 * Created by Shraddha on 26/3/17.
 */

public class BloodBank implements Serializable {

    private long quantity;

    private long bloodGroup;

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(long bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
