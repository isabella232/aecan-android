package com.aeternity.aecan.dataBindingComponents;

import android.graphics.PorterDuff;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.aeternity.aecan.R;
import com.aeternity.aecan.adapters.ListAdapter;
import com.aeternity.aecan.models.Positions;

import java.util.ArrayList;

import java.util.ArrayList;

public class DataBindingAdapter {

    @BindingAdapter("textSimpleInformation")
    public static void setTextSimpleInformation(TextView textView, String resource) {
        if (resource != null && !resource.isEmpty()) {
            String[] separated = resource.split(":");
            if (separated[0] != null) {
                Spannable spannable = new SpannableString(resource);
                spannable.setSpan(new ForegroundColorSpan(textView.getContext().getResources().getColor(R.color.lightGray888888)), 0, separated[0].length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannable, TextView.BufferType.SPANNABLE);
            }
        }
    }

    @BindingAdapter("imageResource")
    public static void setImageResource(ImageView imageView, int resource) {
        if (resource != 0) {
            Glide.with(imageView.getContext())
                    .load(resource)
                    .into(imageView);
        }
    }

    @BindingAdapter("urlImageResource")
    public static void setUrlImageResource(ImageView imageView, String resource) {
        if (!resource.isEmpty()) {
            Glide.with(imageView.getContext())
                    .load(resource)
                    .into(imageView);
        }
    }

    @BindingAdapter("imageUri")
    public static void setImageUri(ImageView imageView, Uri resource) {
        if (resource != null) {
            Glide.with(imageView.getContext())
                    .load(resource)
                    .into(imageView);
        }
    }

    @BindingAdapter("imageResourceCircle")
    public static void setCircleImageResource(ImageView imageView, int resource) {
        if (resource != 0) {
            Glide.with(imageView.getContext())
                    .load(resource)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
        }
    }

    @BindingAdapter("setBackgroundDrawable")
    public static void setBackgroundDrawable(View v, int resource) {
        v.setBackground(v.getContext().getResources().getDrawable(resource));
    }

    @BindingAdapter("setSrcDrawable")
    public static void setSrcDrawable(ImageView imageView, int resource) {
        imageView.setImageDrawable(imageView.getContext().getResources().getDrawable(resource));
    }


    @BindingAdapter("setTextColor")
    public static void setTextColor(TextView v, int resource) {
        v.setTextColor(v.getContext().getResources().getColor(resource));
    }

    @BindingAdapter("reviewCircleImage")
    public static void setReviewCircleImage(ImageView imageView, String resource) {
        if (resource != null) Glide.with(imageView.getContext())
                .load(resource)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(imageView.getContext().getResources().getDrawable(R.drawable.btn_blue_pressed))
                .into(imageView);
    }

    @BindingAdapter("imageTinte")
    public static void setTintColor(ImageView imageView, int resource) {
        if (resource != 0) {
            imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), resource), PorterDuff.Mode.SRC_IN);
        }
    }

    @BindingAdapter(value = {"formatterArgument", "setFormatter"})
    public static void setText(TextView textView, String text, String resourceId) {
        String argument = text != null && !text.isEmpty() ? text : "";
        textView.setText(String.format(resourceId, argument));
    }

    @BindingAdapter(value = {"formatterFirstArgument", "formatterSecondArgument", "setFormatterTwoArgs"})
    public static void setTextTwoParams(TextView textView, String text1, String text2, String resourceId) {
        String argument1 = text1 != null && !text1.isEmpty() ? text1 : "";
        String argument2 = text2 != null && !text2.isEmpty() ? text2 : "";
        textView.setText(Html.fromHtml(String.format(resourceId, argument1, argument2)), TextView.BufferType.SPANNABLE);
    }


    @BindingAdapter("invisibleFirst")
    public static void invisibleFirst(View view, Positions position) {
        if (position.equals(Positions.FIRST)) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter("invisibleLast")
    public static void invisibleLast(View view, Positions position) {
        if (position.equals(Positions.LAST)) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter(value = {"textFormatter", "greenText"})
    public static void setAttributeableGreenText(TextView textView, String text, ArrayList<String> greenTextArray) {
        final int WORD_NOT_EXIST = -1;
        if (text == null || greenTextArray == null)
            return;

        Spannable spannable = new SpannableString(text);
        for (String word : greenTextArray) {
            if (!word.isEmpty()) {
                int position = text.indexOf(word);
                if (position != WORD_NOT_EXIST) {
                    spannable.setSpan(new ForegroundColorSpan(textView.getContext().getResources().getColor(R.color.greenLight)), position,
                            position + word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        textView.setText(spannable);
    }

    @BindingAdapter("textViewColor")
    public static void setTextViewColor(TextView textView, Integer resource) {
        if (resource != null) {
            textView.setTextColor(textView.getContext().getResources().getColor(resource));
        }
    }

    @BindingAdapter("shorOrHideView")
    public static void setViewVisibility(View view, boolean visibility) {
        if (visibility) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("showListSize")
    public static void getListSize(TextView textView, ArrayList<Integer> items) {
        if (items.isEmpty()) {
            textView.setText("-");
        } else {
            textView.setText(String.valueOf(items.size()));
        }
    }

    @BindingAdapter("margin_left")
    public static void setLeftMargin(View view, float leftMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(Math.round(leftMargin), layoutParams.topMargin,
                layoutParams.rightMargin, layoutParams.bottomMargin);
        view.setLayoutParams(layoutParams);
    }

}