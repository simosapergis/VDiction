// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.sapergis.vdiction;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView mImageView;
    private Button mTextButton;
    private Button mTextRecognition;
    private Bitmap mSelectedImage;
    // Max width (portrait mode)
    private Integer mImageMaxWidth;
    // Max height (portrait mode)
    private Integer mImageMaxHeight;
    /**
     * Name of the model file hosted with Firebase.
     */
    private static final String HOSTED_MODEL_NAME = "cloud_model_1";
    private static final String LOCAL_MODEL_ASSET = "mobilenet_v1_1.0_224_quant.tflite";
    /**
     * Name of the label file stored in Assets.
     */
    private static final String LABEL_PATH = "labels.txt";
    /**
     * Number of results to show in the UI.
     */
    private static final int RESULTS_TO_SHOW = 3;
    /**
     * Dimensions of inputs.
     */
    private static final int DIM_BATCH_SIZE = 1;
    private static final int DIM_PIXEL_SIZE = 3;
    private static final int DIM_IMG_SIZE_X = 224;
    private static final int DIM_IMG_SIZE_Y = 224;

    private static final int REQUEST_READ_PERMISSION = 786;

    /**
     * Labels corresponding to the output of the vision model.
     */
    private List<String> mLabelList;

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int  PICK_IMAGE = 2;

    FirebaseVisionImage storedImage;

    private final PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float>
                                o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });
    /* Preallocated buffers for storing storedImage data. */
    private final int[] intValues = new int[DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextButton = findViewById(R.id.findTextButton);
        mTextRecognition = findViewById(R.id.recognizeButton);
        mTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findText();
            }
        });
        mTextRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //runTextRecognition();
                //VDImage mg = new VDImage(mSelectedImage).runTextRecognition()

                File[] internalPath = getExternalFilesDirs(Environment.DIRECTORY_DCIM);
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

//        LanguageIdentifier languageIdentifier = new LanguageIdentifier("Hello");
//        Log.d(TAG,"lang is "+languageIdentifier.getTextResult());

//        mFaceButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                runFaceContourDetection();
//            }
//        });
//        mCloudButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                runCloudTextRecognition();
//            }
//        });
//        mRunCustomModelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                runModelInference();
//            }
//        });
//        Spinner dropdown = findViewById(R.id.spinner);
//        String[] items = new String[]{"Test Image 1 (Text)", "Test Image 2 (Text)", "Test Image 3" +
//                " (Face)", "Test Image 4 (Object)", "Test Image 5 (Object)"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout
//                .simple_spinner_dropdown_item, items);
//        dropdown.setAdapter(adapter);
//        dropdown.setOnItemSelectedListener(this);
        initCustomModel();
    }


    private void initCustomModel() {
        // Replace with code from the codelab to initialize your custom model
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Functions for loading images from app assets.

    // Returns max storedImage width, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
    private Integer getImageMaxWidth() {
        if (mImageMaxWidth == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time storedImage
            // rendering time.
            mImageMaxWidth = mImageView.getWidth();
        }

        return mImageMaxWidth;
    }

    // Returns max storedImage height, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
    private Integer getImageMaxHeight() {
        if (mImageMaxHeight == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time storedImage
            // rendering time.
            mImageMaxHeight =
                    mImageView.getHeight();
        }

        return mImageMaxHeight;
    }

    // Gets the targeted width / height.
    private Pair<Integer, Integer> getTargetedWidthHeight() {
        int targetWidth;
        int targetHeight;
        int maxWidthForPortraitMode = getImageMaxWidth();
        int maxHeightForPortraitMode = getImageMaxHeight();
        targetWidth = maxWidthForPortraitMode;
        targetHeight = maxHeightForPortraitMode;
        return new Pair<>(targetWidth, targetHeight);
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream is;
        Bitmap bitmap = null;
        try {
            is = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    public void selectGalleryPhoto(View view){
        //select storedImage from gallery
//        Intent intent = new Intent();
//        intent.setType("storedImage/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE);




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
//        if (requestCode == PICK_IMAGE){
//            Uri uri = data.getData();
//            String [] proj = {MediaStore.Images.Media.DATA};
//            Cursor cursor = managedQuery(uri,proj,null,null,null);
//            int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            String path = cursor.getString(colIndex);
//            mSelectedImage = getBitmapFromAsset(this, path);
//            int test = 1;
//            setSelectedImage();
//        }
    }

    public void setSelectedImage (){
        Pair<Integer, Integer> targetedSize = getTargetedWidthHeight();

        int targetWidth = targetedSize.first;
        int maxHeight = targetedSize.second;

        // Determine how much to scale down the storedImage
        float scaleFactor =
                Math.max(
                        (float) mSelectedImage.getWidth() / (float) targetWidth,
                        (float) mSelectedImage.getHeight() / (float) maxHeight);

        Bitmap resizedBitmap =
                Bitmap.createScaledBitmap(
                        mSelectedImage,
                        (int) (mSelectedImage.getWidth() / scaleFactor),
                        (int) (mSelectedImage.getHeight() / scaleFactor),
                        true);

        mImageView.setImageBitmap(resizedBitmap);
        mSelectedImage = resizedBitmap;
    }

    public static void updateUI(VDText vdText){
        System.out.println("VDiction  - MainActivity: RawText is");
        System.out.println(vdText.getRawText());
        System.out.println("VDiction  - MainActivity: Translated text is");
        System.out.println(vdText.getTranslatedText());
    }

}
