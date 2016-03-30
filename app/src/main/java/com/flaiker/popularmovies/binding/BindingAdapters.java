/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies.binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Class for static definitions of {@link BindingAdapter BindingAdapters}.
 */
public abstract class BindingAdapters {
    /**
     * Load images with Picasso if an ImageView has an app:imageUrl attribute.
     * <p/>
     * Loads app:error drawable if image could not be loaded.
     */
    @BindingAdapter({"bind:imageUrl", "bind:error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        Picasso.with(view.getContext()).load(url).error(error).into(view);
    }
}
