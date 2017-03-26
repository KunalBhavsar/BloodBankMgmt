package co.project.bloodbankmgmt.models;

import java.io.Serializable;

/**
 * Created by Shraddha on 26/3/17.
 */

public class BloodBank implements Serializable {

    private long quantity;

    private int bloodGroup;

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public int getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(int bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
