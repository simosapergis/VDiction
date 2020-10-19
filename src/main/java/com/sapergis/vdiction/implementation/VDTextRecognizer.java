package com.sapergis.vdiction.implementation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.sapergis.vdiction.model.VDText;

import androidx.annotation.NonNull;

public class VDTextRecognizer {
    VDText vdText = new VDText();
    FirebaseVisionImage vdImage;

    public VDTextRecognizer(FirebaseVisionImage fbImage) {
        this.vdImage = fbImage;
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
                                new VDLanguageIdentifier().identifyLanguage(vdText);
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
