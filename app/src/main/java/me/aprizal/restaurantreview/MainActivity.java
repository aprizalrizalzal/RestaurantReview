package me.aprizal.restaurantreview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import me.aprizal.restaurantreview.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    private static final String RESTAURANT_ID = "uewq1zg2zlskfw1e867";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.getRestaurant().observe(this, this::setRestaurantData);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvReview.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.rvReview.addItemDecoration(itemDecoration);
//        findRestaurant();

        mainViewModel.getListReview().observe(this, this::setReviewData);
        mainViewModel.isLoading().observe(this, this::showLoading);

        mainViewModel.snackbarText().observe(this, text -> {
            Snackbar.make(
                    getWindow().getDecorView().getRootView(),
                    text,
                    Snackbar.LENGTH_SHORT
            ).show();
        });

        binding.btnSend.setOnClickListener(view -> {
//            if (binding.edReview.getText() != null) {
//                postReview(binding.edReview.getText().toString());
//            }
            mainViewModel.postReview(binding.edReview.getText().toString());
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });
    }
//
//    private void postReview(String toString) {
//        showLoading(true);
//        Call<PostReviewResponse> client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Dicoding", toString);
//        client.enqueue(new Callback<PostReviewResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<PostReviewResponse> call, @NonNull retrofit2.Response<PostReviewResponse> response) {
//                showLoading(false);
//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
//                        setReviewData(response.body().getCustomerReviews());
//                    }
//                } else {
//                    if (response.body() != null) {
//                        Log.e(TAG, "onFailure: " + response.body().getMessage());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<PostReviewResponse> call, @NonNull Throwable t) {
//                showLoading(false);
//                Log.e(TAG, "onFailure: " + t.getMessage());
//            }
//        });
//    }

//    private void findRestaurant() {
//        showLoading(true);
//        Call<Response> client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID);
//        client.enqueue(new Callback<Response>() {
//            @Override
//            public void onResponse(@NonNull Call<Response> call, @NonNull retrofit2.Response<Response> response) {
//                showLoading(false);
//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
//                        setRestaurantData(response.body().getRestaurant());
//                        setReviewData(response.body().getRestaurant().getCustomerReviews());
//                    }
//                } else {
//                    if (response.body() != null) {
//                        Log.e(TAG, "onFailure: " + response.body().getMessage());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<Response> call, @NonNull Throwable t) {
//                showLoading(false);
//                Log.e(TAG, "onFailure: " + t.getMessage());
//            }
//        });
//    }

    private void setRestaurantData(Restaurant restaurant) {
        binding.tvTitle.setText(restaurant.getName());
        binding.tvDescription.setText(restaurant.getDescription());
        Glide.with(MainActivity.this).
                load("https://restaurant-api.dicoding.dev/images/large/" + restaurant.getPictureId())
                .into(binding.ivPicture);
    }

    private void setReviewData(List<CustomerReviewsItem> customerReviews) {
        ArrayList<String> listReview = new ArrayList<>();
        for (CustomerReviewsItem review : customerReviews) {
            listReview.add(review.getReview() + "\n- " + review.getName());
        }
        ReviewAdapter adapter = new ReviewAdapter(listReview);
        binding.rvReview.setAdapter(adapter);
        binding.edReview.setText("");
    }

    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}