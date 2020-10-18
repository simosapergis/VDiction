package com.sapergis.vdiction.model;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.sapergis.vdiction.interfaces.ITextOperations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class VDImage implements  ITextOperations{

    public static final String TAG = "VDiction--->";
    private static String text;

    private FirebaseVisionImage fbVisionImage;
    private  Bitmap bitmap;

    public VDImage(Bitmap selectedImage) {
        this.bitmap = selectedImage;
        convertImage(selectedImage);
    }

    public VDImage(FirebaseVisionImage storedImage){
        this.fbVisionImage = storedImage;
    }


    public void runTextRecognition() {
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(fbVisionImage)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                 text = firebaseVisionText.getText();
                                 identifyLanguage(text);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        }
                );

    }


    @Override
    public void identifyLanguage(String text) {
        FirebaseLanguageIdentification languageIdentifier =
                FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
        languageIdentifier.identifyLanguage(text)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@Nullable String languageCode) {
                                if (languageCode != "und") {
                                    Log.i(TAG, "Language: " + languageCode);
                                    translateText(languageCode);
                                } else {
                                    Log.i(TAG, "Can't identify language.");
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG," Model couldn’t be loaded or other internal error.");
                                // ...
                            }
                        });
        return ;
    }

    @Override
    public void translateText(String locale) {
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
                        .setTargetLanguage(FirebaseTranslateLanguage.EL)
                        .build();
        final FirebaseTranslator englishToGreek =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        englishToGreek.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                // Model downloaded successfully. Okay to start translating.
                                // (Set a flag, unhide the translation UI, etc.)
                                Task temp = englishToGreek.translate(text);
                                Object o  = temp.getResult();
                                String translatedText = ((String) o);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be downloaded or other internal error.
                                // ...
                            }
                        });
    }


    @Override
    public String processTextRecognition() {
        return null;
    }

    private void convertImage(Bitmap bitmap){
        this.fbVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
    }


    public String getValue() {
        return text;
    }

    public void setValue(String value) {
        this.text = value;
    }


    public FirebaseVisionImage getFbVisionImage() {
        return fbVisionImage;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}
