package com.stxnext.intranet2.fragment;

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

    private OnFlotingMenuItemClickListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_floating_menu, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFlotingMenuItemClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFlotingMenuItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFlotingMenuItemClickListener {

        void onFloatingMenuItemClick(int option);

    }


}
