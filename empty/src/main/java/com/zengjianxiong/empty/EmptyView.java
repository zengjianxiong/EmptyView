package com.zengjianxiong.empty;


import static com.zengjianxiong.empty.EmptyViewBuilder.CONTENT;
import static com.zengjianxiong.empty.EmptyViewBuilder.EMPTY;
import static com.zengjianxiong.empty.EmptyViewBuilder.ERROR;
import static com.zengjianxiong.empty.EmptyViewBuilder.LOADING;
import static com.zengjianxiong.empty.EmptyViewBuilder.LOADING_CUSTOMIZE;
import static com.zengjianxiong.empty.EmptyViewBuilder.NONE;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.TransitionManager;


public class EmptyView extends ConstraintLayout {

    private final EmptyViewBuilder builder;

    private LinearLayout container;
    private FrameLayout progressLayout;
    private ProgressBar progressBar;
    private ImageView imageView;
    private TextView titleView;
    private TextView textView;
    private Button button;

    public EmptyView(Context context) {
        this(context, null, 0);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        builder = new EmptyViewBuilder(this, attrs);
        inflate(context, R.layout.layout_empty_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        container = findViewById(R.id.empty_layout);
        imageView = findViewById(R.id.empty_icon);
        textView = findViewById(R.id.empty_text);
        titleView = findViewById(R.id.empty_title);
        button = findViewById(R.id.empty_button);
        progressBar = findViewById(R.id.empty_progress_bar);
        progressLayout = findViewById(R.id.empty_progress_layout);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child.getVisibility() == VISIBLE) {
            builder.include(child);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        builder.setOnClickListener(onClickListener);
    }

    public EmptyViewBuilder builder() {
        return builder;
    }

    public EmptyViewBuilder content() {
        return builder.setState(CONTENT);
    }

    public EmptyViewBuilder loading() {
        return loading(LOADING);
    }

    public EmptyViewBuilder loading(int statue) {
        return builder.setState(statue);
    }

    public EmptyViewBuilder empty() {
        return builder.setState(EMPTY);
    }

    public EmptyViewBuilder error() {
        return builder.setState(ERROR);
    }

    public EmptyViewBuilder error(Throwable t) {
        Error error = Error.get(t);
        return error(error);
    }

    public EmptyViewBuilder error(Error error) {

        return error()
                .setErrorTitle(error.getTitle(getContext()))
                .setErrorText(error.getMessage(getContext()));
    }

    @EmptyViewBuilder.State
    public int state() {
        return builder.state;
    }

    public void showContent() {
        content().show();
    }

    public void showLoading() {
        loading().show();
    }

    public void showLoading(View customize) {
        loading(LOADING_CUSTOMIZE).show(customize);
    }

    public void showEmpty() {
        empty().show();
    }

    public void showError() {
        error().show();
    }

    void show(View view) {
        show();
        if (builder.state == LOADING_CUSTOMIZE) {
            addCustomView(view);
        }

    }

    void show() {
        // start animation
        if (builder.transition != null) {
            TransitionManager.beginDelayedTransition(this, builder.transition);
        }
        if (builder.buttonWidth != -100) {
            ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
            layoutParams.width = builder.buttonWidth;
            button.setLayoutParams(layoutParams);
        }

        switch (builder.state) {
            case CONTENT:
                container.setVisibility(View.GONE);
                progressBar.setVisibility(GONE);
                progressLayout.setVisibility(GONE);
                imageView.setVisibility(GONE);
                titleView.setVisibility(GONE);
                textView.setVisibility(GONE);
                button.setVisibility(GONE);
                setChildVisibility(VISIBLE);

                setContainer(Color.TRANSPARENT);
                break;
            case EMPTY:
                container.setVisibility(View.VISIBLE);
                progressBar.setVisibility(GONE);
                progressLayout.setVisibility(GONE);
                imageView.setVisibility(VISIBLE);
                titleView.setVisibility(VISIBLE);
                textView.setVisibility(VISIBLE);
                button.setVisibility(VISIBLE);
                setChildVisibility(GONE);

                setContainer(builder.emptyBackgroundColor);
                setIcon(builder.emptyDrawable, builder.emptyDrawableTint);
                setTitle(builder.emptyTitle, builder.emptyTitleTextColor);
                setText(builder.emptyText, builder.emptyTextColor);
                setButton(builder.emptyButtonText, builder.emptyButtonTextColor,
                        builder.emptyButtonBackgroundColor);
                break;
            case ERROR:
                container.setVisibility(View.VISIBLE);
                progressBar.setVisibility(GONE);
                progressLayout.setVisibility(GONE);
                imageView.setVisibility(VISIBLE);
                titleView.setVisibility(VISIBLE);
                textView.setVisibility(VISIBLE);
                button.setVisibility(VISIBLE);
                setChildVisibility(GONE);

                setContainer(builder.errorBackgroundColor);
                setIcon(builder.errorDrawable, builder.errorDrawableTint);
                setTitle(builder.errorTitle, builder.errorTitleTextColor);
                setText(builder.errorText, builder.errorTextColor);
                setButton(builder.errorButtonText, builder.errorButtonTextColor,
                        builder.errorButtonBackgroundColor);
                break;
            case LOADING:
                container.setVisibility(View.VISIBLE);
                progressBar.setVisibility(VISIBLE);
                progressLayout.setVisibility(VISIBLE);
                imageView.setVisibility(GONE);
                titleView.setVisibility(VISIBLE);
                textView.setVisibility(VISIBLE);
                button.setVisibility(GONE);
                setChildVisibility(GONE);

                setContainer(builder.loadingBackgroundColor);
                setProgressBar(builder.loadingType, builder.loadingDrawableTint);
                setIcon(builder.loadingDrawable, builder.loadingDrawableTint);
                setTitle(builder.loadingTitle, builder.loadingTitleTextColor);
                setText(builder.loadingText, builder.loadingTextColor);
                break;
            case LOADING_CUSTOMIZE:
                container.setVisibility(View.VISIBLE);
                progressLayout.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);
                imageView.setVisibility(GONE);
                titleView.setVisibility(VISIBLE);
                textView.setVisibility(VISIBLE);
                button.setVisibility(GONE);
                setChildVisibility(GONE);

                setContainer(builder.loadingBackgroundColor);
                setProgressBar(builder.loadingType, builder.loadingDrawableTint);
                setIcon(builder.loadingDrawable, builder.loadingDrawableTint);
                setTitle(builder.loadingTitle, builder.loadingTitleTextColor);
                setText(builder.loadingText, builder.loadingTextColor);
                break;
            default:
        }
    }

    private void addCustomView(View view) {
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.gravity = Gravity.CENTER;
//        textView.setLayoutParams(layoutParams);
        progressLayout.addView(view);

    }

    private void setChildVisibility(int visibility) {
        for (View view : builder.children) {
            view.setVisibility(visibility);
        }
    }

    private void setContainer(@ColorInt int backgroundColor) {
        container.setGravity(builder.gravity);
        container.setBackgroundColor(backgroundColor);
    }

    private void setProgressBar(@EmptyViewBuilder.LoadingType int style, @ColorInt int tint) {
        if (progressBar.getVisibility() != VISIBLE) {
            return;
        }
        if (style == NONE) {
            progressBar.setVisibility(GONE);
            return;
        }
        progressBar.setVisibility(VISIBLE);
        if (tint != 0) {
            Drawable drawable = progressBar.getIndeterminateDrawable();
            if (drawable != null) {
                drawable.setColorFilter(tint, Mode.SRC_ATOP);
            }
        }
    }

    private void setIcon(Drawable drawable, @ColorInt int tint) {
        if (imageView.getVisibility() != VISIBLE) {
            return;
        }
        if (drawable == null) {
            imageView.setVisibility(GONE);
            return;
        }
        imageView.setVisibility(VISIBLE);
        imageView.setImageDrawable(drawable);
        imageView.setColorFilter(tint);
    }

    private void setTitle(CharSequence text, @ColorInt int textColor) {
        if (titleView.getVisibility() != VISIBLE) {
            return;
        }
        if (TextUtils.isEmpty(text)) {
            titleView.setVisibility(GONE);
            return;
        }
        titleView.setVisibility(VISIBLE);
        titleView.setText(text);
        titleView.setTextColor(textColor);
        titleView.setTypeface(builder.font);
        if (builder.titleTextSize != 0) {
            EmptyUtils.setTextSize(titleView, builder.titleTextSize);
        }
    }

    private void setText(CharSequence text, @ColorInt int textColor) {
        if (textView.getVisibility() != VISIBLE) {
            return;
        }
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(GONE);
            return;
        }
        textView.setVisibility(VISIBLE);
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTypeface(builder.font);
        if (builder.textSize != 0) {
            EmptyUtils.setTextSize(textView, builder.textSize);
        }
    }

    private void setButton(CharSequence text,
                           @ColorInt int textColor,
                           @ColorInt int backgroundColor) {
        if (button.getVisibility() != VISIBLE) {
            return;
        }
        if (TextUtils.isEmpty(text)) {
            button.setVisibility(GONE);
            return;
        }
        button.setVisibility(VISIBLE);
        button.setText(text);
        button.setTextColor(textColor);
        button.setTypeface(builder.font);
        if (builder.buttonTextSize != 0) {
            EmptyUtils.setTextSize(button, builder.buttonTextSize);
        }
        button.setBackgroundColor(backgroundColor);
        button.setOnClickListener(builder.onClickListener);
    }
}