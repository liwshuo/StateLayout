package com.baymax.statelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;

import java.util.Map;

/**
 * Created by lishuo on 28,May,2019
 */
public class StateLayout extends FrameLayout implements View.OnClickListener {
    public static final String LOADING = "LOADING";
    public static final String OFFLINE = "OFFLINE";
    public static final String EMPTY = "EMPTY";
    public static final String ERROR = "ERROR";
    public static final String CONTENT = "CONTENT";

    private Map<String, View> stateViewMap = new ArrayMap<>();
    private Map<View, String> viewStateMap = new ArrayMap<>();
    private Map<String, OnStateClickListener> listenerMap = new ArrayMap<>();
    private String currentState;
    private View dummyView;

    private boolean useInTransition;
    private boolean useOutTransition;
    private TransitionSet inTransition;
    private TransitionSet outTransition;

    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dummyView = LayoutInflater.from(context).inflate(R.layout.widget_layout_state_layout_dummy, this, false);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StateLayout, defStyleAttr, 0);
        setLoadingViewSrc(array.getResourceId(R.styleable.StateLayout_loadingSrc, -1));
        setContentViewSrc(array.getResourceId(R.styleable.StateLayout_contentSrc, -1));
        setEmptyViewSrc(array.getResourceId(R.styleable.StateLayout_emptySrc, -1));
        setErrorViewSrc(array.getResourceId(R.styleable.StateLayout_errorSrc, -1));
        setOfflineViewSrc(array.getResourceId(R.styleable.StateLayout_offlineSrc, -1));
        useInTransition = array.getBoolean(R.styleable.StateLayout_useInTransition, true);
        useOutTransition = array.getBoolean(R.styleable.StateLayout_useOutTransition, false);
        array.recycle();

        inTransition = new TransitionSet()
                .addTransition(new Fade(Fade.IN))
                .setDuration(300)
                .setInterpolator(new AccelerateInterpolator());
        outTransition = new TransitionSet()
                .addTransition(new Fade(Fade.OUT))
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator());
    }

    public void useInTransition(boolean use) {
        useInTransition = use;
    }

    public void useOutTransition(boolean use) {
        useOutTransition = use;
    }

    public void setInTransition(TransitionSet transition) {
        inTransition = transition;
    }

    public void setOutTransition(TransitionSet transition) {
        outTransition = transition;
    }

    public void setLoadingView(View view) {
        setCustomView(LOADING, view);
    }

    public void setLoadingViewSrc(@LayoutRes int layoutId) {
        setCustomViewSrc(LOADING, layoutId);
    }

    public void setOfflineView(View view) {
        setCustomView(OFFLINE, view);
    }

    public void setOfflineViewSrc(@LayoutRes int layoutId) {
        setCustomViewSrc(OFFLINE, layoutId);
    }

    public void setEmptyView(View view) {
        setCustomView(EMPTY, view);
    }

    public void setEmptyViewSrc(@LayoutRes int layoutId) {
        setCustomViewSrc(EMPTY, layoutId);
    }

    public void setErrorView(View view) {
        setCustomView(ERROR, view);
    }

    public void setErrorViewSrc(@LayoutRes int layoutId) {
        setCustomViewSrc(ERROR, layoutId);
    }

    public void setContentView(View view) {
        setCustomView(CONTENT, view);
    }

    public void setContentViewSrc(@LayoutRes int layoutId) {
        setCustomViewSrc(CONTENT, layoutId);
    }

    public void setCustomView(String state, View view) {
        stateViewMap.put(state, view);
        viewStateMap.put(view, state);
        view.setVisibility(GONE);
        if (view.getParent() == this) {
            return;
        }
        addView(view);
        if (listenerMap.containsKey(state)) {
            view.setOnClickListener(this);
        }
    }

    public void setCustomViewSrc(String state, @LayoutRes int layoutId) {
        if (layoutId == -1) {
            setCustomView(state, dummyView);
        } else {
            setCustomView(state, LayoutInflater.from(getContext()).inflate(layoutId, this, false));
        }
    }

    public void setOfflineClickListener(OnStateClickListener listener) {
        setCustomStateListener(OFFLINE, listener);
    }

    public void setEmptyClickListener(OnStateClickListener listener) {
        setCustomStateListener(EMPTY, listener);

    }

    public void setErrorClickListener(OnStateClickListener listener) {
        setCustomStateListener(ERROR, listener);
    }

    public void setCustomStateListener(String state, OnStateClickListener listener) {
        listenerMap.put(state, listener);
        if (stateViewMap.containsKey(state)) {
            stateViewMap.get(state).setOnClickListener(this);
        }
    }

    public void showLoading() {
        show(LOADING);
    }

    public void showContent() {
        show(CONTENT);
    }

    public void showOffline() {
        show(OFFLINE);
    }

    public void showEmpty() {
        show(EMPTY);
    }

    public void showError() {
        show(ERROR);
    }

    public void show(String state) {
        if (state == null || state.equals(currentState)) {
            return;
        }
        if (stateViewMap.containsKey(currentState)) {
            if (useOutTransition) {
                TransitionSet outTransitionSet = new TransitionSet()
                        .addTransition(outTransition)
                        .addTarget(stateViewMap.get(currentState));
                TransitionManager.beginDelayedTransition(this, outTransitionSet);
            }
            stateViewMap.get(currentState).setVisibility(GONE);
        }
        if (stateViewMap.containsKey(state)) {
            if (useInTransition) {
                TransitionSet inTransitionSet = new TransitionSet()
                        .addTransition(inTransition)
                        .addTarget(stateViewMap.get(state));
                TransitionManager.beginDelayedTransition(this, inTransitionSet);
            }
            stateViewMap.get(state).setVisibility(VISIBLE);
        }
        currentState = state;
    }

    @Override
    public void onClick(View v) {
        String state = viewStateMap.get(v);
        OnStateClickListener listener = listenerMap.get(state);
        if (listener != null) {
            listener.onClick(v, state);
        }
    }

    public interface OnStateClickListener {
        void onClick(View v, String state);
    }

}
