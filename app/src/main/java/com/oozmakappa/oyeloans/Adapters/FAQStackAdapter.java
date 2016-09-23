package com.oozmakappa.oyeloans.Adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;
import com.oozmakappa.oyeloans.R;

import java.util.ArrayList;
import java.util.HashMap;

public class FAQStackAdapter extends StackAdapter<Integer> {

    public static ArrayList<HashMap<String,String>> faqQuestions = new ArrayList<>();


    public static void prepareFAQData(){

        faqQuestions.add(new HashMap<String, String>() {{
            put("What is Oyeloans?","Oyeloans is a mobile app based short term personal loan provider, offers loans upto Rs.100000 to young salaried class professionals.");
        }});

        faqQuestions.add(new HashMap<String, String>() {{
            put("How does Oyeloan works?","a.\tDownload app – Download Oyeloans mobiel app from Google play store and install it.\n" +
                    "b.\tRegister/Login - Login / Register through your Facebook login.\n" +
                    "c.\tProvide details – Enter details like how much amount you want, personal details like name, address, contact details and bank account details.\n" +
                    "d.\tShare documents – Share your Pan card, address proof, salary slip and bank account statement to us.\n" +
                    "e.\tGet Cash – Once approved customer will get cash in their bank account directly.\n" +
                    "f.\tRepayments – Oyeoans provides a 12 month EMI plan, as per the due date the borrower can pay through ECS or NEFT/bank transfers.\n");
        }});


        faqQuestions.add(new HashMap<String, String>() {{
            put("Who can apply?","\tAll Salaried employees are eligible to apply for Oyeloan.\n");
        }});

        faqQuestions.add(new HashMap<String, String>() {{
            put("How should I apply for Oyeloan?","\tDownload Oyeloan app from playstore, login through your FB account and apply through your smartphone.\n");
        }});

        faqQuestions.add(new HashMap<String, String>() {{
            put("Can I apply through online?","Users can apply only through mobile app, cannot apply through other modes like laptop or system.\n");
        }});


        faqQuestions.add(new HashMap<String, String>() {{
            put("Why should I login through Facebook account?","Oyeloans is using Facebook login to assess the customer's creditworthiness in a better way, so that it collects less documents from the applicant.");
        }});


        faqQuestions.add(new HashMap<String, String>() {{
            put("What data will be collected from my social media accounts?","Oyeloans only access the customers general information's like name, education, employment, friends lists and timeline posts to better understand the creditworthiness of the borrower.");
        }});


        faqQuestions.add(new HashMap<String, String>() {{
            put("How much cash can I borrow?","Based on the eligibility, customer can borrow up to Rs.100000.");
        }});


        faqQuestions.add(new HashMap<String, String>() {{
            put("What is the minimum and maximum that can be borrowed?","Customer can borrow Minimum of Rs.10000 and Maximum of Rs.100000.");
        }});


        faqQuestions.add(new HashMap<String, String>() {{
            put("What is the loan duration?","12 monthly repayments");
        }});


        faqQuestions.add(new HashMap<String, String>() {{
            put("Do I need to provide any security, collateral or guarantors?","Oyeloan is a collateral free unsecured loan, so no security or document is required.");
        }});


        faqQuestions.add(new HashMap<String, String>() {{
            put("How should I repay the loan?","a.	ECS/NACH - Once the customer is approved Oyeloans will setup a ECS/NACH account with the customer, through that Oyeloans will debit the monthly EMI amount automatically. \n b.	Also, Repayment can be done through any one of the below methods to repay on or before due date. \n i.	Netbanking transfer \n ii.\tDebit card\niii.\tMobile wallets (like Paytm or Payu).");
        }});


        faqQuestions.add(new HashMap<String, String>() {{
            put("How safe is my data with Oyeloans?","Oyeloans ensures that the customer data is safe and secure by using various levels of security, like using SSL technology firewall");
        }});


        faqQuestions.add(new HashMap<String, String>() {{
            put("What documents are required to apply for a loan with Oyeloan?","a.\tSelf-Attested copy of the following documents in jpeg format:\n" +
                    "b.\tPhoto Identity Proof - PAN card;\n" +
                    "c.\tLatest 3 month salary slip (should have the current office address of the borrower);\n" +
                    "d.\tPermanent Address Proof - Any one of:\n" +
                    "e.\tAadhar Card;\n" +
                    "f.\tPassport;\n" +
                    "g.\tDriving License;\n" +
                    "h.\tUtility Bills i.e. Electric/landline phone bill/ Gas Bill (not more than 2 months old);\n" +
                    "i.\tVoter’s Identity Card.\n" +
                    "j.\tIf Current Address is different from Permanent Address, then additionally:\n" +
                    "i.\tUtility Bill- Electric/landline phone bill/ Gas Bill of current address (not more than 2 months old);\n" +
                    "ii.\tLeave & License Agreement/Rent Agreement;\n" +
                    "k.\tLatest statement of your Bank Account, showing Salary Credit.\n" +
                    "l.\tOne cancelled cheque leaf of your salaried bank account · For more information on this you may contact us on help@oyeloans.com or support@oyeloans.com\n");
        }});


        faqQuestions.add(new HashMap<String, String>() {{
            put("What documents are required to apply for a loan with Oyeloan?","a.\tSelf-Attested copy of the following documents in jpeg format:\n" +
                    "b.\tPhoto Identity Proof - PAN card;\n" +
                    "c.\tLatest 3 month salary slip (should have the current office address of the borrower);\n" +
                    "d.\tPermanent Address Proof - Any one of:\n" +
                    "e.\tAadhar Card;\n" +
                    "f.\tPassport;\n" +
                    "g.\tDriving License;\n" +
                    "h.\tUtility Bills i.e. Electric/landline phone bill/ Gas Bill (not more than 2 months old);\n" +
                    "i.\tVoter’s Identity Card.\n" +
                    "j.\tIf Current Address is different from Permanent Address, then additionally:\n" +
                    "i.\tUtility Bill- Electric/landline phone bill/ Gas Bill of current address (not more than 2 months old);\n" +
                    "ii.\tLeave & License Agreement/Rent Agreement;\n" +
                    "k.\tLatest statement of your Bank Account, showing Salary Credit.\n" +
                    "l.\tOne cancelled cheque leaf of your salaried bank account · For more information on this you may contact us on help@oyeloans.com or support@oyeloans.com\n");
        }});


        faqQuestions.add(new HashMap<String, String>() {{
            put("What are the fees associated with my loan?","a.\tProcessing fee –\n" +
                    "b.\tDefault / Late payment fee-\n" +
                    "i.\t3-day grace period\n" +
                    "ii.\tAfter which default fee of 1% of outstanding principal charged\n" +
                    "i.\tDD+15\n" +
                    "ii.\tDD+30 \n" +
                    "c.\tPre-closure fee – Nil\n" +
                    "d.\tPart-payment fee – Nil\n" +
                    "e.\tAny other fees??? Like\n" +
                    "i.\tStamp duty charges\n" +
                    "ii.\tLoan extension/rollover fee\n" +
                    "iii.\tCheque / ECS bounce charges\n");
        }});


        faqQuestions.add(new HashMap<String, String>() {{
            put("How many loans I can borrow?","One loan can be borrowed, borrower cannot borrow additional if already a loan is running.");
        }});

        faqQuestions.add(new HashMap<String, String>() {{
            put("Can I top-up my loan?","One loan can be borrowed, Currently no");
        }});


        faqQuestions.add(new HashMap<String, String>() {{
            put("How much time my loan application process takes for approval?","Within 24 hrs, depends on the borrowers document and co-operation with our team.");
        }});

    }

    public FAQStackAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindView(Integer data, int position, CardStackView.ViewHolder holder) {
        if (holder instanceof ColorItemViewHolder) {
            ColorItemViewHolder h = (ColorItemViewHolder) holder;
            h.onBind(data, position);
        }
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {

            default:
                view = getLayoutInflater().inflate(R.layout.list_card_item, parent, false);
                return new ColorItemViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.list_card_item;
    }

    static class ColorItemViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle;
        TextView answerText;

        public ColorItemViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
            answerText = (TextView) view.findViewById(R.id.answer_text_view);
        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(Integer data, int position) {
            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            if (FAQStackAdapter.faqQuestions.size() > position) {
                HashMap<String, String> question = faqQuestions.get(position);
                mTextTitle.setText((String) question.keySet().toArray()[0]);
                if (answerText != null)
                    answerText.setText(question.get((String)question.keySet().toArray()[0]));

            }
            else
                mTextTitle.setText(String.valueOf(position));
        }

    }

}
