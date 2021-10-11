package compact.mobile.service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import java.io.File;
import java.net.URI;

/**
 * Project mobile_android.
 *
 * Created by wellsen on 2019-07-29.
 * for PT. Sumber Trijaya Lestari.
 */
public class AWSUploadService extends JobIntentService {

  private static final int UPLOAD_JOB_ID = 52535;

  public static final String EXTRA_PHOTO_FILE = "extra_uri";
  public static final String EXTRA_AWS_KEY_CODE = "extra_aws_key_code";
  public static final String EXTRA_AWS_SECRET_CODE = "extra_aws_secret_code";
  public static final String EXTRA_AWS_PATH_FOLDER = "extra_aws_path_folder";

  final Handler handler = new Handler();

  AmazonS3Client s3;
  TransferUtility transferUtility;
  TransferObserver observer;
  String STR_AWS_Key_Code, STR_AWS_Secret_Code, STR_AWS_Path_Folder;
  File photoFile;

  @Override
  protected void onHandleWork(@NonNull Intent intent) {
    STR_AWS_Key_Code = intent.getStringExtra(EXTRA_AWS_KEY_CODE);
    STR_AWS_Secret_Code = intent.getStringExtra(EXTRA_AWS_SECRET_CODE);
    STR_AWS_Path_Folder = intent.getStringExtra(EXTRA_AWS_PATH_FOLDER);

    if (intent.getExtras() == null) {
      return;
    }
//    photoFile = (File) intent.getExtras().get(EXTRA_PHOTO_FILE);
    URI uri = (URI) intent.getExtras().get(EXTRA_PHOTO_FILE);
    photoFile = new File (uri);

    BasicAWSCredentials credentials = new BasicAWSCredentials(STR_AWS_Key_Code,STR_AWS_Secret_Code);
    s3 = new AmazonS3Client(credentials);
    s3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));

    java.security.Security.setProperty("networkaddress.cache.ttl" , "60");

    transferUtility = new TransferUtility(s3, AWSUploadService.this);

    String fileName = photoFile.getName();
    if(fileName.contains("-")){
      fileName = fileName.substring(0, fileName.indexOf("-"));
    }
    if(!fileName.endsWith(".jpg")){
      fileName = fileName + ".jpg";
    }
    observer = transferUtility.upload(
        STR_AWS_Path_Folder,
            fileName,
        photoFile,
        CannedAccessControlList.PublicRead
    );

    observer.setTransferListener(new TransferListener() {
      @Override public void onStateChanged(int id, TransferState state) {
        if (TransferState.COMPLETED.equals(observer.getState())) {
          showToast("File Upload Complete");
          photoFile.delete();
        }
      }

      @Override public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

      }

      @Override public void onError(int id, Exception ex) {
        showToast(ex.getLocalizedMessage());
      }
    });
  }

  // Helper for showing tests
  void showToast(final CharSequence text) {
    handler.post(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(AWSUploadService.this, text, Toast.LENGTH_SHORT).show();
      }
    });
  }

  public static void enqueueWork(Context context, Intent intent) {
    enqueueWork(context, AWSUploadService.class, UPLOAD_JOB_ID, intent);
  }

}
