package navneet.com.quizrest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by user on 07-Dec-2017.
 */

public interface RequestInterface {
    @GET("api.php?amount=10")
    Call<AndroidVersion> getJSON(@Query("category") String category);
}
