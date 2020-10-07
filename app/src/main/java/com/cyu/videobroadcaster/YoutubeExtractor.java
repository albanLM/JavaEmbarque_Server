package com.cyu.videobroadcaster;

import android.app.Application;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.yausername.ffmpeg.FFmpeg;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;
import com.yausername.youtubedl_android.YoutubeDLRequest;

import java.io.File;

public class YoutubeExtractor {
    private static YoutubeExtractor singleton;
    private YoutubeDL youtubeDL;
    private FFmpeg fmpeg;

    private YoutubeExtractor(Application app) throws YoutubeDLException {
        youtubeDL = YoutubeDL.getInstance();
        fmpeg = FFmpeg.getInstance();
            youtubeDL.init(app);
            fmpeg.init(app);
    }

    public static YoutubeExtractor getInstance(Application app) throws YoutubeDLException {
        if (singleton == null) {
            singleton = new YoutubeExtractor(app);
        }
        return singleton;
    }

    public void downloadFromURL(String url) throws YoutubeDLException, InterruptedException {
        File youtubeDLDir = getDownloadLocation();
        YoutubeDLRequest request = new YoutubeDLRequest(url);
        request.addOption("-o", youtubeDLDir.getAbsolutePath() + "/%(title)s.%(ext)s");

        youtubeDL.execute(request, (progress, etaInSeconds) -> {
            System.out.println(String.valueOf(progress) + "% (ETA " + String.valueOf(etaInSeconds) + " seconds)");
        });
    }

    @NonNull
    private File getDownloadLocation() {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File youtubeDLDir = new File(downloadsDir, "video_server_dl");
        if (!youtubeDLDir.exists()) youtubeDLDir.mkdir();
        return youtubeDLDir;
    }
}
