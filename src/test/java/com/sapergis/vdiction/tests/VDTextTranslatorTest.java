package com.sapergis.vdiction.tests;

import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.sapergis.vdiction.implementation.VDTextTranslator;
import com.sapergis.vdiction.model.VDText;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VDTextTranslatorTest {
    VDTextTranslator  vdTextTranslator;

    @Before
    public void setUp() throws Exception {
        VDText vdText = new VDText();
        vdText.setRawText("Hello, my name is Simos");
        String locale = "en";
        vdTextTranslator = new VDTextTranslator(locale,vdText);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setOptions() {
        FirebaseTranslatorOptions options = vdTextTranslator.setOptions();
        Assert.assertNotNull(options);
    }

    @Test
    public void translate() {
    }


}