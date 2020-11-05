package com.sapergis.vdiction.implementation;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class VDTextToSpeech {

    private TextToSpeech textToSpeech;
    private Context context;

    public VDTextToSpeech(Context context) {
        this.context = context;
        initialize();
    }

    private void initialize (){
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if ( status == TextToSpeech.SUCCESS){
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                    Log.d("TextToSpeech", "->> Initiated :: Language ->> "+lang);
                }
            }
        });
    }

    public void speak (String text){
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
    }

    public void shutDown (){
        textToSpeech.shutdown();
        Log.d("TextToSpeech", "->> was ShutDown");
    }
}
