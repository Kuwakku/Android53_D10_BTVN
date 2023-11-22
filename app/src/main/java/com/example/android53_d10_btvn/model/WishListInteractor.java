package com.example.android53_d10_btvn.model;

import android.content.Context;

import com.example.android53_d10_btvn.interfaces.IWishInteractor;
import com.example.android53_d10_btvn.interfaces.IWishPresenter;

import java.util.List;

public class WishListInteractor implements IWishInteractor {
    DBHelper dbHelper;

    private IWishPresenter iWishPresenterIpml;

    public WishListInteractor(Context context, IWishPresenter iWishPresenterIpml) {
        this.iWishPresenterIpml = iWishPresenterIpml;
        dbHelper = new DBHelper(context);
        dbHelper.setInteractor(this);
    }

    public void addToWishlist(Product product) {
        // Thêm item vào cơ sở dữ liệu
        dbHelper.addProduct(product);
    }

    public void removeFromWishlist(Product product) {
        // Xóa item khỏi cơ sở dữ liệu
        dbHelper.deleteProduct(product.getId());
    }
    public List<Product> getWishlistItems() {
        // Truy vấn cơ sở dữ liệu và trả về danh sách item yêu thích
        return dbHelper.getProducts();
    }

    @Override
    public void onAddProduct(boolean result,Product product) {
        if (result) {
            iWishPresenterIpml.onAddSuccess(product);
        }
    }
}