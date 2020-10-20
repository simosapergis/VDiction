package com.sapergis.vdiction.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.sapergis.vdiction.R;
import com.sapergis.vdiction.model.VDText;
import com.sapergis.vdiction.viewmodel.LDTranslationViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TranslationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TranslationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView rawTextView;
    private TextView translatedTextView;
    private LDTranslationViewModel ldTranslationViewModel;

    public TranslationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TranslationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TranslationFragment newInstance(String param1, String param2) {
        TranslationFragment fragment = new TranslationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ldTranslationViewModel = ViewModelProviders.of(this).get(LDTranslationViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView  = inflater.inflate(R.layout.fragment_translation, container, false);
        rawTextView = (TextView) rootView.findViewById(R.id.rawText);
        translatedTextView = (TextView) rootView.findViewById(R.id.translatedText);
        rawTextView.setText(mParam1);
        translatedTextView.setText(mParam2);
        return rootView;
    }

    private void subscribe(FirebaseVisionImage currentImage) {
        final Observer<VDText> vdTextObserver = new Observer<VDText>() {
            @Override
            public void onChanged(VDText vdText) {
                System.out.println("VDction~~Observer~~ getrawtext:"+vdText.getRawText());
                System.out.println("VDction~~Observer~~ getTranslatedText :"+vdText.getTranslatedText());

            }
        };
        ldTranslationViewModel.getTranslation(currentImage).observeForever(vdTextObserver);
    }

}
