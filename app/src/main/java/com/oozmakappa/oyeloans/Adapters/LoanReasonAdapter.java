package com.oozmakappa.oyeloans.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oozmakappa.oyeloans.R;

/**
 * Created by satheeshwaran on 9/19/16.
 */
public class LoanReasonAdapter extends BaseAdapter {
    private Context mContext;
    public Integer[] categoryIcons = {R.drawable.home_category,R.drawable.wedding_category,R.drawable.education_category,R.drawable.medical_category,R.drawable.business_category,R.drawable.car_category,R.drawable.other_category};
    public String[] categoryValues = {"Home Renovation","Wedding","Education","Medical Emergency","Business","Vehicle Repair","Others"};
    public String selectedCategory;
    // Constructor
    public LoanReasonAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return categoryIcons.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, final View convertView, ViewGroup parent) {
        /*ImageView imageView;
        TextView textView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(categoryIcons[position]);
        return imageView;*/

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(mContext);

            gridView = inflater.inflate( R.layout.layout_reason_cell , null);

            TextView textView = (TextView) gridView
                    .findViewById(R.id.categoryValueLabel);

            textView.setText(categoryValues[position]);

            final ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.categoryIcon);
            imageView.setTag(Integer.valueOf(position));

            imageView.setImageResource(categoryIcons[position]);

            final View finalGridView = gridView;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout layour = (RelativeLayout) imageView.getParent();

                    if (finalGridView.findViewById(R.id.selectedIcon).getVisibility() == View.INVISIBLE) {
                        (finalGridView.findViewById(R.id.selectedIcon)).setVisibility(View.VISIBLE);
                        layour.setBackgroundColor(mContext.getResources().getColor(R.color.ninty_percent_transparency_white));
                        selectedCategory = categoryValues[(Integer) v.getTag()];
                    }else{
                        (finalGridView.findViewById(R.id.selectedIcon)).setVisibility(View.INVISIBLE);
                        layour.setBackgroundColor(Color.TRANSPARENT);
                        selectedCategory = "";
                    }
                }
            });

        } else {

            gridView = (View) convertView;
        }

        return gridView;
    }

}
