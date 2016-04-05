package com.stxnext.intranet2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stxnext.intranet2.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;


/**
 * Created by Bartosz Kosarzycki on 2015-08-10.
 */

public class PicturePreviewActivity extends AppCompatActivity {

    @Bind(R.id.profile_image_view)
    ImageView profileImageView;
    private String pictureUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_picture_preview);
        //todo: THIS SHOULD BE HIGH RESOLUTION PHOTO - IS it available from the backend?
        ButterKnife.bind(this);
        pictureUrl = "https://intranet.stxnext.pl" + getIntent().getExtras().getString("pictureUrl");
        //Picasso.with(this).invalidate(pictureUrl);
        loadViews();
    }

    private void loadViews() {
        Picasso.with(this)
                .load(pictureUrl)
                .placeholder(R.drawable.avatar_placeholder)
                .into(profileImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        profileImageView.animate().alpha(1).setDuration(200).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                profileImageView.animate().scaleX(2.5f).scaleY(2.5f).setDuration(400);
                            }
                        });
                    }

                    @Override
                    public void onError() {
                        profileImageView.animate().alpha(1).setDuration(200);
                    }
                });
    }


    @Override
    public void onBackPressed() {
        final int BACK_ANIM_DURATION_MILLIS = 300;
        final float BASE_SCALE = 1f;
        profileImageView.animate().scaleX(BASE_SCALE).scaleY(BASE_SCALE).setDuration(BACK_ANIM_DURATION_MILLIS).withEndAction(new Runnable() {
            @Override
            public void run() {
                PicturePreviewActivity.this.supportFinishAfterTransition();
            }
        }).start();
    }

    @OnClick(R.id.activity_picture_preview_outerLayout)
    public void onOutOfPictureClick(View v) {
        onBackPressed();
    }
}
