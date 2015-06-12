package com.stxnext.intranet2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.model.Office;

/**
 * Created by Tomasz Konieczny on 2015-06-11.
 */
public class OfficeInfoFragment extends Fragment {

    private static final String ARG_OFFICE = "office";
    private static final String TAG_MAP = "map";

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
            Office office = (Office) getArguments().getSerializable(ARG_OFFICE);

            if (office != null) {
                TextView locationTextView = (TextView) view.findViewById(R.id.location_text_view);
                locationTextView.setText(office.getCity());

                TextView streetTextView = (TextView) view.findViewById(R.id.street_text_view);
                streetTextView.setText(office.getStreet());

                TextView telTextView = (TextView) view.findViewById(R.id.tel_text_view);
                telTextView.setText(office.getTel());

                TextView faxTextView = (TextView) view.findViewById(R.id.fax_text_view);
                if (office.getFax().length() > 0) {
                    faxTextView.setText(office.getFax());
                } else {
                    faxTextView.setVisibility(View.GONE);
                }
            }

            prepareMap(office);
        }

        return view;
    }

    private void prepareMap(final Office office) {
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentByTag(TAG_MAP);
        if (fragment == null) {
            GoogleMapOptions options = new GoogleMapOptions();
            options.liteMode(true);
            fragment = SupportMapFragment.newInstance(options);
            final SupportMapFragment finalFragment = fragment;
            getChildFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    GoogleMap map = finalFragment.getMap();
                    MarkerOptions options = new MarkerOptions();
                    LatLng position = new LatLng(office.getLat(), office.getLon());
                    LatLng cameraPosition = new LatLng(office.getLat() + 0.02, office.getLon());
                    options.position(position);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 12));
                    map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    map.addMarker(options);
                    map.getUiSettings().setAllGesturesEnabled(false);
                }
            });

            fm.beginTransaction().replace(R.id.map_container, fragment, TAG_MAP).addToBackStack(null).commit();
        }

    }
}
