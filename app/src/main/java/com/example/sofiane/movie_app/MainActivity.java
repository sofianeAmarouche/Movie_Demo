package com.example.sofiane.movie_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    RecyclerView recyclerView;
    MovieListAdapter movieListAdapter;
    Data dataList;
    SearchView searchView;
    CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCompositeDisposable = new CompositeDisposable();
        initRecyclerView();
        loadJSON();

        searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);
    }

    private void initRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

    }

    private void loadJSON(){
        JSONInterface jsonInterface = new Retrofit.Builder()
                .baseUrl(JSONInterface.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(JSONInterface.class);

        mCompositeDisposable.add(jsonInterface.getMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(Data data) {
        dataList = data;
        movieListAdapter = new MovieListAdapter(dataList.getData());
        recyclerView.setAdapter(movieListAdapter);
    }

    private void handleError(Throwable throwable) {
        Toast.makeText(getApplicationContext(), "Loading issue...", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        String textEntered = query;
        movieListAdapter.filter(textEntered);
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
