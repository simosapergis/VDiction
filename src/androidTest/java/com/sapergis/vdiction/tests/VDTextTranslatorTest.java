package com.sapergis.vdiction.tests;

import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.sapergis.vdiction.implementation.VDTextTranslator;
import com.sapergis.vdiction.model.VDText;

import org.junit.Before;
import org.junit.Test;


public class VDTextTranslatorTest {
    private static final String RAW_TEXT = "good evening";
    private static final String EXCPECTED_TRANSLATION = "καλησπέρα";

    VDText vdText;
    VDTextTranslator  vdTextTranslator;
    FirebaseTranslatorOptions options;
    FirebaseTranslator fbTranslator;
    FirebaseModelDownloadConditions fbModelConditions;


    @Before
    public void setUp() throws Exception {
        vdText = new VDText();
        vdText.setRawText(RAW_TEXT);

        vdTextTranslator = new VDTextTranslator(VDTextTranslator.ENGLISH_LOCALE,vdText);

    }



    @Test
    public void translate() {
        vdTextTranslator.startTranslation();
    }


}