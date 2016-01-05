package de.tobiasamon.demoapplicationretrofit.comments;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;

/**
 * Created by tobiasamon on 16.11.15.
 */
public interface CommentService {

    @GET("/comments")
    Call<List<Comment>> listComments();
}
