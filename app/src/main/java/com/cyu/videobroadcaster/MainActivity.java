package com.cyu.videobroadcaster;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yausername.youtubedl_android.YoutubeDLException;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddVideoDialog.NoticeDialogListener {
    private YoutubeExtractor youtubeExtractor;
    private RecyclerView videoList;
    private VideoList mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            youtubeExtractor = YoutubeExtractor.getInstance(getApplication());
        } catch (YoutubeDLException e) {
            Toast.makeText(MainActivity.this, "failed to initialize youtubedl-android", Toast.LENGTH_LONG).show();
        }

        // Asks permissions to use storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
            }
        }

        initViews();
    }

    private void initViews() {
        videoList = findViewById(R.id.recycler_view_video_list);
        videoList.setHasFixedSize(true); // to improve performance
        layoutManager = new LinearLayoutManager(this);
        videoList.setLayoutManager(layoutManager);

        File[] videoFiles = getVideoList(); // Get the file names of the videos
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<Drawable> thumbnails = new ArrayList<>();
        for (File file : videoFiles) {
            ImageView image = new ImageView(MainActivity.this);
            thumbnails.add(image.getDrawable());
            titles.add(file.getName());
        }

        mAdapter = new VideoList(titles, thumbnails);
        videoList.setAdapter(mAdapter);
    }

    public void showNoticeDialog(View view) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new AddVideoDialog();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String url) {
        // User touched the dialog's positive button

        // Verifies if the url is valid
        url = url.trim();
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(MainActivity.this, "please enter a valid url", Toast.LENGTH_LONG).show();
            return;
        }

        dialog.dismiss(); // Dismisses the dialog window

        // Tries to download the file
        try {
            youtubeExtractor.downloadFromURL(url);
        } catch (YoutubeDLException | InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "failed to download the video", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        dialog.dismiss();
    }

    public File[] getVideoList() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/video_server_dl";
        File directory = new File(path);
        File[] files = directory.listFiles();
        return files;
    }
}