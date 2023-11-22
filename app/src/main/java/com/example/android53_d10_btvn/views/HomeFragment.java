package com.example.android53_d10_btvn.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android53_d10_btvn.R;
import com.example.android53_d10_btvn.adapter.ProductAdapter;
import com.example.android53_d10_btvn.api.ProductServices;
import com.example.android53_d10_btvn.api.RetrofitClient;
import com.example.android53_d10_btvn.interfaces.IClickListener;
import com.example.android53_d10_btvn.interfaces.IWishView;
import com.example.android53_d10_btvn.model.Product;
import com.example.android53_d10_btvn.model.ProductsResponse;
import com.example.android53_d10_btvn.presenter.WishListPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class HomeFragment extends Fragment implements IWishView,IClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView rvProduct, rvCategory;
    private ProductAdapter mProductAdapter, mCategoryAdapter;
    private List<Product> mListProducts;
    private ProductServices productServices;
    private WishListPresenter wishListPresenter;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        callApiGetProducts();
    }

    private void initView(View view) {
        rvProduct = view.findViewById(R.id.rvProduct);
        rvCategory = view.findViewById(R.id.rvCategory);
    }

    private void callApiGetProducts() {
        wishListPresenter = new WishListPresenter(getContext(),this);
        productServices = RetrofitClient.create(ProductServices.class);

        productServices.getProducts().enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                ProductsResponse productsResponse = response.body();
                mListProducts = new ArrayList<>();
                mListProducts = productsResponse.getProducts();
                mProductAdapter = new ProductAdapter(mListProducts, HomeFragment.this);
                rvProduct.setAdapter(mProductAdapter);

                getProductsByCategory(mListProducts);
            }

            @Override
            public void onFailure(Call<ProductsResponse> call, Throwable t) {

            }
        });
    }
    public void getProductsByCategory(List<Product> mListProducts) {
        List<Product> products = mListProducts.stream()
                .filter(product -> product.getCategory().equals("smartphones"))
                .collect(Collectors.toList());

        mCategoryAdapter = new ProductAdapter(products, HomeFragment.this);
        rvCategory.setAdapter(mCategoryAdapter);
    }

    @Override
    public void onItemClick(int productID) {
        ProductDetailsFragment fragment = ProductDetailsFragment.newInstance(productID + "");
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerProductDetail, fragment)
                .commit();
    }

    int updatePosition = -1;
    @Override
    public void onWishClick(int position) {
        updatePosition = position;
        wishListPresenter.addProductToWishList(mListProducts.get(position));
    }

    @Override
    public void onAddSuccess(Product product) {
        //Do something
        if (updatePosition != -1){
            Product newProduct = product;
            newProduct.setWish(true);
            mListProducts.set(updatePosition,newProduct);
            mProductAdapter.notifyItemChanged(updatePosition);
            updatePosition = -1;
        }
    }
}