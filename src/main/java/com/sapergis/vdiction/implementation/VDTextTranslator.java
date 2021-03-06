package com.sapergis.vdiction.implementation;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.sapergis.vdiction.model.VDText;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class VDTextTranslator {
    public static final String GREEK_LOCALE = "el";
    public static final String ENGLISH_LOCALE = "en";
    public static final String TRANSLATION_SUCCESS = "SUCCESS";
    public static final String TRANSLATION_FAILURE = "FAILURE";
    private final String TAG = this.getClass().getSimpleName();
//    private String translateTo = null;
//    private String translateFrom;
    private Integer languageCodeTo;
    private Integer languageCodeFrom;
    private FirebaseTranslator fbTranslator;
    private FirebaseTranslatorOptions translatorOptions;
    private FirebaseModelDownloadConditions fbModelDownloadConditions;
    private MutableLiveData mutableVDText;
    private VDText vdText;

    public VDTextTranslator(VDText vdText,MutableLiveData<VDText> mutableVDText) {
        this.vdText = vdText;
        this.mutableVDText = mutableVDText;
       }

    public void startTranslation(){
            this.translatorOptions = setOptions();
            if(translatorOptions!=null){
                this.fbTranslator = createTranslator(translatorOptions);
                this.fbModelDownloadConditions = createModelConditions();
                downloadModel(fbModelDownloadConditions);
            }else{
               //TODO =>> message that the translation has failed
            }

    }

    //1. first define the language to be translated
    private FirebaseTranslatorOptions setOptions (){
        //TODO --> logic to get saved language in order to set translateFrom
        switch (vdText.getTranslateFromLocale()){
            case ENGLISH_LOCALE :
                vdText.setTranslateToLocale(GREEK_LOCALE);
                break;
            case GREEK_LOCALE :
                vdText.setTranslateToLocale(ENGLISH_LOCALE);
                break;
                default :
                    //TODO --> Maybe show a pop up
                    Log.e(TAG, "Translation to this language is not supported");
                    break;
        }
        if(vdText.getTranslateToLocale() != null){
            this.languageCodeFrom = FirebaseTranslateLanguage.languageForLanguageCode(vdText.getTranslateFromLocale());
            this.languageCodeTo = FirebaseTranslateLanguage.languageForLanguageCode(vdText.getTranslateToLocale());
            return new FirebaseTranslatorOptions.Builder()
                    .setSourceLanguage(languageCodeFrom)
                    .setTargetLanguage(languageCodeTo)
                    .build();
        }else{
            return null;
        }

        //createTranslator(options);
    }

    //2. Create the translator based on the options
    private FirebaseTranslator createTranslator (FirebaseTranslatorOptions options) {
        fbTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        return fbTranslator;
    }

    //3.Create the conditions of the translation model
    private FirebaseModelDownloadConditions createModelConditions (){
         return new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
    }

    //4.Download the Translation Model
    private void downloadModel(FirebaseModelDownloadConditions conditions){
        fbTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                // Model downloaded successfully. Okay to start translating.
                                // (Set a flag, unhide the translation UI, etc.)
                                translate();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, e.toString());
                            }
                        });

    }

    private void translate(){
        fbTranslator.translate(vdText.getRawText())
                .addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                vdText.setTranslatedText(s);
                mutableVDText.postValue(vdText);
                //Log.i(TAG, vdText.getTranslatedText());
            }
        });

    }

}
