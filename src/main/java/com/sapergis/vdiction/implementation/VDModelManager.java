package com.sapergis.vdiction.implementation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateModelManager;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateRemoteModel;

import java.util.Set;

import androidx.annotation.NonNull;

public class VDModelManager {
    FirebaseTranslateModelManager modelManager;

    public VDModelManager(){
        modelManager = FirebaseTranslateModelManager.getInstance();
    }
// Get translation models stored on the device.

    public void getAvailableModels(){
        modelManager.getAvailableModels(FirebaseApp.getInstance())
                .addOnSuccessListener(new OnSuccessListener<Set<FirebaseTranslateRemoteModel>>() {
                    @Override
                    public void onSuccess(Set<FirebaseTranslateRemoteModel> models) {
                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error.
                    }
                });
    }


    public void deleteModel(int modelNumber){
        // Delete the German model if it's on the device.
        FirebaseTranslateRemoteModel deModel =
                new FirebaseTranslateRemoteModel.Builder(modelNumber).build();
        modelManager.deleteDownloadedModel(deModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model deleted.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error.
                    }
                });

    }

   public void downloadModel(int modelNumber){
       // Download the French model.
       FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
               .requireWifi()
               .build();
       FirebaseTranslateRemoteModel frModel =
               new FirebaseTranslateRemoteModel.Builder(modelNumber)
                       .setDownloadConditions(conditions)
                       .build();
       modelManager.downloadRemoteModelIfNeeded(frModel)
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void v) {
                       // Model downloaded.
                   }
               })
               .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       // Error.
                   }
               });
   }

}
