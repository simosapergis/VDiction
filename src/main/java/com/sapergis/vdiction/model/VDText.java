package com.sapergis.vdiction.model;

import com.sapergis.vdiction.interfaces.ITextOperations;

public class VDText  {

    private String RawText;
    private String translatedText;
    private String translateFromLocale;
    private String translateToLocale;

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


    public String getTranslateFromLocale() {
        return translateFromLocale;
    }

    public void setTranslateFromLocale(String translateFromLocale) {
        this.translateFromLocale = translateFromLocale;
    }

    public String getTranslateToLocale() {
        return translateToLocale;
    }

    public void setTranslateToLocale(String translateToLocale) {
        this.translateToLocale = translateToLocale;
    }

    public String refineText(String string){
        return string.replaceAll("\\n"," ");
    }


}
