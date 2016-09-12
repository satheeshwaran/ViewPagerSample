package layout;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.oozmakappa.oyeloans.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddNewDebitCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNewDebitCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewDebitCardFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match

    private OnFragmentInteractionListener mListener;
    public AddNewDebitCardFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddNewDebitCardFragment newInstance() {
        AddNewDebitCardFragment fragment = new AddNewDebitCardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_debit_card, container, false);
        Button addButton = (Button) view.findViewById(R.id.addCardButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onFragmentInteraction(null);
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onFragmentInteraction(null);
        }
    }
}
