package in.divyamary.moviereel.api;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class ApiManager {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    private ApiManager() {

    }

    public static TmdbMovieApi getTmdbMovieApi() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(logging);
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                com.squareup.okhttp.Response response = chain.proceed(request);
                boolean unAuthorized = (response.code() == 401);
                if (unAuthorized) {
                    throw new AuthorizeException();
                }
                boolean notFound = (response.code() == 404);
                if (notFound) {
                    throw new NotFoundException();
                }
                return response;
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        TmdbMovieApi tmdbMovieApi = retrofit.create(TmdbMovieApi.class);
        return tmdbMovieApi;
    }
}
