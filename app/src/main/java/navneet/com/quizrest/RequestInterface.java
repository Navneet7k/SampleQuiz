package navneet.com.quizrest;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by user on 07-Dec-2017.
 */

public interface RequestInterface {
    @GET("api.php?amount=10")
    Call<AndroidVersion> getJSON();
}
