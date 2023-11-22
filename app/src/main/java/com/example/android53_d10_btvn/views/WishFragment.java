package com.example.android53_d10_btvn.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android53_d10_btvn.R;
import com.example.android53_d10_btvn.adapter.ProductAdapter;
import com.example.android53_d10_btvn.interfaces.IClickListener;
import com.example.android53_d10_btvn.interfaces.IWishView;
import com.example.android53_d10_btvn.model.Product;
import com.example.android53_d10_btvn.presenter.WishListPresenter;

import java.util.List;

public class WishFragment extends Fragment implements IWishView, IClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ProductAdapter mProductAdapter;
    private RecyclerView rvProduct;
    private List<Product> mListProducts;
    private WishListPresenter presenter;

    public WishFragment() {
        // Required empty public constructor
    }
    public static WishFragment newInstance(String param1, String param2) {
        WishFragment fragment = new WishFragment();
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
        return inflater.inflate(R.layout.fragment_wish, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        initView(view);
    }

    private void initData() {
        presenter = new WishListPresenter( getContext(),this);
    }

    private void initView(View view) {
        rvProduct = view.findViewById(R.id.rvProduct);

        mListProducts = presenter.getWishList();
        mProductAdapter = new ProductAdapter(mListProducts, this);
        rvProduct.setAdapter(mProductAdapter);
    }

    @Override
    public void onAddSuccess(Product product) {
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onWishClick(int position) {
        presenter.addProductToWishList(mListProducts.get(position));
    }
}