package com.vinaygaba.androidplayground;

/**
 * Created by vinaygaba on 6/30/16.
 */
class Contributor {
    String login;
    int contributions;
    String avatar_url;

    public Contributor(String login, int contributions) {
        this.login = login;
        this.contributions = contributions;
        this.avatar_url = "";
    }

    public Contributor(String login, int contributions, String avatar_url) {
        this.login = login;
        this.contributions = contributions;
        this.avatar_url = avatar_url;
    }

    public String getAvatar_url() {

        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }


    public String getLogin() {

        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getContributions() {
        return contributions;
    }

    public void setContributions(int contributions) {
        this.contributions = contributions;
    }
}