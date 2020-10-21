package com.sapergis.vdiction.viewmodel;

import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.sapergis.vdiction.implementation.VDTextRecognizer;
import com.sapergis.vdiction.model.VDText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LDTranslationViewModel extends ViewModel {
    private MutableLiveData<VDText> vdText;
    public LDTranslationViewModel() {
        super();
    }

    public LiveData<VDText> getTranslation (FirebaseVisionImage currentImage){

        vdText = new MutableLiveData<VDText>();
        translate(currentImage);
        return vdText;
    }

    private void translate(FirebaseVisionImage currentImage) {
        VDTextRecognizer vdTextRecognizer = new VDTextRecognizer(currentImage, vdText);
        vdTextRecognizer.runTextRecognition();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
