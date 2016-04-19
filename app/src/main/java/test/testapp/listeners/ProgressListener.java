package test.testapp.listeners;

/**
 * Created by Darko-PC on 4/19/2016.
 */
public interface ProgressListener {

    void onProgressChanged(int progress);

    void onTaskCanceled();
}