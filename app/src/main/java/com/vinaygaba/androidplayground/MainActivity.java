package com.vinaygaba.androidplayground;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private List<Contributor> contributorList;
    private Retrofit retrofit;
    private GithubInterface gitHubInterface;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        initializeDummyData();
        setupRetrofitCall();


    }

    private void setupRetrofitCall() {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gitHubInterface = retrofit.create(GithubInterface.class);
        Call<List<Contributor>> call = gitHubInterface.contributors("vinaygaba","CreditCardView");

        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {

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
            @BindView(R.id.person_photo) ImageView personPhoto;

            RepoViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }
}
