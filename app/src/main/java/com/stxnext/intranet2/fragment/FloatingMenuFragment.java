package com.stxnext.intranet2.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stxnext.intranet2.R;

public class FloatingMenuFragment extends Fragment {

    public static final int LATE = 0;
    public static final int HOLIDAY = 1;
    public static final int OUT_OF_OFFICE = 2;

    private OnFloatingMenuItemClickListener mListener;

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
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.floating_view_container).animate().alpha(1f).setDuration(300);
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
            getView().findViewById(R.id.floating_view_container)
                    .animate()
                    .alpha(0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (getFragmentManager() != null) {
                                getFragmentManager().popBackStackImmediate();
                            }
                        }
                    });
        }
    }

    public interface OnFloatingMenuItemClickListener {

        void onFloatingMenuItemClick(int option);

        void onFloatingMenuClose();

    }


}
