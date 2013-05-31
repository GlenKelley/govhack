package org.govhack.vespene;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;


public class ImageFetcher {

  private static final String TAG = "ImageFetcher";
  private static final String DOMAIN = "d1vz4okl100cxy.cloudfront.net"; //"li.cutie.s3.amazonaws.com";

  private class ImageInfo {
    public String token;
    public byte[] bytes;
    public Bitmap bitmap;
    
    ImageInfo(String tag, byte[] bytes, Bitmap bitmap) {
      this.token = tag;
      this.bytes = bytes;
      this.bitmap = bitmap;
    }
  }
    
  /** Saves an image to file. */
  private class SaveTask extends AsyncTask<ImageInfo, Void, Void> {
    @Override
    protected Void doInBackground(ImageInfo... imgs) {
      ImageInfo img = imgs[0];
      String filename = filenameForToken(img.token);
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

  private class FetchTask extends AsyncTask<String, Void, ImageInfo> {
    private ImageView targetView;
    
    public FetchTask(ImageView targetView) {
      this.targetView = targetView;
    }
    
    @Override
    protected ImageInfo doInBackground(String... params) {
      String token = params[0];
      // Try to open from storage
      String filename = filenameForToken(token);
      try {
        Log.i(TAG, "Loading file " + filename);
        FileInputStream stream = context.openFileInput(filename);
        return imageFromStream(stream, token);
      } catch (IOException e) {
        Log.i(TAG, "No file " + filename);
        // File not accessible, download instead.
      }
      
      // Try to download
      try {
        ImageInfo downloaded = downloadImage(token);
        new SaveTask().execute(downloaded);
        return downloaded;
      } catch (IOException e) {
        Log.e(TAG, "Failed to download image: " + e.getMessage(), e);
        return null;
      }
    }
    
    @Override
    protected void onPostExecute(ImageInfo img) {
      if (img != null) {
        targetView.setImageBitmap(img.bitmap);        
      }
    }
  }
  
  private final Context context;

  public ImageFetcher(Context context) {
    this.context = context;
  }
  
  public void fetchImage(ImageView view, String token) {
    new FetchTask(view).execute(token);
  }

  //////
  
  private URL url(String token) throws IOException {
    try {
      URL url = new URL("http://" + DOMAIN + "/" + filenameForToken(token));
      return url;
    } catch (MalformedURLException e) {
      Log.e(TAG, "Bad URL", e);
      throw new IOException("Bad URL", e);
    }
  }

  private String filenameForToken(String token) {
    return "img-" + token + ".jpg";
  }

  private ImageInfo downloadImage(String token) throws IOException {
    URL u = url(token);
    Log.i(TAG, "Downloading " + u);
    InputStream stream = u.openConnection().getInputStream();
    ImageInfo info = imageFromStream(stream, token);
    stream.close();
    return info;
  }

  
  private ImageInfo imageFromStream(InputStream stream, String token) throws IOException {
    ByteArrayOutputStream downloaded = new ByteArrayOutputStream(100 * 1000);
    IOUtils.copy(stream, downloaded);
    byte[] imageBytes = downloaded.toByteArray();
    Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    return new ImageInfo(token, imageBytes, bmp);
  }

}
