package com.stxnext.intranet2.rest;

import retrofit.RestAdapter;

/**
 * Created by bkosarzycki on 02.10.15.
 */
public class IntranetRestAdapter {

    public static RestAdapter build() {
        return new RestAdapter.Builder()
                .setEndpoint("https://intranet-staging.stxnext.pl")
                .build();
    }
}
