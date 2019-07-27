package com.gon.kineapp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.gon.kineapp.GlideApp;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ImageLoader {

    private final RequestBuilder<Drawable> imageLoader;

    private RequestOptions requestOptions;

    private ImageLoader(RequestBuilder<Drawable> load) {
        this.imageLoader = load;
    }

    private static RequestBuilder<Drawable> loadWithFragment(Fragment fragment, String url) {
        return GlideApp.with(fragment).load(url);
    }

    private static RequestBuilder<Drawable> loadWithFragment(Fragment fragment, Uri url) {
        return GlideApp.with(fragment).load(url);
    }

    private static RequestBuilder<Drawable> loadWithActivity(Activity activity, String url) {
        return GlideApp.with(activity).load(url);
    }

    private static RequestBuilder<Drawable> loadWithActivity(Activity activity, Uri url) {
        return GlideApp.with(activity).load(url);
    }

    public static ImageLoader load(Fragment fragment, Uri url) {
        return new ImageLoader(loadWithFragment(fragment, url));
    }

    public static ImageLoader load(Activity activity, Uri url) {
        return new ImageLoader(loadWithActivity(activity, url));
    }

    public static ImageLoader load(Fragment fragment, String url) {
        return new ImageLoader(loadWithFragment(fragment, url));
    }

    public static ImageLoader load(Activity activity, String url) {
        return new ImageLoader(loadWithActivity(activity, url));
    }

    public static ImageLoader load(Context context, @NonNull String url) {
        return new ImageLoader(GlideApp.with(context).load(url));
    }

    public static ImageLoader load(Context context, @NonNull Bitmap bitmap) {
        return new ImageLoader(GlideApp.with(context).load(bitmap));
    }

    private void createOptions() {
        if (this.requestOptions == null) {
            this.requestOptions = new RequestOptions();
        }
    }

    public ImageLoader size(int width, int height) {
        createOptions();
        this.requestOptions.override(width, height);
        return this;
    }

    public ImageLoader grayScaleCircle() {
        createOptions();
        List<Transformation<Bitmap>> transformations = new ArrayList<>();
        transformations.add(new GrayscaleTransformation());
        transformations.add(new CircleCrop());
        MultiTransformation<Bitmap> multi = new MultiTransformation<>(transformations);
        this.requestOptions.apply(bitmapTransform(multi));
        return this;
    }

    public ImageLoader grayScale() {
        createOptions();
        this.requestOptions.apply(bitmapTransform(new GrayscaleTransformation()));
        return this;
    }

    public ImageLoader blurCircle() {
        createOptions();
        List<Transformation<Bitmap>> transformations = new ArrayList<>();
        transformations.add(new BlurTransformation());
        transformations.add(new CircleCrop());
        MultiTransformation<Bitmap> multi = new MultiTransformation<>(transformations);
        this.requestOptions.apply(bitmapTransform(multi));
        return this;
    }

    public ImageLoader blur(int radius) {
        createOptions();
        List<Transformation<Bitmap>> transformations = new ArrayList<>();
        transformations.add(new BlurTransformation(radius));
        MultiTransformation<Bitmap> multi = new MultiTransformation<>(transformations);
        this.requestOptions.apply(bitmapTransform(multi));
        return this;
    }

    public ImageLoader centerInside() {
        createOptions();
        this.requestOptions.centerInside();
        return this;
    }

    public ImageLoader centerCrop() {
        createOptions();
        this.requestOptions.centerCrop();
        return this;
    }

    public ImageLoader circle() {
        createOptions();
        this.requestOptions.circleCrop();
        return this;
    }

    public ImageLoader placeHolder(int resourceId) {
        createOptions();
        this.requestOptions.placeholder(resourceId);
        return this;
    }

    public ImageLoader into(ImageView imageView) {
        if (requestOptions != null) {
            this.imageLoader.apply(this.requestOptions);
        }
        this.imageLoader.into(imageView);
        return this;
    }

    public ImageLoader listener(RequestListener<Drawable> requestListener) {
        this.imageLoader.listener(requestListener);
        return this;
    }

    public static void stopLoading(Context context, ImageView imageView) {
        RequestManager requestManager = GlideApp.with(context);
        requestManager.clear(imageView);
    }
}
