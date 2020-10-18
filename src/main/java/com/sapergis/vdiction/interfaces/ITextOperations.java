package com.sapergis.vdiction.interfaces;

import com.sapergis.vdiction.model.VDText;

public interface ITextOperations {

    void identifyLanguage(String text);
    void translateText(String text);
    String processTextRecognition();
}
