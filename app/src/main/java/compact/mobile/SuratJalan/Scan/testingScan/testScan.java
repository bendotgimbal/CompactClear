package compact.mobile.SuratJalan.Scan.testingScan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.mobile.client.AWSMobileClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Random;

import compact.mobile.Base64;
import compact.mobile.R;
import compact.mobile.SuratJalan.Scan.testingScan.Imageutils;
import compact.mobile.SuratJalan.Scan.testingScan.MyFileContentProvider;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Bendot Gimbal on 12/06/2019.
 */

public class testScan extends Activity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 1888;

    public String STR_foto="";
    public String STR_OriginalReplace="";
    public String STR_ResizeReplace="";
    public String STR_replace="";

    public String STR_base64_original;
    public String STR_base64_resize;

    public String STR_UserName="yosua";
    public String STR_HostUrl="stg.visitpd.alfatrex.id";
    String STR_RespnseMessage, STR_RespnseCode, STR_IDCheckin;
    String STR_FileName;

    Uri imageUri = null;

    private ImageView actualImageView;
    private ImageView compressedImageView;
    private TextView actualSizeTextView;
    private TextView compressedSizeTextView;
    TextView _status;
    private File actualImage;
    private File compressedImage;
    Button BTN_TakePhoto, BTN_ChooseImage, BTN_Upload, BTN_UploadAWS;
    ProgressBar pb;

    byte[] imageInByte;
    static Bitmap compressedImage2;
    static Bitmap compressedImageBitmap;
    static Bitmap Photo;
    static int heightPict,widthPict,qualityPict;
    static int check_size;

    private Bitmap bitmap;
    private String file_name;
    Imageutils imageutils;
    static int height,width,quality;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String IMG_SERIAL = "serial";
    SharedPreferences sharedpreferences;

    String STR_key = "";
    String STR_secret = "";
//    String STR_key;
//    String STR_secret;
    private AmazonS3Client s3Client;
    private BasicAWSCredentials credentials;

    AmazonS3Client s3;
    TransferUtility transferUtility;
    TransferObserver observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_scan);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        AWSMobileClient.getInstance().initialize(this).execute();
        actualImageView = (ImageView) findViewById(R.id.actual_image);
        compressedImageView = (ImageView) findViewById(R.id.compressed_image);
        actualSizeTextView = (TextView) findViewById(R.id.actual_size);
        compressedSizeTextView = (TextView) findViewById(R.id.compressed_size);

        BTN_ChooseImage = (Button) findViewById(R.id.choose_image);
//        final Button photo = (Button) findViewById(R.id.photo);
        BTN_TakePhoto = (Button) findViewById(R.id.photo);
        BTN_Upload = (Button) findViewById(R.id.upload);
        BTN_UploadAWS = (Button) findViewById(R.id.uploadAWS);
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        _status = (TextView) findViewById(R.id.txt_progress);

        BTN_TakePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Debug","Button Take Photo");
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        BTN_ChooseImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Debug","Button Choose Image");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        BTN_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Compressor", "Button Upload");

                if (STR_base64_original == null ){
                    Log.d("Compressor", "Base64 Original Kosong");
                    Toast.makeText(getApplicationContext(),"Base64 Original Kosong", Toast.LENGTH_SHORT).show();
                }else if (STR_base64_resize == null ){
                    Log.d("Compressor", "Base64 Resize Kosong");
                    Toast.makeText(getApplicationContext(),"Base64 Resize Kosong", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("Compressor", "Base64 Original / Resize Isi");
                    new JSONParseUploadImage().execute();
                    clearImage();
                    clearImage2();
                }
            }
        });

        BTN_UploadAWS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                STR_key = getString(R.string.aws_s3_key);
//                Log.d("Debug", "AWS S3 Key" + STR_key);
//                STR_secret = getString(R.string.aws_s3_secret);
//                Log.d("Debug", "AWS S3 Secret Key" + STR_secret);
                credentials = new BasicAWSCredentials(STR_key,STR_secret);
                s3 = new AmazonS3Client(credentials);
                transferUtility = new TransferUtility(s3, testScan.this);


//                File file = new File(path);
//                File file = new File(new File("/sdcard/Image Resize/"),"Comp_img_"+ sharedpreferences.getString(IMG_SERIAL,"1")+".jpg");
                File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Image Compact");
//                File file = new File(mediaStorageDir, "Comp_img_" + sharedpreferences.getString(IMG_SERIAL, "1") + ".jpg");
                File file = new File(mediaStorageDir, STR_FileName);
                Log.d("Debug", "Path Upload AWS From = " + file);
                if(!file.exists()) {
                    Toast.makeText(testScan.this, "File Not Found!", Toast.LENGTH_SHORT).show();
                    return;
                }
//                observer = transferUtility.upload(
//                        "Compact",
//                        "Test_Video",
//                        file
//                );
                observer = transferUtility.upload(
                        "compact-clear",
                        STR_FileName,
                        file
                );

                observer.setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {

                        if (state.COMPLETED.equals(observer.getState())) {

                            Toast.makeText(testScan.this, "File Upload Complete", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        long _bytesCurrent = bytesCurrent;
                        long _bytesTotal = bytesTotal;

                        float percentage =  ((float)_bytesCurrent /(float)_bytesTotal * 100);
                        Log.d("percentage","" +percentage);
                        pb.setProgress((int) percentage);
                        String strPercentage = String.format("%.02f", percentage);
                        _status.setText(strPercentage + "%");
//                        _status.setText(percentage + "%");
                    }

                    @Override
                    public void onError(int id, Exception ex) {

                        Toast.makeText(testScan.this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        actualImageView.setBackgroundColor(getRandomColor());
        clearImage();
    }

    public void chooseImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

//    public void takePicture(View view) {
//        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
////        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);
//        startActivityForResult(cameraIntent, CAMERA_REQUEST);
//    }

    private void setCompressedImage() {
//        compressedImageView.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getAbsolutePath()));
//        compressedSizeTextView.setText(String.format("Size : %s", getReadableFileSize(compressedImage.length())));

        Bitmap mBitmap2 = BitmapFactory.decodeFile(compressedImage.getAbsolutePath());
        compressedImageView.setImageBitmap(mBitmap2);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        mBitmap2.compress(Bitmap.CompressFormat.JPEG, 50, bao);
        byte [] byte_arr = bao.toByteArray();
        String image_encode2 = "data:image/png;base64,"+ Base64.encodeBytes(byte_arr);
        String base64Image2 = image_encode2.split(",")[1];
        String STR_foto2 = base64Image2;
        Log.d("Debug","Hasil Encode Photo 2 = "+STR_foto2);
        Log.d("Debug",String.format("Size Resize : %s", getReadableFileSize(image_encode2.length())));
        compressedSizeTextView.setText(String.format("Size : %s", getReadableFileSize(image_encode2.length())));

        int width = mBitmap2.getWidth();
        int height = mBitmap2.getHeight();
        Log.d("Debug", "Ukuran Compress = " + String.valueOf(width)+ " px : " + String.valueOf(height) + " px");

//        STR_base64_resize = STR_foto2;
        Log.d("Debug", String.format("Base64 Resize: %s", STR_foto2));
        STR_base64_resize= STR_foto2.replaceAll("[+]", ".");

        Toast.makeText(this, "Compressed image save in " + compressedImage.getPath(), Toast.LENGTH_LONG).show();
        Log.d("Compressor", "Compressed image save in " + compressedImage.getPath());

//        compressedImageView.setImageBitmap(compressedImage2);
//        Log.d("Debug",String.format("Size Resize : %s", getReadableFileSize(imageInByte.length)));
//        compressedSizeTextView.setText(String.format("Size : %s", getReadableFileSize(imageInByte.length)));
//
//        final Bitmap myImagecompress = compressedImage2;
//        int width = myImagecompress.getWidth();
//        int height = myImagecompress.getHeight();
//        Log.d("Debug", "Ukuran Compress = " + String.valueOf(width)+ " px : " + String.valueOf(height) + " px");
    }

    private void clearImage() {
        actualImageView.setBackgroundColor(getRandomColor());
        compressedImageView.setImageDrawable(null);
        compressedImageView.setBackgroundColor(getRandomColor());
        compressedSizeTextView.setText("Size : -");
    }

    private void clearImage2() {
        actualImageView.setImageDrawable(null);
        actualSizeTextView.setText("Size : -");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Log.i("Debug", "Receive the camera result");

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    File out = new File(getFilesDir(), "newImage.jpg");
                    if (!out.exists()) {
                        Toast.makeText(getBaseContext(),
                                "Error while capturing image", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    Bitmap mBitmap = BitmapFactory.decodeFile(out.getAbsolutePath());
//                    Bitmap mBitmap = (Bitmap) data.getExtras().get("data");
                    actualImageView.setImageBitmap(mBitmap);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
//                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bao);
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                    byte[] byte_arr = bao.toByteArray();
                    String image_encode = "data:image/png;base64," + Base64.encodeBytes(byte_arr);
                    String base64Image = image_encode.split(",")[1];
                    STR_foto = base64Image;
                    STR_base64_original= STR_foto.replaceAll("[+]", ".");
                    Log.d("Debug", "Hasil Encode Photo = " + STR_foto);
                    actualSizeTextView.setText(String.format("Size : %s", getReadableFileSize(image_encode.length())));
                    Log.d("Debug", String.format("Size image from camera: %s", getReadableFileSize(image_encode.length())));
                    int width = mBitmap.getWidth();
                    int height = mBitmap.getHeight();
                    Log.d("Debug", "Scale Foto = " + String.valueOf(width)+ " px : " + String.valueOf(height) + " px");

//                    STR_base64_original = STR_foto;
                    Log.d("Debug", String.format("Base64 Original From Camera: %s", STR_foto));

//                    String height_data="760";
//                    String width_data="1080";
//                    String quality_data="100";
//                    Photo = mBitmap;
//
//                    if(height_data.trim().length() <= 0 && width_data.trim().length() > 0){
//                        if(quality_data.trim().length() <= 0){
//                            Toast.makeText(getApplicationContext(), "Please insert quality", Toast.LENGTH_SHORT).show();
//                        }else {
//                            check_size = 1;
//                            widthPict = Integer.parseInt(width_data);
//                            qualityPict = Integer.parseInt(quality_data);
//                            if(qualityPict > 0 && qualityPict <= 100){
//                                ResizeImage();
//                            }else{
//                                Toast.makeText(getApplicationContext(), "Wrong quality input.try again !", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                    else if(height_data.trim().length() > 0 && width_data.trim().length() <= 0){
//                        Toast.makeText(getApplicationContext(), "Please insert width", Toast.LENGTH_SHORT).show();
//                    }else{
//                        if(quality_data.trim().length() <= 0){
//                            Toast.makeText(getApplicationContext(), "Please insert quality", Toast.LENGTH_SHORT).show();
//                        }else {
//                            check_size = 0;
//                            heightPict = Integer.parseInt(height_data);
//                            widthPict = Integer.parseInt(width_data);
//                            qualityPict = Integer.parseInt(quality_data);
//                            if(qualityPict > 0 && qualityPict <= 100){
//                                ResizeImage();
//                            }else{
//                                Toast.makeText(getApplicationContext(), "Wrong quality input.try again !", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }

                    new Compressor(this)
                            .compressToFileAsFlowable(out)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<File>() {
                                @Override
                                public void accept(File file) {
                                    compressedImage = file;
                                    setCompressedImage();
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) {
                                    throwable.printStackTrace();
                                    showError(throwable.getMessage());
                                }
                            });

                    //File file = new File(new File("/sdcard/Image Resize/"),"Comp_img_"+ sharedpreferences.getString(IMG_SERIAL,"1")+".jpg");
//                    File file = new File(new File(Environment.getExternalStorageDirectory() + File.separator + "Image Resize" + File.separator),"Comp_img_"+ sharedpreferences.getString(IMG_SERIAL,"1")+".jpg");
//                    File file = new File(new File("/sdcard/Image Resize/"),"Comp_img_"+ sharedpreferences.getString(IMG_SERIAL,"1")+".jpg");
                    File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Image Compact");
                    File file = new File(mediaStorageDir, "Comp_img_" + sharedpreferences.getString(IMG_SERIAL, "1") + ".jpg");
                    Log.d("Debug", "Path Save = " + file);
                    STR_FileName = "Comp_img_" + sharedpreferences.getString(IMG_SERIAL, "1") + ".jpg";
                    Log.d("Debug", "Name File = " + STR_FileName);
                    try {
                        Log.i("Debug", "Save Photo");
                        FileOutputStream out2 = new FileOutputStream(file);
                        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                        out2.flush();
                        out2.close();
                        Toast.makeText(getApplicationContext(),"Saved", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        int abc = Integer.parseInt(sharedpreferences.getString(IMG_SERIAL,"1"));
                        abc++;
                        editor.putString(IMG_SERIAL,String.valueOf(abc));
                        editor.commit();
//                        String path =  Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
                        String path =  Environment.getExternalStorageDirectory() + File.separator + "Image Compact" + File.separator;
                        Log.d("Debug", "Save - Path = " + path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case PICK_IMAGE_REQUEST:
                    if (data == null) {
                        showError("Failed to open picture!");
                        Log.d("Debug", "Failed to open picture!");
                        return;
                    }
                    try {
                        actualImage = FileUtil.from(this, data.getData());
                        actualImageView.setImageBitmap(BitmapFactory.decodeFile(actualImage.getAbsolutePath()));
                        actualSizeTextView.setText(String.format("Size : %s", getReadableFileSize(actualImage.length())));
                        Log.d("Debug", String.format("Size image from folder: %s", getReadableFileSize(actualImage.length())));
                        clearImage();

                        Bitmap mBitmap2 = BitmapFactory.decodeFile(actualImage.getAbsolutePath());
                        actualImageView.setImageBitmap(mBitmap2);
                        ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
                        mBitmap2.compress(Bitmap.CompressFormat.JPEG, 20, bao2);
                        byte[] byte_arr2 = bao2.toByteArray();
                        String image_encode2 = "data:image/png;base64," + Base64.encodeBytes(byte_arr2);
                        String base64Image2 = image_encode2.split(",")[1];
                        STR_foto = base64Image2;
                        STR_base64_original= STR_foto.replaceAll("[+]", ".");
                        Log.d("Debug", "Hasil Encode Photo From SDCard = " + STR_foto);

//                        STR_base64_original = STR_foto;
                        Log.d("Debug", String.format("Base64 Original From SDCard: %s", STR_foto));

                        new Compressor(this)
                                .compressToFileAsFlowable(actualImage)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<File>() {
                                    @Override
                                    public void accept(File file) {
                                        compressedImage = file;
                                        setCompressedImage();
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) {
                                        throwable.printStackTrace();
                                        showError(throwable.getMessage());
                                    }
                                });

//                        compressedImage2 = BitmapFactory.decodeFile(actualImage.getAbsolutePath());
//                        int width2 = compressedImage2.getWidth();
//                        int height2 = compressedImage2.getHeight();
//                        Log.d("Debug", "Ukuran From SDCard = " + String.valueOf(width2)+ " px : " + String.valueOf(height2) + " px");
//
//                        float aspectRatio2 = mBitmap2.getWidth() / (float) mBitmap2.getHeight();
//                        if(check_size == 0){
//                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                            compressedImage2 = Bitmap.createScaledBitmap(compressedImage2, 240, 240, false);
//                            compressedImage2.compress(Bitmap.CompressFormat.JPEG, 10, stream);
//                            imageInByte = stream.toByteArray();
//                            setCompressedImage();
//                        }else{
//                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                            int heightless = Math.round(240 / aspectRatio2);
//                            compressedImage2 = Bitmap.createScaledBitmap(compressedImage2, 240, heightless, false);
//                            compressedImage2.compress(Bitmap.CompressFormat.JPEG, 10, stream);
//                            imageInByte = stream.toByteArray();
//                            setCompressedImage();
//                        }
                    } catch (IOException e) {
                        showError("Failed to read picture data!");
                        Log.d("Debug", "Failed to read picture data!");
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
//            // uncomment if you want to get the thumbnail
//            Bitmap mBitmap = (Bitmap) data.getExtras().get("data");
//            actualImageView.setImageBitmap(mBitmap);
//            ByteArrayOutputStream bao = new ByteArrayOutputStream();
////                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bao);
//            mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, bao);
//            byte[] byte_arr = bao.toByteArray();
//            String image_encode = "data:image/png;base64," + Base64.encodeBytes(byte_arr);
//            String base64Image = image_encode.split(",")[1];
//            STR_foto = base64Image;
//            STR_base64_original= STR_foto.replaceAll("[+]", ".");
//            Log.d("Debug", "Hasil Encode Photo = " + STR_foto);
//            actualSizeTextView.setText(String.format("Size : %s", getReadableFileSize(image_encode.length())));
//            Log.d("Debug", String.format("Size image from camera: %s", getReadableFileSize(image_encode.length())));
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    private class JSONParseUploadImage extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        String Str_UserName = STR_UserName;
        String Str_Foto_Original = STR_base64_original;
        String Str_Foto_Replace = STR_base64_resize;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(testScan.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("Debug", "Lewat PreExecute");

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            HttpURLConnection connection;
            OutputStreamWriter request = null;
            URL url = null;
            String URI = null;
            String response = null;
            String parameters = "username="+Str_UserName+"&foto_original="+Str_Foto_Original+"&foto_resize="+Str_Foto_Replace;
//            Log.d("Debug", "Parameter Upload Image -> " + parameters);
            Log.d("Debug", "Parameter Username -> " + Str_UserName);
            Log.d("Debug", "Parameter Image Original -> " + Str_Foto_Original);
            Log.d("Debug", "Parameter Image Replace -> " + Str_Foto_Replace);

            try
            {
                String Str_urlphp;
                Str_urlphp = "";
                Log.d("debug", "Host Server -> " + STR_HostUrl);
                url = new URL("http://"+STR_HostUrl+"/alfatrexpd/uploadimage");
                Log.d("Debug","Test URL Search Store " + url);

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestMethod("POST");

                request = new OutputStreamWriter(connection.getOutputStream());
                //Log.d("Debug","request = " + request);
                request.write(parameters);
                request.flush();
                request.close();
                String line = "";

                //Get data from server
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                    sb.append("response_code");
                    sb.append("response_message");
                    Log.d("Debug","TraceLine = " + line);
                }

                JSONObject jsonObjectCheckIN = new JSONObject(sb.toString());
                STR_RespnseMessage = jsonObjectCheckIN.getString("response_message");
                STR_RespnseCode = jsonObjectCheckIN.getString("response_code");
//                JSONArray ListObjData = jsonObjectLogin.getJSONArray("data");
                JSONObject jsonIDCheckin = new JSONObject(sb.toString()).getJSONObject("data");
                STR_IDCheckin = jsonIDCheckin.getString("id_checkin");
                Log.d("Debug", "ID CheckIn -> " + STR_IDCheckin);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(JSONObject json) {
            Log.d("Debug", "1.Test Sampai Sini ");
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Berhasil Upload Image", Toast.LENGTH_SHORT).show();
        }
    }

    public void ResizeImage() {
        if (Photo == null) {
            showError("Please choose an image!");
        } else {
            // Compress image in main thread using custom Compressor
            try {

                compressedImageBitmap = Photo;
                float aspectRatio = compressedImageBitmap.getWidth() /
                        (float) compressedImageBitmap.getHeight();
                if(check_size == 0){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    compressedImageBitmap = Bitmap.createScaledBitmap(
                            compressedImageBitmap, widthPict, heightPict, false);
                    compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, qualityPict, stream);
                    imageInByte = stream.toByteArray();
                    setCompressedImage();

                    Log.d("Debug", String.format("Size Image Compress If : %s", getReadableFileSize(imageInByte.length)));
                    String image_encode = "data:image/png;base64," + Base64.encodeBytes(imageInByte);
                    String base64Image = image_encode.split(",")[1];
//					String STR_foto = base64Image;
//					Log.d("Debug", "Hasil Encode Re-Image = " + STR_foto);

                    int widthcompressedImageBitmap = compressedImageBitmap.getWidth();
                    int heightcompressedImageBitmap = compressedImageBitmap.getHeight();
                    Log.d("Debug", "Ukuran Image Compress If = " + String.valueOf(widthcompressedImageBitmap)+ " px : " + String.valueOf(heightcompressedImageBitmap) + " px");

//					STR_replace= STR_foto.replaceAll("[+]", ".");
//					Log.d("Debug","Replace Encode Photo = "+STR_replace);

                    STR_foto = base64Image;
                    Log.d("Debug","Hasil Encode Photo If = "+STR_foto);
                    STR_replace= STR_foto.replaceAll("[+]", ".");
                    Log.d("Debug","Replace Encode Re-Image Photo = "+STR_replace);
                }else{
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    int heightless = Math.round(widthPict / aspectRatio);
                    compressedImageBitmap = Bitmap.createScaledBitmap(
                            compressedImageBitmap, widthPict, heightless, false);
                    compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, qualityPict, stream);
                    imageInByte = stream.toByteArray();
                    setCompressedImage();

                    Log.d("Debug", String.format("Size Image Compress Else : %s", getReadableFileSize(imageInByte.length)));
                    String image_encode = "data:image/png;base64," + Base64.encodeBytes(imageInByte);
                    String base64Image = image_encode.split(",")[1];
//					String STR_foto = base64Image;
//					Log.d("Debug", "Hasil Encode Re-Image = " + STR_foto);

                    int widthcompressedImageBitmap = compressedImageBitmap.getWidth();
                    int heightcompressedImageBitmap = compressedImageBitmap.getHeight();
                    Log.d("Debug", "Ukuran Image Compress Else = " + String.valueOf(widthcompressedImageBitmap)+ " px : " + String.valueOf(heightcompressedImageBitmap) + " px");

//					STR_replace= STR_foto.replaceAll("[+]", ".");
//					Log.d("Debug","Replace Encode Photo = "+STR_replace);

                    STR_foto = base64Image;
                    Log.d("Debug","Hasil Encode Photo Else = "+STR_foto);
                    STR_replace= STR_foto.replaceAll("[+]", ".");
                    Log.d("Debug","Replace Encode Re-Image Photo = "+STR_replace);
                }

            } catch (Exception e) {
                e.printStackTrace();
                showError(e.getMessage());
            }
        }
    }

    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private int getRandomColor() {
        Random rand = new Random();
        return Color.argb(100, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.0#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}