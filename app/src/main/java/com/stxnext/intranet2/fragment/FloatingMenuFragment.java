package com.stxnext.intranet2.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.MyProfileActivity;

public class FloatingMenuFragment extends Fragment {

    public static final int LATE = 0;
    public static final int HOLIDAY = 1;
    public static final int OUT_OF_OFFICE = 2;
    public static final int ADD_HOURS = 3;

    private static final String VIEW_TRANSITION_ARG = "view_transition_arg";

    private int contentTransition;
    private OnFloatingMenuItemClickListener mListener;
    private boolean isOnSaveInstanceState = false;
    private boolean fragmentPoppedBack = false;

    public static FloatingMenuFragment newInstance(int floatingButtonTransition) {
        FloatingMenuFragment fragment = new FloatingMenuFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(VIEW_TRANSITION_ARG, floatingButtonTransition);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_floating_menu, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFloatingMenuItemClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFloatingMenuItemClickListener");
        }

        if (getArguments() != null) {
            contentTransition = getArguments().getInt(VIEW_TRANSITION_ARG);
        }
    }

    @Override
    public void onResume() {
        isOnSaveInstanceState = false;
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.floating_view_container)
                .animate()
                .alpha(1f)
                .setDuration(300);

        ViewGroup buttons = (ViewGroup) view.findViewById(R.id.floating_buttons_container);
        for (int i = 0; i < buttons.getChildCount(); i++) {
            buttons.getChildAt(i).animate()
                    .setDuration(400)
                    .setStartDelay(100 * i)
                    .alpha(1f)
                    .translationY(contentTransition)
                    .setInterpolator(new OvershootInterpolator());
        }

        TextView lateOptionButton = (TextView) view.findViewById(R.id.floating_late_option);
        TextView addHoursButton = (TextView) view.findViewById(R.id.floating_add_hours);
        TextView outOfOfficeButton = (TextView) view.findViewById(R.id.floating_out_of_office_option);
        TextView absenceButton = (TextView) view.findViewById(R.id.floating_absence_option);

        lateOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFloatingMenuItemClick(LATE);
            }
        });

        addHoursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFloatingMenuItemClick(ADD_HOURS);
            }
        });

        outOfOfficeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFloatingMenuItemClick(OUT_OF_OFFICE);
            }
        });

        absenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFloatingMenuItemClick(HOLIDAY);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void close() {
        mListener.onFloatingMenuClose();
        if (getView() != null) {
            ViewGroup buttons = (ViewGroup) getView().findViewById(R.id.floating_buttons_container);
            for (int i = 0; i < buttons.getChildCount(); i++) {
                buttons.getChildAt(i).animate()
                        .setDuration(300)
                        .setStartDelay(100 * i)
                        .alpha(0f)
                        .translationYBy(-contentTransition / 4)
                        .setInterpolator(new DecelerateInterpolator());
            }

            getView().findViewById(R.id.floating_view_container)
                    .animate()
                    .alpha(0f)
                    .setDuration(300)
                    .setStartDelay(100)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            closeFloatingMenuFragment();
                        }
                    });
        }
    }

    public synchronized void closeFloatingMenuFragment() {
        if (!fragmentPoppedBack && !isOnSaveInstanceState && isFloatingMenuPresent()) {
            fragmentPoppedBack = true;
            getFragmentManager().popBackStackImmediate();
        }
    }

    private boolean isFloatingMenuPresent() {
        return getFragmentManager() != null &&
                getFragmentManager().findFragmentByTag(MyProfileActivity.FLOATING_MENU_TAG) != null;
    }

    @Override
    public void onPause() {
        closeFloatingMenuFragment();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        isOnSaveInstanceState = true;
        super.onSaveInstanceState(outState);
    }

    public interface OnFloatingMenuItemClickListener {

        void onFloatingMenuItemClick(int option);

        void onFloatingMenuClose();

    }


}
