package com.vinaygaba.androidplayground.interfaces;

import com.vinaygaba.androidplayground.models.Contributor;
import com.vinaygaba.androidplayground.models.Repo;

import java.util.List;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by vinaygaba on 7/1/16.
 */
public interface GithubRxInterface {

    @GET("users/{user}/repos")
    Observable<List<Repo>> listRepos(@Path("user") String user);

    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> contributors(
            @Path("owner") String owner,
            @Path("repo") String repo
    );
}
