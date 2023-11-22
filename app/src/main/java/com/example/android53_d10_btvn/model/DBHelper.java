package com.example.android53_d10_btvn.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.android53_d10_btvn.interfaces.IWishInteractor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "product.db";
    private static final int VERSION = 1;
    public static final String TABLE_NAME = "product";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_TITLE = "title";
    public static final String PRODUCT_DES = "description";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_DISCOUNT = "discountPercentage";
    public static final String PRODUCT_RATING = "rating";
    public static final String PRODUCT_STOCK = "stock";
    public static final String PRODUCT_BRAND = "brand";
    public static final String PRODUCT_CATEGORY = "category";
    public static final String PRODUCT_THUMBNAIL = "thumbnail";
    public static final String PRODUCT_IMAGES = "images";
    public IWishInteractor interactor;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public void setInteractor(IWishInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" + PRODUCT_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + PRODUCT_TITLE + " TEXT NOT NULL," + PRODUCT_DES + " TEXT NOT NULL," + PRODUCT_PRICE + " TEXT NOT NULL," + PRODUCT_DISCOUNT + " TEXT NOT NULL,"
                + PRODUCT_RATING + " TEXT NOT NULL,"
                + PRODUCT_STOCK + " TEXT NOT NULL,"
                + PRODUCT_BRAND + " TEXT NOT NULL,"
                + PRODUCT_CATEGORY + " TEXT NOT NULL,"
                + PRODUCT_THUMBNAIL + " TEXT NOT NULL,"
                + PRODUCT_IMAGES + " TEXT)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public void addProduct(Product product) {
        if (product != null) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PRODUCT_TITLE, product.getTitle());
            contentValues.put(PRODUCT_DES, product.getDescription());
            contentValues.put(PRODUCT_PRICE, product.getPrice());
            contentValues.put(PRODUCT_DISCOUNT, product.getDiscountPercentage());
            contentValues.put(PRODUCT_RATING, product.getRating());
            contentValues.put(PRODUCT_STOCK, product.getStock());
            contentValues.put(PRODUCT_BRAND, product.getBrand());
            contentValues.put(PRODUCT_CATEGORY, product.getCategory());
            contentValues.put(PRODUCT_THUMBNAIL, product.getThumbnail());
            // convert list sang gson
            Gson gson = new Gson();
            Type typeToken = new TypeToken<List<String>>() {
            }.getType();
            String data = gson.toJson(product.getImages(), typeToken);
            contentValues.put(PRODUCT_IMAGES, data);

            long response = db.insert(TABLE_NAME, null, contentValues);
            db.close();

            if (interactor != null) {
                if (response > -1) {
                    interactor.onAddProduct(true, product);
                } else {
                    interactor.onAddProduct(false, product);
                }
            }
            return;
        }
        if (interactor != null) {
            interactor.onAddProduct(false, null);
        }
    }

    public boolean updateProduct(int productId, Product product) {
        if (productId >= 0 && product != null) {
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = PRODUCT_ID + " =?";
            String[] whereArgs = {productId + ""}; // điều kiện so sánh của hàng
            ContentValues contentValues = new ContentValues();
            contentValues.put(PRODUCT_TITLE, product.getTitle());
            contentValues.put(PRODUCT_DES, product.getDescription());
            contentValues.put(PRODUCT_PRICE, product.getPrice());
            contentValues.put(PRODUCT_DISCOUNT, product.getDiscountPercentage());
            contentValues.put(PRODUCT_RATING, product.getRating());
            contentValues.put(PRODUCT_STOCK, product.getStock());
            contentValues.put(PRODUCT_BRAND, product.getBrand());
            contentValues.put(PRODUCT_CATEGORY, product.getCategory());
            contentValues.put(PRODUCT_THUMBNAIL, product.getThumbnail());

            Gson gson = new Gson();
            Type typeToken = new TypeToken<List<String>>() {
            }.getType();
            String data = gson.toJson(product.getImages(), typeToken);
            contentValues.put(PRODUCT_IMAGES, data);
            db.update(TABLE_NAME, contentValues, whereClause, whereArgs);
            db.close();
            return true;
        }
        return false;
    }

    public boolean deleteProduct(int productId) {
        if (productId >= 0) {
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = PRODUCT_ID + " =?";
            String[] whereArgs = {productId + ""}; // điều kiện so sánh của hàng

            db.delete(TABLE_NAME, whereClause, whereArgs);
            db.close();
            return true;
        }
        return false;
    }

    @SuppressLint("Range") // để không phải nhớ chỉ số hàng
    public List<Product> getProducts() {
        List<Product> listProduct = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Product product = new Product();
//                productModel.setId(cursor.getInt(0));
                product.setId(cursor.getInt(cursor.getColumnIndex(PRODUCT_ID)));
                product.setTitle(cursor.getString(cursor.getColumnIndex(PRODUCT_TITLE)));
                product.setDescription(cursor.getString(cursor.getColumnIndex(PRODUCT_DES)));

                int price = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCT_PRICE)));
                product.setPrice(price);

                double discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCT_DISCOUNT)));
                product.setDiscountPercentage(discount);

                double rating = Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCT_RATING)));
                product.setRating(rating);

                int stock = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCT_STOCK)));
                product.setStock(stock);

                product.setBrand(cursor.getString(cursor.getColumnIndex(PRODUCT_BRAND)));
                product.setCategory(cursor.getString(cursor.getColumnIndex(PRODUCT_CATEGORY)));
                product.setThumbnail(cursor.getString(cursor.getColumnIndex(PRODUCT_THUMBNAIL)));

                Gson gson = new Gson();
                Type typeToken = new TypeToken<List<String>>() {}.getType();
                List<String> data = gson.fromJson(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGES)), typeToken);
                product.setImages(data);

                listProduct.add(product);
            }
        }
        db.close();
        return listProduct;
    }

    @SuppressLint("Range")
    public List<Product> getProductsByTitle(String title) {
        List<Product> listProduct = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + PRODUCT_TITLE + " LIKE '%" + title + "%'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(PRODUCT_ID)));
                product.setTitle(cursor.getString(cursor.getColumnIndex(PRODUCT_TITLE)));
                product.setDescription(cursor.getString(cursor.getColumnIndex(PRODUCT_DES)));

                int price = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCT_PRICE)));
                product.setPrice(price);

                double discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCT_DISCOUNT)));
                product.setDiscountPercentage(discount);

                double rating = Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCT_RATING)));
                product.setRating(rating);

                int stock = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCT_STOCK)));
                product.setStock(stock);

                product.setBrand(cursor.getString(cursor.getColumnIndex(PRODUCT_BRAND)));
                product.setCategory(cursor.getString(cursor.getColumnIndex(PRODUCT_CATEGORY)));
                product.setThumbnail(cursor.getString(cursor.getColumnIndex(PRODUCT_THUMBNAIL)));

                Gson gson = new Gson();
                Type typeToken = new TypeToken<List<String>>() {}.getType();
                List<String> data = gson.fromJson(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGES)), typeToken);
                product.setImages(data);

                listProduct.add(product);
            }
        }
        db.close();
        return listProduct;
    }
}