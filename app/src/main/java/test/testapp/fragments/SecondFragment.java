package test.testapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import test.testapp.R;
import test.testapp.listeners.ProgressListener;

/**
 * Created by Darko-PC on 4/19/2016.
 */
public class SecondFragment extends Fragment {


    @Bind(R.id.button)
    Button button;

    private ProgressListener progressListener;
    private int progress;
    private Handler handler = new Handler();
    private AsyncTask task;
    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        progressListener = (ProgressListener) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this, rootView);
        setRetainInstance(true);
        button.setVisibility(View.VISIBLE);
        button.getPaddingTop();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTask();
            }
        });
        initialize();
        return rootView;
    }


    private void initialize() {
        progress = 0;
        activity = getActivity();
        runTask();
    }


    private void runTask() {
        task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                while (progress < 100) {
                    if (isCancelled())
                        break;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressListener.onProgressChanged(progress);
                            progress += 1;
                        }
                    });


                }
                cancel(true);
                return null;

            }
        }.execute();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTask();
    }

    private void cancelTask() {
        if (task != null) {
            task.cancel(true);
            progressListener.onTaskCanceled();
        }
    }


}
