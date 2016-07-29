package com.github.sasd97.upitter.constants;

import android.util.Log;

import com.github.sasd97.upitter.utils.Names;

/**
 * Created by alexander on 27.07.16.
 */

public interface GalleryConstants {

    enum AlbumMode {

        FILE_MODE {
            @Override
            public String obtainPath(String prePath) {
                Log.d("PATH", prePath);
                if (prePath.contains("file://")) return prePath;
                return Names
                        .getInstance()
                        .getFilePath(prePath)
                        .toString();
            }
        },
        INTERNET_MODE {
            @Override
            public String obtainPath(String prePath) {
                return prePath;
            }
        };

        public abstract String obtainPath(String prePath);

        public static AlbumMode obtainMode(int mode) {
            if (mode == 0) return FILE_MODE;
            return INTERNET_MODE;
        }
    }
}
