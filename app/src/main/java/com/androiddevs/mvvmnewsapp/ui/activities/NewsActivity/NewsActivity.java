package com.androiddevs.mvvmnewsapp.ui.activities.NewsActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.androiddevs.mvvmnewsapp.R;
import com.androiddevs.mvvmnewsapp.Repository.NewsRepository;
import com.androiddevs.mvvmnewsapp.databinding.ActivityMainBinding;
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.disposables.CompositeDisposable;

@AndroidEntryPoint
public class NewsActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    BottomNavigationView bottomNav;
    NavController mNavController;
    ArticleDatabase db;
    NewsRepository newsRepo;
    NewsViewModelProviderFactory newsFactory;
    public NewsViewModel newsViewModel;
    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNav = binding.bottomNavigationView;
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        mNavController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                switch (destination.getId())
                {
                    case R.id.breakingNewsFragment:
                        setTitle("Breaking News");
                        break;
                    case R.id.savedNewsFragment:
                        setTitle("Saved News");
                        break;

                    case R.id.searchNewsFragment:
                        setTitle("Search News");
                        break;

                }
            }
        });
        NavigationUI.setupWithNavController(bottomNav, mNavController);
        //db = ArticleDatabase.getInstance(this);
//        compositeDisposable =new CompositeDisposable();
//        newsRepo = new NewsRepository(db,compositeDisposable, NewsClient.getInstance());
        //newsViewModel = new ViewModelProvider(this,new NewsViewModelProviderFactory(getApplication(),newsRepo,compositeDisposable)).get(NewsViewModel.class);
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
    }
}
