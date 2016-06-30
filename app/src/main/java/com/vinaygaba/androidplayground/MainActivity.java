package com.vinaygaba.androidplayground;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private List<Contributor> contributorList;
    private Retrofit retrofit;
    private GithubInterface gitHubInterface;
    private RecyclerViewAdapter adapter;
    static Context context;
    Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = getApplication();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        initializeDummyData();
        //setupIterceptor();
        setupRetrofitCall();


    }

    private void setupIterceptor() {
        REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                if (Util.isNetworkAvailable(context)) {
                    int maxAge = 60; // read from cache for 1 minute
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };
    }

    private void setupRetrofitCall() {

        //setup cache
        File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);


        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                okhttp3.Response originalResponse = chain.proceed(chain.request());
                                if (Util.isNetworkAvailable(context)) {
                                    int maxAge = 60; // read from cache for 1 minute
                                    return originalResponse.newBuilder()
                                            .header("Cache-Control", "public, max-age=" + maxAge)
                                            .build();
                                } else {
                                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                                    return originalResponse.newBuilder()
                                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                            .build();
                                }
                            }
                        })
                .cache(cache)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gitHubInterface = retrofit.create(GithubInterface.class);
        Call<List<Contributor>> call = gitHubInterface.contributors("vinaygaba","CreditCardView");

        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, retrofit2.Response<List<Contributor>> response) {

                contributorList = response.body();
                System.out.println("Response");
                System.out.println(contributorList);
                adapter = new RecyclerViewAdapter(contributorList);
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {
                System.out.println("Failed");
                t.printStackTrace();
            }

        });
    }

    private void initializeDummyData() {
        contributorList = new ArrayList<>();
        contributorList.add(new Contributor("RepoName1",10));
        contributorList.add(new Contributor("RepoName2",20));
        contributorList.add(new Contributor("RepoName3",30));
        contributorList.add(new Contributor("RepoName4",40));

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
            @BindView(R.id.cv) CardView cv;
            @BindView(R.id.person_name) TextView contributorLogin;
            @BindView(R.id.person_age) TextView contributions;
            @BindView(R.id.person_photo) SquareImageView personPhoto;

            RepoViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }
}
