package com.sun.bingo.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.sun.bingo.model.BingoEntity;
import com.sun.bingo.model.UserEntity;
import com.sun.bingo.ui.activity.BingoDetailActivity;
import com.sun.bingo.ui.activity.EditNewBingoActivity;
import com.sun.bingo.ui.activity.LoginActivity;
import com.sun.bingo.ui.activity.MainActivity;
import com.sun.bingo.ui.activity.MineBingoActivity;
import com.sun.bingo.ui.activity.ProfileActivity;
import com.sun.bingo.ui.activity.UserInfoActivity;
import com.sun.bingo.ui.activity.WebPageActivity;

import java.io.File;

public class NavigateManager {

    public static final int TAKE_PICTURE_REQUEST_CODE = 7;
    public static final int CHOOSE_PICTURE_REQUEST_CODE = 23;
    public static final int PROFILE_REQUEST_CODE = 29;

    //拍照
    public static void gotoTakePicture(Activity activity, String takePicturePath) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), takePicturePath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, TAKE_PICTURE_REQUEST_CODE);
    }

    //从相册选择
    public static void gotoChoosePicture(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, CHOOSE_PICTURE_REQUEST_CODE);
    }

    //使用系统浏览器打开
    public static void gotoSystemExplore(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static void gotoSpecifiedActivity(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public static void gotoEditNewBingoActivity(Activity activity, String url) {
        Intent intent = new Intent(activity, EditNewBingoActivity.class);
        if (!TextUtils.isEmpty(url)) {
            intent.putExtra("url", url);
        }
        activity.startActivity(intent);
    }

    public static void gotoProfileActivity(Activity activity, boolean isGotoMain) {
        Intent intent = new Intent(activity, ProfileActivity.class);
        intent.putExtra("isGotoMain", isGotoMain);
        activity.startActivityForResult(intent, PROFILE_REQUEST_CODE);
    }

    public static void gotoLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    public static void gotoMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    public static void gotoBingoDetailActivity(Context context, BingoEntity entity) {
        Intent intent = new Intent(context, BingoDetailActivity.class);
        intent.putExtra("entity", entity);
        context.startActivity(intent);
    }

    public static void gotoUserInfoActivity(Context context, UserEntity userEntity) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra("userEntity", userEntity);
        context.startActivity(intent);
    }

    public static void gotoMineBingoActivity(Context context, int index) {
        Intent intent = new Intent(context, MineBingoActivity.class);
        intent.putExtra("index", index);
        context.startActivity(intent);
    }

    public static void gotoWebPageActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, WebPageActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
}
