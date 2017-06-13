package com.github.demixdn.weather.ui.cities;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.demixdn.weather.App;
import com.github.demixdn.weather.R;
import com.github.demixdn.weather.data.Observer;
import com.github.demixdn.weather.data.model.City;
import com.github.demixdn.weather.data.model.Weather;
import com.github.demixdn.weather.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CitiesFragment extends Fragment implements CitiesView, CityRemoveListener, Observer {


    private CitiesPresenter presenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CityWeatherAdapter adapter;

    private ProgressDialog progressDialog;

    public CitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        App.getInstance().getAppComponent().inject(this);
        initAdapter();
        getPresenter().loadWeather();
    }

    @Override
    public void onDestroyView() {
        getPresenter().unbindView();
        super.onDestroyView();
    }

    private void initUI(View root) {
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorMaterial400,
                R.color.colorMaterial300,
                R.color.colorMaterial200,
                R.color.colorMaterial100);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (presenter != null) {
                    presenter.loadWeather();
                }
            }
        });
        recyclerView = (RecyclerView) root.findViewById(R.id.rvCityItems);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Load weather for cities");
    }

    private void initAdapter() {
        adapter = new CityWeatherAdapter(new ArrayList<Weather>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(getContext(), R.drawable.ic_clear);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = getResources().getDimensionPixelSize(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                CityWeatherAdapter testAdapter = (CityWeatherAdapter) recyclerView.getAdapter();
                if (testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                CityWeatherAdapter adapter = (CityWeatherAdapter) recyclerView.getAdapter();
                adapter.pendingRemoval(swipedPosition);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                if (viewHolder.getAdapterPosition() == -1) {
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setUpAnimationDecoratorHelper() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                if (parent.getItemAnimator().isRunning()) {

                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    int left = 0;
                    int right = parent.getWidth();

                    int top = 0;
                    int bottom = 0;

                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

    private void showSwipeLayout() {
        if (!swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(true);
    }

    private void hideSwipeLayout() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.destroyDrawingCache();
            swipeRefreshLayout.clearAnimation();
        }
    }

    @Override
    public void showProgress() {
        showSwipeLayout();
    }

    @Override
    public void hideProgress() {
        hideSwipeLayout();
    }

    @Override
    public void showError(String errorMessage) {
        hideProgress();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.dialog_error_title);
        builder.setMessage(R.string.dialog_error_default_message_retry);
        builder.setPositiveButton(R.string.dialog_action_retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getPresenter().loadWeather();
                dialog.dismiss();
            }
        });
        builder.setNeutralButton(R.string.dialog_action_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void bindPresenter(CitiesPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public CitiesPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showWeather(List<Weather> weathers) {
        adapter.addItems(weathers);
    }

    @Override
    public void onCityRemoved(@NonNull City removedCity) {
        getPresenter().onCityRemove(removedCity);
    }

    @Override
    public void update() {
        Logger.d("Observable update CitiesFragment");
        App.getInstance().getAppComponent().inject(this);
    }
}
