package co.project.bloodbankmgmt.Interfaces;

/**
 * Created by Kunal on 02/04/17.
 */

public interface AppDataChangeNotifier {
    void attach(AppDataChangeListener listener);
    void detach(AppDataChangeListener listener);
}
