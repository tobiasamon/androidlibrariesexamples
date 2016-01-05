package de.tobiasamon.demoapplicationrxandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import de.tobiasamon.demoapplicationrxandroid.comments.Comment;
import de.tobiasamon.demoapplicationrxandroid.comments.CommentService;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements Observer<String> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView backgroundText;

    private TextView errorText;

    private TextView commentsText;

    private long start;
    private Subscription sub;
    private Observable<List<Comment>> commentObs;
    private CommentsObserver observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundText = (TextView) findViewById(R.id.textView);
        errorText = (TextView) findViewById(R.id.textView3);
        commentsText = (TextView) findViewById(R.id.textView4);

        start = System.currentTimeMillis();

        getObservable()
                .subscribeOn(Schedulers.newThread()) // Do computation on a new thread
                .observeOn(AndroidSchedulers.mainThread()) // Perform updates on main thread
                .subscribe(this); // Set the current class as observer

        getErrorObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CommentService service = retrofit.create(CommentService.class);

        observer = new CommentsObserver();
        commentObs = service.listComments()
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        sub = commentObs.subscribe(observer);
    }

    @Override
    protected void onPause() {
        sub.unsubscribe();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sub = commentObs.subscribe(observer);
    }

    private void handleComments(List<Comment> comments) {
        commentsText.setText("Found " + comments.size() + " comments");
    }

    private void handleCommentError(String message) {
        commentsText.setText("An error occured when getting comments: " + message);
    }

    /**
     * Creates an observable for a long running method.
     * Observable.defer() is used because using Observable.just() will block the thread.
     *
     * @return The observable instance
     */
    private Observable<String> getObservable() {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just(justALongRunningMethod());
            }
        });
    }

    private Observable<String> getErrorObservable() {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                throw new RuntimeException("Exception in a backend thread");
            }
        });
    }

    /**
     * This is a long running method where you can compute anything you want
     *
     * @return a string
     */
    private String justALongRunningMethod() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        long end = System.currentTimeMillis();
        return "Processing took " + (end - start) + " ms";
    }

    @Override
    public void onCompleted() {

    }

    /**
     * Method is getting called whenever an exception occurs in the observable.
     *
     * @param e The exception instance
     */
    @Override
    public void onError(Throwable e) {
        errorText.setText(e.getMessage());
    }

    @Override
    public void onNext(String s) {
        backgroundText.setText(s);
    }

    class CommentsObserver implements Observer<List<Comment>> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "Exception", e);
            handleCommentError(e.getMessage());
        }

        @Override
        public void onNext(List<Comment> comments) {
            handleComments(comments);
        }
    }
}
