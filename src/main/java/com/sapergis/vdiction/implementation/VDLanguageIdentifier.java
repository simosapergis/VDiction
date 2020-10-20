package com.sapergis.vdiction.implementation;


import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.sapergis.vdiction.model.VDText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;


/**
 * Created by Σίμος on 20/8/2019.
 */

public class VDLanguageIdentifier {

    //private String locale = null;
    private VDText vdText;
    private MutableLiveData<VDText> mutableVDText;
    final String TAG  = "Vdiction Message: ";

    public VDLanguageIdentifier(MutableLiveData<VDText> mutableVDText) {
        this.mutableVDText = mutableVDText;
    }

    public void identifyLanguage(final VDText vdText){
        this.vdText = vdText;
        FirebaseLanguageIdentification languageIdentifier = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
            languageIdentifier.identifyLanguage(vdText.getRawText())
                    .addOnSuccessListener(
                            new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(@Nullable String languageCode) {
                                    if (languageCode != "und") {
                                        Log.i(TAG, "Language: " + languageCode);
                                        //locale = languageCode;
                                        vdText.setTranslateFromLocale(languageCode);
                                        VDTextTranslator vdTextTranslator = new VDTextTranslator(vdText, mutableVDText);
                                        vdTextTranslator.startTranslation();
                                    } else {
                                        Log.i(TAG, "Can't identify language.");
                                    }


                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });

    }

    public VDText getVDText() {
        return vdText;
    }


}
