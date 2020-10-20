package com.sapergis.vdiction.implementation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.sapergis.vdiction.model.VDText;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class VDTextRecognizer {
    VDText vdText = new VDText();
    MutableLiveData<VDText>  mutableVDText;
    FirebaseVisionImage vdImage;


    public VDTextRecognizer(FirebaseVisionImage fbImage, MutableLiveData<VDText> mutableVDText) {
        this.vdImage = fbImage;
        this.mutableVDText = mutableVDText;
    }

    public void runTextRecognition() {
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(vdImage)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                String refinedText =  vdText.refineText(firebaseVisionText.getText());
                                vdText.setRawText(refinedText);
                                //mutableVDText.postValue(vdText);
                                new VDLanguageIdentifier(mutableVDText).identifyLanguage(vdText);
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

    public VDText getText() {
        return vdText;
    }

}
