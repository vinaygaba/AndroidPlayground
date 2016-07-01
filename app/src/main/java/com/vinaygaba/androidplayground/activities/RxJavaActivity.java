package com.vinaygaba.androidplayground.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vinaygaba.androidplayground.R;
import com.vinaygaba.androidplayground.interfaces.GithubInterface;
import com.vinaygaba.androidplayground.interfaces.GithubRxInterface;
import com.vinaygaba.androidplayground.models.Contributor;
import com.vinaygaba.androidplayground.views.SquareImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxJavaActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private List<Contributor> contributorList;
    private Retrofit retrofit;
    private GithubRxInterface gitHubInterface;
    private RecyclerViewAdapter adapter;
    static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = getApplication();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        setupRetrofitCall();

    }

    private void setupRetrofitCall() {

        contributorList = new ArrayList<>();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        gitHubInterface = retrofit.create(GithubRxInterface.class);
        gitHubInterface.contributors("square","retrofit")
                .flatMap(new Func1<List<Contributor>, Observable<Contributor>>() {
                    @Override
                    public Observable<Contributor> call(List<Contributor> contributors) {
                        return Observable.from(contributors);
                    }
                })
                .map(new Func1<Contributor, Contributor>() {
                    @Override
                    public Contributor call(Contributor contributor) {
                        if(contributor.getLogin().equals("JakeWharton")){
                            contributor.setLogin("VinayGaba");
                            return contributor;
                        }

                        return contributor;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Contributor>() {
                    @Override
                    public final void onCompleted() {
                        // do nothing
                        adapter = new RecyclerViewAdapter(contributorList);
                        mRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                    @Override
                    public final void onError(Throwable e) {
                        Log.e("GithubDemo", e.getMessage());
                    }

                    @Override
                    public final void onNext(Contributor response) {
                        contributorList.add(response);
                        Log.e("OnNext","Called");
                    }
                });

    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RepoViewHolder>{

        List<Contributor> contributorsList;


        RecyclerViewAdapter(List<Contributor> reposList){
            this.contributorsList = reposList;
        }

        @Override
        public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            RepoViewHolder pvh = new RepoViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(RepoViewHolder holder, int position) {
            holder.contributorLogin.setText(contributorsList.get(position).getLogin());
            holder.contributions.setText(contributorsList.get(position).getContributions()+"");
            Log.e("Tag",position+"");
            //holder.personPhoto.setImageResource(reposList.get(position).photoId);
            Picasso.with(getApplication())
                    .load(contributorsList.get(position).getAvatar_url())
                    .into(holder.personPhoto);

        }

        @Override
        public int getItemCount() {
            Log.e("TAG",contributorsList.size()+"");
            return contributorsList.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class RepoViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.cv)
            CardView cv;

            @BindView(R.id.person_name)
            TextView contributorLogin;

            @BindView(R.id.person_age)
            TextView contributions;

            @BindView(R.id.person_photo)
            SquareImageView personPhoto;

            RepoViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }
}
