package test.testapp.network;

/**
 * Created by Darko-PC on 4/16/2016.
 */
import retrofit2.Call;
import retrofit2.http.GET;
import test.testapp.model.Model;

public interface ImgurApi {

    @GET("/3/gallery/hot/viral/0.json")
     Call<Model> getData();


}