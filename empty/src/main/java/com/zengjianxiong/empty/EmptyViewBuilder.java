package com.zengjianxiong.empty;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.annotation.StringRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.transition.AutoTransition;
import androidx.transition.Explode;
import androidx.transition.Fade;
import androidx.transition.Slide;
import androidx.transition.Transition;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;


public class EmptyViewBuilder {

    public static final int NONE = 0;

    // Gravity
    public static final int TOP = 48;
    public static final int CENTER = 17;
    public static final int BOTTOM = 80;

    // Loading Type
    public static final int CIRCULAR = 1;

    // State
    public static final int CONTENT = 1;
    public static final int LOADING = 2;
    public static final int EMPTY = 3;
    public static final int ERROR = 4;
    public static final int LOADING_CUSTOMIZE = 5;

    // Transition
    public static final int SLIDE = 1;
    public static final int EXPLODE = 2;
    public static final int FADE = 3;
    public static final int AUTO = 4;

    private final EmptyView emptyView;
    private final Context context;
    List<View> children;
    @State
    int state;
    int gravity;

    // Shared attributes
    float titleTextSize;
    float textSize;
    float buttonTextSize;
    Typeface font;
    Transition transition;
    View.OnClickListener onClickListener;

    int buttonWidth = -100;
    // Loading state attributes
    @LoadingType
    int loadingType;
    CharSequence loadingTitle;
    @ColorInt
    int loadingTitleTextColor;
    CharSequence loadingText;
    @ColorInt
    int loadingTextColor;
    Drawable loadingDrawable;
    @ColorInt
    int loadingDrawableTint;
    @ColorInt
    int loadingBackgroundColor;

    // Empty state attributes
    CharSequence emptyTitle;
    @ColorInt
    int emptyTitleTextColor;
    CharSequence emptyText;
    @ColorInt
    int emptyTextColor;
    CharSequence emptyButtonText;
    @ColorInt
    int emptyButtonTextColor;
    @ColorInt
    int emptyButtonBackgroundColor;
    Drawable emptyDrawable;
    @ColorInt
    int emptyDrawableTint;
    @ColorInt
    int emptyBackgroundColor;

    // Error state attributes
    CharSequence errorTitle;
    @ColorInt
    int errorTitleTextColor;
    CharSequence errorText;
    @ColorInt
    int errorTextColor;
    CharSequence errorButtonText;
    @ColorInt
    int errorButtonTextColor;
    @ColorInt
    int errorButtonBackgroundColor;
    Drawable errorDrawable;
    @ColorInt
    int errorDrawableTint;
    @ColorInt
    int errorBackgroundColor;

    private EmptyViewBuilder(EmptyView emptyView) {
        this.emptyView = emptyView;
        this.context = emptyView.getContext();
        this.children = new ArrayList<>();
    }

    EmptyViewBuilder(EmptyView emptyView,  AttributeSet attributeSet) {
        this(emptyView);

        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.EmptyView);
        try {
            Resources resources = context.getResources();
            int defaultTextColor = resources.getColor(android.R.color.black);

            gravity = a.getInt(R.styleable.EmptyView_ev_gravity, CENTER);
            if (a.hasValue(R.styleable.EmptyView_ev_transition)) {
                setTransition(a.getInt(R.styleable.EmptyView_ev_transition, NONE));
            }
            if (a.hasValue(R.styleable.EmptyView_ev_font)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    font = a.getFont(R.styleable.EmptyView_ev_font);
                } else {
                    int fontResId = a.getResourceId(R.styleable.EmptyView_ev_font, 0);
                    if (fontResId != 0) {
                        font = ResourcesCompat.getFont(context, fontResId);
                    }
                }
            }
            if (a.hasValue(R.styleable.EmptyView_ev_titleTextSize)) {
                titleTextSize = a.getDimension(R.styleable.EmptyView_ev_titleTextSize, 0);
            }
            if (a.hasValue(R.styleable.EmptyView_ev_textSize)) {
                textSize = a.getDimension(R.styleable.EmptyView_ev_textSize, 0);
            }
            if (a.hasValue(R.styleable.EmptyView_ev_buttonTextSize)) {
                buttonTextSize = a.getDimension(R.styleable.EmptyView_ev_buttonTextSize, 0);
            }

            // Loading state attributes
            if (a.hasValue(R.styleable.EmptyView_ev_loading_type)) {
                loadingType = a.getInt(R.styleable.EmptyView_ev_loading_type, CIRCULAR);
            }
            if (a.hasValue(R.styleable.EmptyView_ev_loading_title)) {
                loadingTitle = a.getText(R.styleable.EmptyView_ev_loading_title);
            }
            loadingTitleTextColor = a.getColor(R.styleable.EmptyView_ev_loading_titleTextColor,
                    defaultTextColor);
            if (a.hasValue(R.styleable.EmptyView_ev_loading_text)) {
                loadingText = a.getText(R.styleable.EmptyView_ev_loading_text);
            }
            if (a.hasValue(R.styleable.EmptyView_ev_loading_text)) {
                loadingText = a.getText(R.styleable.EmptyView_ev_loading_text);
            }
            loadingTextColor = a.getColor(R.styleable.EmptyView_ev_loading_textColor, defaultTextColor);
            if (a.hasValue(R.styleable.EmptyView_ev_loading_drawable)) {
                loadingDrawable = a.getDrawable(R.styleable.EmptyView_ev_loading_drawable);
            }
            if (a.hasValue(R.styleable.EmptyView_ev_loading_drawable)) {
                loadingDrawable = a.getDrawable(R.styleable.EmptyView_ev_loading_drawable);
            }
            if (a.hasValue(R.styleable.EmptyView_ev_loading_drawableTint)) {
                loadingDrawableTint = a.getColor(R.styleable.EmptyView_ev_loading_drawableTint, 0);
            }
            if (a.hasValue(R.styleable.EmptyView_ev_loading_backgroundColor)) {
                loadingBackgroundColor = a.getColor(R.styleable.EmptyView_ev_loading_backgroundColor, 0);
            }

            // Empty state attributes
            if (a.hasValue(R.styleable.EmptyView_ev_empty_title)) {
                emptyTitle = a.getText(R.styleable.EmptyView_ev_empty_title);
            }
            emptyTitleTextColor = a.getColor(R.styleable.EmptyView_ev_empty_titleTextColor,
                    defaultTextColor);
            if (a.hasValue(R.styleable.EmptyView_ev_empty_text)) {
                emptyText = a.getText(R.styleable.EmptyView_ev_empty_text);
            }
            emptyTextColor = a.getColor(R.styleable.EmptyView_ev_empty_textColor, defaultTextColor);
            if (a.hasValue(R.styleable.EmptyView_ev_empty_button)) {
                emptyButtonText = a.getText(R.styleable.EmptyView_ev_empty_button);
            }
            emptyButtonTextColor = a.getColor(R.styleable.EmptyView_ev_empty_buttonTextColor,
                    defaultTextColor);
            emptyButtonBackgroundColor = a.getColor(R.styleable.EmptyView_ev_empty_buttonBackgroundColor,
                    0);
            if (a.hasValue(R.styleable.EmptyView_ev_empty_drawable)) {
                emptyDrawable = a.getDrawable(R.styleable.EmptyView_ev_empty_drawable);
            }
            if (a.hasValue(R.styleable.EmptyView_ev_empty_drawableTint)) {
                emptyDrawableTint = a.getColor(R.styleable.EmptyView_ev_empty_drawableTint, 0);
            }
            if (a.hasValue(R.styleable.EmptyView_ev_empty_backgroundColor)) {
                emptyBackgroundColor = a.getColor(R.styleable.EmptyView_ev_empty_backgroundColor, 0);
            }

            // Error state attributes
            if (a.hasValue(R.styleable.EmptyView_ev_error_title)) {
                errorTitle = a.getText(R.styleable.EmptyView_ev_error_title);
            }
            errorTitleTextColor = a.getColor(R.styleable.EmptyView_ev_error_titleTextColor,
                    defaultTextColor);
            if (a.hasValue(R.styleable.EmptyView_ev_error_text)) {
                errorText = a.getText(R.styleable.EmptyView_ev_error_text);
            }
            errorTextColor = a.getColor(R.styleable.EmptyView_ev_error_textColor, defaultTextColor);
            if (a.hasValue(R.styleable.EmptyView_ev_error_button)) {
                errorButtonText = a.getText(R.styleable.EmptyView_ev_error_button);
            }
            errorButtonTextColor = a.getColor(R.styleable.EmptyView_ev_error_buttonTextColor,
                    defaultTextColor);
            errorButtonBackgroundColor = a.getColor(R.styleable.EmptyView_ev_error_buttonBackgroundColor,
                    0);
            if (a.hasValue(R.styleable.EmptyView_ev_error_drawable)) {
                errorDrawable = a.getDrawable(R.styleable.EmptyView_ev_error_drawable);
            }
            if (a.hasValue(R.styleable.EmptyView_ev_error_drawableTint)) {
                errorDrawableTint = a.getColor(R.styleable.EmptyView_ev_error_drawableTint, 0);
            }
            if (a.hasValue(R.styleable.EmptyView_ev_error_backgroundColor)) {
                errorBackgroundColor = a.getColor(R.styleable.EmptyView_ev_error_backgroundColor, 0);
            }
        } finally {
            a.recycle();
        }
    }

    public EmptyViewBuilder setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public EmptyViewBuilder exclude(@IdRes int... ids) {
        for (int id : ids) {
            View view = emptyView.findViewById(id);
            if (children.contains(view)) {
                children.remove(view);
            }
        }
        return this;
    }

    public EmptyViewBuilder exclude(View... views) {
        for (View view : views) {
            if (children.contains(view)) {
                children.remove(view);
            }
        }
        return this;
    }

    public EmptyViewBuilder include(@IdRes int... ids) {
        for (int id : ids) {
            View view = emptyView.findViewById(id);
            if (!children.contains(view)) {
                children.add(view);
            }
        }
        return this;
    }

    public EmptyViewBuilder include(View... views) {
        for (View view : views) {
            if (!children.contains(view)) {
                children.add(view);
            }
        }
        return this;
    }

    public EmptyViewBuilder setTransition(@TransitionType int type) {
        switch (type) {
            case NONE:
            default:
                transition = null;
                break;
            case AUTO:
                transition = new AutoTransition();
                break;
            case EXPLODE:
                transition = new Explode();
                break;
            case FADE:
                transition = new Fade();
                break;
            case SLIDE:
                transition = new Slide();
                break;
        }
        return this;
    }

    public EmptyViewBuilder setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }


    public EmptyViewBuilder setErrorButtonBackgroundColor(@ColorInt int color) {
        this.errorButtonBackgroundColor = color;
        return this;
    }

    public EmptyViewBuilder setEmptyButtonBackgroundColor(@ColorInt int color) {
        this.emptyButtonBackgroundColor = color;
        return this;
    }

    public EmptyViewBuilder setEmptyButtonTextColor(@ColorInt int color) {
        this.emptyButtonTextColor = color;
        return this;
    }

    public EmptyViewBuilder setErrorButtonTextColor(@ColorInt int color) {
        this.errorButtonTextColor = color;
        return this;
    }

    public EmptyViewBuilder setButtonTextSize(int buttonTextSize) {
        this.buttonTextSize = buttonTextSize;
        return this;
    }

    public EmptyViewBuilder setState(@State int state) {
        this.state = state;
        return this;
    }

    public int getState() {
        return state;
    }

    public EmptyViewBuilder setLoadingType(@LoadingType int loadingType) {
        this.loadingType = loadingType;
        return this;
    }

    public EmptyViewBuilder setLoadingTitle(@StringRes int id) {
        return setLoadingTitle(EmptyUtils.getString(context, id));
    }

    public EmptyViewBuilder setLoadingTitle(CharSequence loadingTitle) {
        this.loadingTitle = loadingTitle;
        return this;
    }

    public EmptyViewBuilder setLoadingText(@StringRes int id) {
        return setLoadingText(EmptyUtils.getString(context, id));
    }

    public EmptyViewBuilder setLoadingText(CharSequence loadingText) {
        this.loadingText = loadingText;
        return this;
    }

    public EmptyViewBuilder setLoadingDrawable(@DrawableRes int id) {
        return setLoadingDrawable(EmptyUtils.getDrawable(context, id));
    }

    public EmptyViewBuilder setLoadingDrawable(Drawable loadingDrawable) {
        this.loadingDrawable = loadingDrawable;
        return this;
    }

    public EmptyViewBuilder setEmptyTitle(@StringRes int id) {
        return setEmptyTitle(EmptyUtils.getString(context, id));
    }

    public EmptyViewBuilder setEmptyTitle(CharSequence emptyTitle) {
        this.emptyTitle = emptyTitle;
        return this;
    }

    public EmptyViewBuilder setEmptyText(@StringRes int id) {
        return setEmptyText(EmptyUtils.getString(context, id));
    }

    public EmptyViewBuilder setEmptyText(CharSequence emptyText) {
        this.emptyText = emptyText;
        return this;
    }

    public EmptyViewBuilder setEmptyButtonText(@StringRes int id) {
        return setEmptyButtonText(EmptyUtils.getString(context, id));
    }

    public EmptyViewBuilder setEmptyButtonText(CharSequence emptyButtonText) {
        this.emptyButtonText = emptyButtonText;
        return this;
    }

    public EmptyViewBuilder setEmptyDrawable(@DrawableRes int id) {
        return setEmptyDrawable(EmptyUtils.getDrawable(context, id));
    }

    public EmptyViewBuilder setEmptyDrawable(Drawable emptyDrawable) {
        this.emptyDrawable = emptyDrawable;
        return this;
    }

    public EmptyViewBuilder setErrorTitle(@StringRes int id) {
        return setErrorTitle(EmptyUtils.getString(context, id));
    }

    public EmptyViewBuilder setErrorTitle(CharSequence errorTitle) {
        this.errorTitle = errorTitle;
        return this;
    }

    public EmptyViewBuilder setErrorText(@StringRes int id) {
        return setErrorText(EmptyUtils.getString(context, id));
    }

    public EmptyViewBuilder setErrorText(CharSequence errorText) {
        this.errorText = errorText;
        return this;
    }

    public EmptyViewBuilder setErrorButtonText(@StringRes int id) {
        return setErrorButtonText(EmptyUtils.getString(context, id));
    }

    public EmptyViewBuilder setErrorButtonText(CharSequence errorButtonText) {
        this.errorButtonText = errorButtonText;
        return this;
    }

    public EmptyViewBuilder setErrorDrawable(@DrawableRes int id) {
        return setErrorDrawable(EmptyUtils.getDrawable(context, id));
    }

    public EmptyViewBuilder setErrorDrawable(Drawable errorDrawable) {
        this.errorDrawable = errorDrawable;
        return this;
    }

    public void show() {
//        emptyView.show();
    }

    public void show(View view) {
//        emptyView.show(view);
    }


    public EmptyViewBuilder setButtonWidth(int matchParent) {
        this.buttonWidth = matchParent;
        return this;
    }

    @IntDef({NONE, SLIDE, EXPLODE, FADE, AUTO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TransitionType {
    }

    @IntDef({NONE, CIRCULAR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoadingType {
    }

    @IntDef({CONTENT, EMPTY, ERROR, LOADING, LOADING_CUSTOMIZE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }
}