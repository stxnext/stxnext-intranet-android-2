package com.stxnext.intranet2.backend.api;

import android.content.Context;
import android.util.Log;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.callback.UserApiCallback;
import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.impl.UserRestWrapper;
import com.stxnext.intranet2.utils.Config;
import com.stxnext.intranet2.utils.DBManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by bkosarzycki on 06.08.15.
 */
public interface EmployeesCommonApi {

    void downloadUser(final Context context, final Optional<UserApiCallback> apiCallback, final String userId);

    void downloadUsers(final Context context, final Optional<EmployeesApiCallback> apiCallback);
}
