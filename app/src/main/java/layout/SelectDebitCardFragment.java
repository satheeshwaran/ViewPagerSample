package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooltechworks.creditcarddesign.CreditCardView;
import com.oozmakappa.oyeloans.Models.DebitCard;
import com.oozmakappa.oyeloans.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectDebitCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectDebitCardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DebitCard debitCard;
    private OnFragmentInteractionListener mListener;

    public SelectDebitCardFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SelectDebitCardFragment newInstance(DebitCard card) {
        SelectDebitCardFragment fragment = new SelectDebitCardFragment();
        fragment.debitCard = card;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_debit_card, container, false);
        if (this.debitCard != null) {
            CreditCardView creditCardView = (CreditCardView) view.findViewById(R.id.debitCard);
            creditCardView.setCVV(this.debitCard.debitCardCVV);
            creditCardView.setCardHolderName(this.debitCard.debitCardName);
            creditCardView.setCardExpiry(this.debitCard.debitCardExiry);
            creditCardView.setCardNumber(this.debitCard.debitCardNumber);
        }

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

}
