package com.stxnext.intranet2.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.model.Office;

/**
 * Created by Tomasz Konieczny on 2015-06-11.
 */
public class OfficeInfoFragment extends Fragment {

    private static final String ARG_OFFICE = "office";

    public static OfficeInfoFragment newInstance(Office office) {
        OfficeInfoFragment fragment = new OfficeInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_OFFICE, office);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_info, container, false);

        if (getArguments() != null) {
            final Office office = (Office) getArguments().getSerializable(ARG_OFFICE);

            if (office != null) {
                TextView locationTextView = (TextView) view.findViewById(R.id.location_text_view);
                locationTextView.setText(office.getCity());

                TextView streetTextView = (TextView) view.findViewById(R.id.street_text_view);
                streetTextView.setText(office.getStreet());

                TextView telTextView = (TextView) view.findViewById(R.id.tel_text_view);
                telTextView.setText(office.getTel());

                if (office.getFax().length() > 0) {
                    TextView faxTextView = (TextView) view.findViewById(R.id.fax_text_view);
                    faxTextView.setText(office.getFax());
                } else {
                    view.findViewById(R.id.fax_container).setVisibility(View.GONE);
                }

                view.findViewById(R.id.call_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                        phoneIntent.setData(Uri.parse("tel:" + office.getTel()));
                        startActivity(phoneIntent);
                    }
                });

                view.findViewById(R.id.map_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?q=loc:"
                                        + office.getLat() + ", " + office.getLon()));
                        startActivity(intent);
                    }
                });

                view.findViewById(R.id.navigation_buttons).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(
                                "http://maps.google.com/maps?daddr="
                                        + office.getLat() + ", " + office.getLon()));
                        startActivity(intent);
                    }
                });
            }
        }

        return view;
    }

}
