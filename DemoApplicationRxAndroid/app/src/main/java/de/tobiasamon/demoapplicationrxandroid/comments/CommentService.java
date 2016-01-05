package de.tobiasamon.demoapplicationrxandroid.comments;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import rx.Observable;

/**
 * Created by tobiasamon on 16.11.15.
 */
public interface CommentService {

    @GET("/comments")
    Observable<List<Comment>> listComments();
}
