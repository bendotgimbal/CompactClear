package compact.mobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import compact.mobile.service.AWSUploadService;

/**
 * Project mobile_android.
 *
 * Created by wellsen on 2019-07-29.
 * for PT. Sumber Trijaya Lestari.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
      Intent mIntent = new Intent(context, AWSUploadService.class);
      mIntent.putExtra("maxCountValue", 1000);
      AWSUploadService.enqueueWork(context, mIntent);
    }
  }

}
