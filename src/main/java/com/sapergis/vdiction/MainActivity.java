package com.sapergis.vdiction;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.sapergis.vdiction.implementation.VDTextRecognizer;
import com.sapergis.vdiction.model.VDText;
import java.io.File;
import java.io.IOException;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    private Button mTextButton;
    private Button mTextRecognition;
    private Button mTranslateFromGallery;
    private Bitmap mSelectedImage;
    private FirebaseVisionImage storedImage;

    private static final int PICK_IMAGE = 101;
    private static final int REQUEST_READ_PERMISSION = 786;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextButton = findViewById(R.id.findTextButton);
        mTextRecognition = findViewById(R.id.recognizeButton);
        mTranslateFromGallery = findViewById(R.id.translateFromGallery);

        mTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findText();
            }
        });

        mTranslateFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGalleryPhoto();
            }
        });

        mTextRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //runTextRecognition();
                //VDImage mg = new VDImage(mSelectedImage).runTextRecognition()
                try {

                    if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ){
                        storedImage = FirebaseVisionImage.fromFilePath(MainActivity.this,
                                Uri.fromFile(new File("/storage/emulated/0/DCIM/Camera/Sap.jpg")));
                    }else{
                        String [] perm = new String[2];
                        perm[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
                        requestPermissions(perm, REQUEST_READ_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
//                new  VDImage(mSelectedImage).
//                        runTextRecognition();
                //FirebaseVisionImage fbVisionImage = new  VDImage(storedImage).getFbVisionImage();
                VDTextRecognizer vdTextRecognizer = new VDTextRecognizer(storedImage);
                vdTextRecognizer.runTextRecognition();
//                String text = vdTextRecognizer.getText();
//                System.out.println("SAP TEXT "+text);
            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    public void selectGalleryPhoto(){
        //select storedImage from gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE);




    }

    public void findText(){
        //Get text with camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if(imageBitmap!=null){
                mImageView = new ImageView(this.getBaseContext());
                mImageView.setImageBitmap(imageBitmap);
                mSelectedImage = imageBitmap;
            }

        }
        if (requestCode == PICK_IMAGE){
            String test;
            Uri uri = data.getData();
            if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ){
                try {
                    storedImage = FirebaseVisionImage.fromFilePath(MainActivity.this, uri  );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                String [] perm = new String[2];
                perm[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
                requestPermissions(perm, REQUEST_READ_PERMISSION);
            }
//            String [] proj = {MediaStore.Images.Media.DATA};
//            Cursor cursor = managedQuery(uri,proj,null,null,null);
//            int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            String path = cursor.getString(colIndex);
//            test = path;
            //            mSelectedImage = getBitmapFromAsset(this, path);
            ;
//            setSelectedImage();
        }
    }

    public static void updateUI(VDText vdText){
        System.out.println("VDiction  - MainActivity: RawText is"+ vdText.getRawText());
        System.out.println("VDiction  - MainActivity: Translated text is"+vdText.getTranslatedText());
    }

}
