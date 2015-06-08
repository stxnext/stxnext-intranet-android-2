package com.stxnext.intranet2.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.stxnext.intranet2.R;

/**
 * Created by Tomasz Konieczny on 2015-06-08.
 */
public class ContactDialogFragment extends DialogFragment {

    private static final String ARG_FIRST_NAME = "first_name";
    private static final String ARG_PHONE_NUMBER = "phone_number";
    private static final String ARG_EMAIL = "email";

    private static final int SEND_MAIL = R.string.send_email;
    private static final int CALL = R.string.call;
    private static final int SEND_SMS = R.string.send_sms;

    public static void show(FragmentManager fragmentManager, String name, String phone, String email) {
        ContactDialogFragment dialogFragment = new ContactDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_FIRST_NAME, name);
        bundle.putSerializable(ARG_PHONE_NUMBER, phone);
        bundle.putSerializable(ARG_EMAIL, email);

        dialogFragment.setArguments(bundle);

        dialogFragment.show(fragmentManager, "contact_dialog");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            throw new RuntimeException("Cannot show dialog without arguments");
        }

        final String name = arguments.getString(ARG_FIRST_NAME);
        final String phone = arguments.getString(ARG_PHONE_NUMBER);
        final String email = arguments.getString(ARG_EMAIL);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(String.format("%s %s", getString(R.string.contact_with), name));

        CharSequence[] items;
        if ("null".equals(phone)) {
            items = new CharSequence[]{getString(SEND_MAIL)};
        } else {
            items = new CharSequence[]{getString(SEND_MAIL),getString(CALL), getString(SEND_SMS)};
        }

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                        startActivity(Intent.createChooser(emailIntent, ""));
                        break;
                    case 1:
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                        phoneIntent.setData(Uri.parse("tel:" + phone));
                        startActivity(phoneIntent);
                        break;
                    case 2:
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.setData(Uri.parse("sms:" + phone));
                        startActivity(sendIntent);
                        break;
                }
            }
        });

        builder.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }

}
