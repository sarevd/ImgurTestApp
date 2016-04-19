package test.testapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import test.testapp.R;
import test.testapp.fragments.SecondFragment;
import test.testapp.listeners.ProgressListener;

/**
 * Created by Darko-PC on 4/19/2016.
 */
public class SecondActivity extends AppCompatActivity implements ProgressListener {

    @Bind(R.id.asincConunter)
    TextView txtAsyncCounter;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.VISIBLE);
        txtAsyncCounter.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SecondFragment()).commit();


    }

    @Override
    public void onProgressChanged(int progress) {
        setProgressViews(progress);
    }

    @Override
    public void onTaskCanceled() {
        txtAsyncCounter.setText(R.string.canceled);
    }

    private void setProgressViews(int progress) {
        progressBar.setProgress(progress);
        txtAsyncCounter.setText("Progress at: " + progress + "%");
    }
}
