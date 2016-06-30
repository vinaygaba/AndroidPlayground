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

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private List<Repo> repoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        initializeDummyData();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(repoList);
        mRecyclerView.setAdapter(adapter);

    }

    private void initializeDummyData() {
        repoList = new ArrayList<>();
        repoList.add(new Repo("RepoName1","RepoUrl1"));
        repoList.add(new Repo("RepoName2","RepoUrl2"));
        repoList.add(new Repo("RepoName3","RepoUrl3"));
        repoList.add(new Repo("RepoName4","RepoUrl4"));

    }


    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RepoViewHolder>{

        List<Repo> reposList;


        RecyclerViewAdapter(List<Repo> reposList){
            this.reposList = reposList;
        }

        @Override
        public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            RepoViewHolder pvh = new RepoViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(RepoViewHolder holder, int position) {
            holder.repoTitle.setText(reposList.get(position).getmRepoName());
            holder.repoURL.setText(reposList.get(position).getmRepoUrl());
            Log.e("Tag",position+"");
            //holder.personPhoto.setImageResource(reposList.get(position).photoId);

        }

        @Override
        public int getItemCount() {
            Log.e("TAG",reposList.size()+"");
            return reposList.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class RepoViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.cv) CardView cv;
            @BindView(R.id.person_name) TextView repoTitle;
            @BindView(R.id.person_age) TextView repoURL;
            @BindView(R.id.person_photo) ImageView personPhoto;

            RepoViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }
}
