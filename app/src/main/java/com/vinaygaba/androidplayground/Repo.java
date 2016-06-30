package com.vinaygaba.androidplayground;

/**
 * Created by vinaygaba on 6/30/16.
 */
public class Repo {

    String mRepoName;
    String mRepoUrl;

    public String getmRepoName() {
        return mRepoName;
    }

    public void setmRepoName(String mRepoName) {
        this.mRepoName = mRepoName;
    }

    public String getmRepoUrl() {
        return mRepoUrl;
    }

    public void setmRepoUrl(String mRepoUrl) {
        this.mRepoUrl = mRepoUrl;
    }

    public Repo(String mRepoName, String mRepoUrl) {

        this.mRepoName = mRepoName;
        this.mRepoUrl = mRepoUrl;
    }
}
