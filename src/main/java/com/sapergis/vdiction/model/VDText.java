package com.sapergis.vdiction.model;

import com.sapergis.vdiction.interfaces.ITextOperations;

public class VDText  {
    String RawText;
    String translatedText;

    public String getRawText() {
        return RawText;
    }

    public void setRawText(String rawText) {
        RawText = rawText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }


}
