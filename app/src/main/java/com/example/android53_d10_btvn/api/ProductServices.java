package com.example.android53_d10_btvn.api;

import com.example.android53_d10_btvn.model.ProductsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductServices {
    @GET("products?limit=0")
    Call<ProductsResponse> getProducts();
}
