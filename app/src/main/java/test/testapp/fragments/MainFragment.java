package test.testapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import test.testapp.R;
import test.testapp.adapter.SampleGridViewAdapter;
import test.testapp.listeners.SampleScrollListener;
import test.testapp.model.Datum;
import test.testapp.model.Model;
import test.testapp.network.ImgurApi;
import test.testapp.utils.Constants;

/**
 * Created by Darko-PC on 4/19/2016.
 */
public class MainFragment extends Fragment {


    List<Datum> data;
    SampleGridViewAdapter sampleGridViewAdapter;
    ProgressDialog progress;
    Model model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sample_gridview_activity, container, false);
        ButterKnife.bind(this, rootView);
        data = new ArrayList<>();
        sampleGridViewAdapter = new SampleGridViewAdapter(getContext(), android.R.layout.simple_list_item_1, data);
        GridView gv = (GridView) rootView.findViewById(R.id.grid_view);
        gv.setOnScrollListener(new SampleScrollListener(getContext()));
        gv.setAdapter(sampleGridViewAdapter);

        progress = new ProgressDialog(getContext());
        progress.show();
        progress.setMessage(getContext().getString(R.string.download));
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Client-ID " + Constants.MY_IMGUR_CLIENT_ID).build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        ImgurApi service = retrofit.create(ImgurApi.class);

        Call<Model> call = service.getData();
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                model = response.body();
                if (response.isSuccessful()) {
                    progress.dismiss();
                    Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                }
                if (model != null) {
                    data.clear();
                    data.addAll(model.getData());
                    sampleGridViewAdapter.notifyDataSetChanged();
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<Model> call, Throwable t) {

            }
        });


        return rootView;
    }

}
