package com.example.android53_d10_btvn.presenter;

import android.content.Context;

import com.example.android53_d10_btvn.interfaces.IWishPresenter;
import com.example.android53_d10_btvn.interfaces.IWishView;
import com.example.android53_d10_btvn.model.Product;
import com.example.android53_d10_btvn.model.WishListInteractor;

import java.util.List;

public class WishListPresenter implements IWishPresenter {
    private IWishView iView;
    private WishListInteractor wishListInteractor;

    public WishListPresenter(Context context, IWishView iView) {
        this.iView = iView;
        wishListInteractor = new WishListInteractor(context, this);
    }

    public void addProductToWishList(Product product) {
        wishListInteractor.addToWishlist(product);
    }

    public List<Product> getWishList() {
        return wishListInteractor.getWishlistItems();
    }

    @Override
    public void onAddSuccess(Product product) {
        iView.onAddSuccess(product);
    }
}