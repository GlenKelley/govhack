package org.govhack.vespene;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.govhack.vespene.util.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;


public class ImageFetcher {

  private static final String TAG = "ImageFetcher";

  private class ImageInfo {
    public String url;
    public byte[] bytes;
    public Bitmap bitmap;
    
    ImageInfo(byte[] bytes, Bitmap bitmap, String url) {
      this.url = url;
      this.bytes = bytes;
      this.bitmap = bitmap;
    }
  }
    
  /** Saves an image to file. */
  private class SaveTask extends AsyncTask<ImageInfo, Void, Void> {
    @Override
    protected Void doInBackground(ImageInfo... imgs) {
      ImageInfo img = imgs[0];
      String filename = filenameForUrl(img.url);
      Log.i(TAG, "Saving file " + filename);
      try {
        FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        outputStream.write(imgs[0].bytes);
        outputStream.close();
      } catch (Exception e) {
        Log.w(TAG, "Failed to save file", e);
      }
      return null;
    }
  }
  
  public interface ImageCb {
    void onImage(Bitmap bitmap);
  }
  
  public static class ImageUpdater implements ImageCb {
    private final ImageView view;
    
    public ImageUpdater(ImageView view) {
      this.view = view;
    }
    @Override public void onImage(Bitmap bitmap) {
      view.setImageBitmap(bitmap);
    }
  }

  private class FetchTask extends AsyncTask<String, Void, ImageInfo> {
    private ImageCb cb;
    
    public FetchTask(ImageCb cb) {
      this.cb = cb;
    }
    
    @Override
    protected ImageInfo doInBackground(String... params) {
      String url = params[0];
      // Try to open from storage
      String filename = filenameForUrl(url);
      try {
        Log.i(TAG, "Loading file " + filename);
        FileInputStream stream = context.openFileInput(filename);
        return imageFromStream(stream, url);
      } catch (IOException e) {
        Log.i(TAG, "No file " + filename);
        // File not accessible, download instead.
      }
      
      // Try to download
      try {
        ImageInfo downloaded = downloadImage(url);
        new SaveTask().execute(downloaded);
        return downloaded;
      } catch (IOException e) {
        Log.e(TAG, "Failed to download image: " + e.getMessage(), e);
        return null;
      }
    }
    
    @Override
    protected void onPostExecute(ImageInfo img) {
      if (img != null) cb.onImage(img.bitmap);
    }
  }
  
  private final Context context;

  public ImageFetcher(Context context) {
    this.context = context;
  }
  
  public void fetchImage(String url, ImageCb callback) {
    new FetchTask(callback).execute(url);
  }

  //////

  private String filenameForUrl(String url) {
    return "img-" + Util.sha1(url) + ".jpg";
  }

  private ImageInfo downloadImage(String url) throws IOException {
	url = url.replace(" ", "%20");
    URL u = new URL(url);
    Log.i(TAG, "Downloading " + u);
    InputStream stream = u.openConnection().getInputStream();
    ImageInfo info = imageFromStream(stream, url);
    stream.close();
    return info;
  }

  
  private ImageInfo imageFromStream(InputStream stream, String sourceUrl) throws IOException {
    ByteArrayOutputStream downloaded = new ByteArrayOutputStream(100 * 1000);
    IOUtils.copy(stream, downloaded);
    byte[] imageBytes = downloaded.toByteArray();
    Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    return new ImageInfo(imageBytes, bmp, sourceUrl);
  }

}
