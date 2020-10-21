package com.sapergis.vdiction.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sapergis.vdiction.R;
import com.sapergis.vdiction.model.VDText;

public class TypeTranslationFragment extends Fragment {

    private TypeTranslationViewModel mViewModel;
    private LiveData<VDText> liveDataVDText;
    private EditText typeEditText;
    private TextView translatedTextView;
    private Button translateButton;

    public static TypeTranslationFragment newInstance() {
        return new TypeTranslationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.type_translation_fragment, container, false);
        typeEditText = (EditText) view.findViewById(R.id.typeEditText);
        translatedTextView = (TextView) view.findViewById(R.id.translatedText);
        translateButton = (Button) view.findViewById(R.id.translateButton);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VDText vdText = new VDText();
                vdText.setRawText(typeEditText.getText().toString());
                subscribe(vdText);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TypeTranslationViewModel.class);
    }


    private void subscribe(VDText vdText) {
        final Observer<VDText> vdTextObserver = new Observer<VDText>() {
            @Override
            public void onChanged(VDText vdText) {
                translatedTextView.setText(vdText.getTranslatedText());
                translatedTextView.setVisibility(View.VISIBLE);
                removeLiveDataObserver();
            }
        };
        liveDataVDText = mViewModel.getTranslation(vdText);
        liveDataVDText.observe(this,vdTextObserver);
    }

    private void removeLiveDataObserver() {
        liveDataVDText.removeObservers(this);
    }
}
