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
import com.sapergis.vdiction.fragments.ImageTranslationFragment;
import com.sapergis.vdiction.fragments.TypeTranslationFragment;
import com.sapergis.vdiction.helper.GrantPermission;
import com.sapergis.vdiction.helper.VDStaticValues;
import com.sapergis.vdiction.model.VDText;
import com.sapergis.vdiction.viewmodel.LDTranslationViewModel;
import java.io.IOException;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity implements ImageSelectionFragment.ImageSelectionFragmentListener {
    private ImageView mImageView;
    private Button mTextButton;
    private Button fragmentTest;
    private FirebaseVisionImage mSelectedImage;
    private FirebaseVisionImage storedImage;
    private LDTranslationViewModel ldTranslationViewModel;
    private LiveData<VDText> liveDataVDText;

    private static final int PICK_IMAGE = 101;
    private static final int REQUEST_READ_PERMISSION = 786;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ldTranslationViewModel = ViewModelProviders.of(this).get(LDTranslationViewModel.class);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, ImageSelectionFragment.newInstance("text1","text2"))
                .commit();
    }

    private void subscribe(FirebaseVisionImage currentImage) {
        final Observer<VDText> vdTextObserver = new Observer<VDText>() {
            @Override
            public void onChanged(VDText vdText) {
                openTranslationFragment(vdText);
                removeLiveDataObserver();
            }
        };
        liveDataVDText = ldTranslationViewModel.getTranslation(currentImage);
        liveDataVDText.observe(this,vdTextObserver);
    }

    private void removeLiveDataObserver() {
        liveDataVDText.removeObservers(this);
    }

    private void openTranslationFragment(VDText vdText){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout,
                        ImageTranslationFragment.newInstance(vdText.refineText(vdText.getRawText()),
                                vdText.getTranslatedText()))
                .addToBackStack("TranslationFromImage")
                .commit();
    }

    private void openTypeTranslationFragment (){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, TypeTranslationFragment.newInstance())
                .addToBackStack("TypeTranslation")
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            processImage(VDStaticValues.CAMERA_IMAGE, data);
        }
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK ) {
            GrantPermission.check(Manifest.permission.READ_EXTERNAL_STORAGE, MainActivity.this);
            processImage(VDStaticValues.GALLERY_IMAGE, data);

        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
        }
    }

    @Override
    public void imageFromCamera() {
        //Get text with camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void imageFromGallery() {
        //select storedImage from gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE);
    }

    @Override
    public void typeWord(){
        openTypeTranslationFragment();
    }
}
