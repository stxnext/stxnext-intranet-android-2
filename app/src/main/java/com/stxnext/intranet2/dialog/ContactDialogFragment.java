package com.stxnext.intranet2.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stxnext.intranet2.R;

/**
 * Created by Tomasz Konieczny on 2015-06-08.
 */
public class ContactDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private static final int CALL = R.string.call;
    private static final int SEND_MAIL = R.string.send_email;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.contact_with) + " Agata");
        builder.setAdapter(new ContactOptionsListAdapter(getActivity()), this);
        builder.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    private class ContactOptionsListAdapter extends ArrayAdapter<Integer> {

        public ContactOptionsListAdapter(Context context) {
            super(context, -1, new Integer[] {CALL, SEND_MAIL});
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            ((TextView) convertView).setText(getItem(position));

            return convertView;
        }
    }
}
