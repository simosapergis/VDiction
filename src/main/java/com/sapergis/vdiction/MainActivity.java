package com.sapergis.vdiction;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.sapergis.vdiction.fragments.ImageSelectionFragment;
import com.sapergis.vdiction.fragments.TranslationFragment;
import com.sapergis.vdiction.helper.GrantPermission;
import com.sapergis.vdiction.helper.VDStaticValues;
import com.sapergis.vdiction.implementation.VDTextRecognizer;
import com.sapergis.vdiction.model.VDText;
import com.sapergis.vdiction.viewmodel.LDTranslationViewModel;

import java.io.IOException;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    private Button mTextButton;
    private Button mPickFromCamera;
    private Button mPickFromGallery;
    private Button fragmentTest;
    private FirebaseVisionImage mSelectedImage;
    private FirebaseVisionImage storedImage;
    private LDTranslationViewModel ldTranslationViewModel;
    private LiveData<VDText> livaDataVDText;

    private static final int PICK_IMAGE = 101;
    private static final int REQUEST_READ_PERMISSION = 786;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "MainActivity";

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String x = "str";

        mPickFromCamera = findViewById(R.id.fromCameraButton);
        mPickFromGallery = findViewById(R.id.fromGalleryButton);
        ldTranslationViewModel = ViewModelProviders.of(this).get(LDTranslationViewModel.class);
//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment, ImageSelectionFragment.newInstance("text1","text2"));
//        fragmentTransaction.addToBackStack("Selection");
////                fragmentTransaction.add(TranslationFragment.newInstance("text1","text2"),"Fragment_FOR_Translation");
//        fragmentTransaction.commit();
//
//        fragmentTest = findViewById(R.id.fragmentTest);
//
//        fragmentTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment, TranslationFragment.newInstance("text1","text2"));
//                fragmentTransaction.addToBackStack("Translation");
////                fragmentTransaction.add(TranslationFragment.newInstance("text1","text2"),"Fragment_FOR_Translation");
//                fragmentTransaction.commit();
//
//            }
//        });

        mPickFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFromCamera();
            }
        });

        mPickFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFromGallery();
            }
        });

    }

    private void subscribe(FirebaseVisionImage currentImage) {
        final Observer<VDText> vdTextObserver = new Observer<VDText>() {
            @Override
            public void onChanged(VDText vdText) {
                System.out.println("VDction~~Observer~~ getrawtext:"+vdText.getRawText());
                System.out.println("VDction~~Observer~~ getTranslatedText :"+vdText.getTranslatedText());
            }
        };
        ldTranslationViewModel.getTranslation(currentImage).observeForever(vdTextObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            processImage(VDStaticValues.CAMERA_IMAGE, data);
        }
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK ) {
            if (GrantPermission.check(Manifest.permission.READ_EXTERNAL_STORAGE,
                    MainActivity.this)) {
                processImage(VDStaticValues.GALLERY_IMAGE, data);
            }
        }
    }

    public static void updateUI(VDText vdText, String tranlationStatus){
        System.out.println("VDiction  - MainActivity: "+tranlationStatus
                +" RawText is"+ vdText.getRawText());
        System.out.println("VDiction  - MainActivity: "+tranlationStatus
                +" Translated text is"+vdText.getTranslatedText());

    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void imageFromGallery(){
        //select storedImage from gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE);
    }

    private void imageFromCamera(){
        //Get text with camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void processImage(String imageCAtegory, @Nullable Intent data) {
        FirebaseVisionImage currentImage = null;
        switch (imageCAtegory) {
            case VDStaticValues.CAMERA_IMAGE:
                Bundle extras = data.getExtras();
                Bitmap bitmapImage = (Bitmap) extras.get("data");
                if (bitmapImage != null) {
                    currentImage = FirebaseVisionImage.fromBitmap(bitmapImage);
                }
                break;
            case VDStaticValues.GALLERY_IMAGE:
                Uri uri = data.getData();
                try {
                    currentImage = FirebaseVisionImage.fromFilePath(MainActivity.this, uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        if (currentImage != null) {
             subscribe(currentImage);
//            VDTextRecognizer vdTextRecognizer = new VDTextRecognizer(currentImage);
//            vdTextRecognizer.runTextRecognition();
        }
    }

}
