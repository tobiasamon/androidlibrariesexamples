package de.tobiasamon.demoapplicationretrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.tobiasamon.demoapplicationretrofit.comments.Comment;
import de.tobiasamon.demoapplicationretrofit.comments.CommentAdapter;
import de.tobiasamon.demoapplicationretrofit.comments.CommentService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements Callback<List<Comment>> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ListView listView;

    private CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        listView.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, (RelativeLayout) findViewById(R.id.mainLayout), false));
        adapter = new CommentAdapter(this);
        listView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .client(createClient())
                .baseUrl("http://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CommentService service = retrofit.create(CommentService.class);

        Call<List<Comment>> comments = service.listComments();
        comments.enqueue(this);
    }

    private OkHttpClient createClient() {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request newRequest = request.newBuilder().addHeader("Cache-Control", "no-cache").build();
                return chain.proceed(newRequest);
            }
        });
        Cache cache = createCache();
        if (cache != null) {
            client.setCache(cache);
        }
        return client;
    }

    private Cache createCache() {
        File httpCacheDirectory = new File(getCacheDir(), "responses");
        Cache cache = null;
        if (!httpCacheDirectory.exists()) {
            httpCacheDirectory.mkdir();
            Log.d(TAG, "Created cache dir " + httpCacheDirectory.getAbsolutePath());
        }
        Log.d(TAG, "Have cache dir " + httpCacheDirectory.getAbsolutePath());
        if (httpCacheDirectory != null) {
            cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        }
        return cache;
    }

    @Override
    public void onResponse(Response<List<Comment>> response, Retrofit retrofit) {
        if (response.body() != null) {
            adapter.clear();
            Log.d(TAG, "Found " + response.body().size() + " comments");
            for (Comment comment : response.body()) {
                adapter.add(comment);
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e(TAG, "Exception occurred", t);
    }
}
