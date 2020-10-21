package com.sapergis.vdiction.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sapergis.vdiction.R;

public class ImageTranslationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RAWTEXT = "param1";
    private static final String ARG_TRANSLATEDTEXT = "param2";

    private String rawText;
    private String translatedText;

    private TextView rawTextView;
    private TextView translatedTextView;
    //private LDTranslationViewModel ldTranslationViewModel;

    public ImageTranslationFragment() {
    }

    public static ImageTranslationFragment newInstance(@Nullable String rawText, @Nullable String translatedText) {
        ImageTranslationFragment fragment = new ImageTranslationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RAWTEXT, rawText);
        args.putString(ARG_TRANSLATEDTEXT, translatedText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rawText = getArguments().getString(ARG_RAWTEXT);
            translatedText = getArguments().getString(ARG_TRANSLATEDTEXT);
        }
        //ldTranslationViewModel = ViewModelProviders.of(this).get(LDTranslationViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //subscribe();
        final View rootView  = inflater.inflate(R.layout.fragment_translation, container, false);
        rawTextView = (TextView) rootView.findViewById(R.id.typeEditText);
        translatedTextView = (TextView) rootView.findViewById(R.id.translatedText);
        rawTextView.setMovementMethod(new ScrollingMovementMethod());
        rawTextView.setEditableFactory(new Editable.Factory());
        translatedTextView.setMovementMethod(new ScrollingMovementMethod());
        rawTextView.setText(rawText);
        translatedTextView.setText(translatedText);
        return rootView;
    }

//    private void subscribe() {
//        final Observer<VDText> vdTextObserver = new Observer<VDText>() {
//            @Override
//            public void onChanged(VDText vdText) {
//                System.out.println("VDction~~Observer FROM TRANSLATION ~~ getrawtext:"+vdText.getRawText());
//                System.out.println("VDction~~Observer FROM TRANSLATION  ~~ getTranslatedText :"+vdText.getTranslatedText());
//
//            }
//        };
//        ldTranslationViewModel.getLiveDataVDText().observeForever(vdTextObserver);
//    }

}
