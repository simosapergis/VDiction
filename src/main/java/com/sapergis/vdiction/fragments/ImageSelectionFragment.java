package com.sapergis.vdiction.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sapergis.vdiction.R;


public class ImageSelectionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button mPickFromCamera;
    private Button mPickFromGallery;
    private Button mTypeWord;

    private String mParam1;
    private String mParam2;
    public ImageSelectionFragmentListener listener;

    /*
    Interface for communication between ImageSelectionFragment and the parent activity has been
    created. In order to work, we need to add a reference to the parent. This is done in onAttach
    method further below.
     */
    public interface ImageSelectionFragmentListener{
        void imageFromCamera();
        void imageFromGallery();
        void typeWord();
    }


    public ImageSelectionFragment() {
        // Required empty public constructor
    }


    public static ImageSelectionFragment newInstance(String param1, String param2) {
        ImageSelectionFragment fragment = new ImageSelectionFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_selection, container, false);
        mPickFromCamera = (Button)view.findViewById(R.id.fromCameraButton);
        mPickFromGallery = (Button)view.findViewById(R.id.fromGalleryButton);
        mTypeWord = (Button)view.findViewById(R.id.typeWord);
        mPickFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.imageFromCamera();
            }
        });
        mPickFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.imageFromGallery();
            }
        });
        mTypeWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.typeWord();
            }
        });
        return view;
    }

    /*
    We need to create a reference of the listener in the parent activity and we can do it when the
    fragment will be attached to this particular activity.
    We check if the activity has implemented the interface otherwise we throw an exception.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ImageSelectionFragmentListener){
            listener = (ImageSelectionFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    + " must implement ImageSelectionFragmentListener");
        }
    }

    /*
    When the fragment ll be detached from the activity, we will remove the reference of the listener
    in our activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
