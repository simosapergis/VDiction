package com.sapergis.vdiction.fragments;

import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.sapergis.vdiction.implementation.VDLanguageIdentifier;
import com.sapergis.vdiction.implementation.VDTextRecognizer;
import com.sapergis.vdiction.model.VDText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TypeTranslationViewModel extends ViewModel {
    private MutableLiveData<VDText> mutableLiveData;
    public TypeTranslationViewModel() {
        super();
    }

    public LiveData<VDText> getTranslation (VDText vDText){
        mutableLiveData = new MutableLiveData<VDText>();
        translate(vDText);
        return mutableLiveData;
    }

    private void translate(VDText vDText) {
        new VDLanguageIdentifier(mutableLiveData).identifyLanguage(vDText);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
