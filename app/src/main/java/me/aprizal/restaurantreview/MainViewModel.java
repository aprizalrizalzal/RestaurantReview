package me.aprizal.restaurantreview;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<Restaurant> _restaurant = new MutableLiveData<>();
    public LiveData<Restaurant> getRestaurant() {
        return _restaurant;
    }

    private final MutableLiveData<List<CustomerReviewsItem>> _listReview = new MutableLiveData<>();
    public LiveData<List<CustomerReviewsItem>> getListReview() {
        return _listReview;
    }

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading() {
        return _isLoading;
    }

    private final MutableLiveData<String> _snackbarText = new MutableLiveData<>();
    public LiveData<String> snackbarText() {
        return _snackbarText;
    }

    private static final String TAG = "MainViewModel";
    private static final String RESTAURANT_ID = "uewq1zg2zlskfw1e867";

    public MainViewModel() {
        findRestaurant();
    }

    public void findRestaurant() {
        _isLoading.setValue(true);
        Call<Response> client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID);
        client.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(@NonNull Call<Response> call, @NonNull retrofit2.Response<Response> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        _restaurant.setValue(response.body().getRestaurant());
                        _listReview.setValue(response.body().getRestaurant().getCustomerReviews());
                    }
                } else {
                    if (response.body() != null) {
                        Log.e(TAG, "onFailure: " + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Response> call, @NonNull Throwable t) {
                _isLoading.setValue(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void postReview(String toString) {
        _isLoading.setValue(true);
        Call<PostReviewResponse> client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Dicoding", toString);
        client.enqueue(new Callback<PostReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<PostReviewResponse> call, @NonNull retrofit2.Response<PostReviewResponse> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        _listReview.setValue(response.body().getCustomerReviews());
                        _snackbarText.setValue(response.body().getMessage());
                    }
                } else {
                    if (response.body() != null) {
                        Log.e(TAG, "onFailure: " + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostReviewResponse> call, @NonNull Throwable t) {
                _isLoading.setValue(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

}
