package com.oozmakappa.oyeloans.Adapters;

/**
 * Created by sankarnarayanan on 08/09/16.
 */

import android.content.res.Resources;
import android.widget.BaseExpandableListAdapter;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.oozmakappa.oyeloans.Models.LoanSummaryModel;

import com.oozmakappa.oyeloans.R;

public class LoanSummaryExpandableAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<LoanSummaryModel>> _listDataChild;
    public Resources _res;

    public LoanSummaryExpandableAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<LoanSummaryModel>> listChildData, Resources resLocal) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._res = resLocal;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final LoanSummaryModel model = (LoanSummaryModel) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.tabitem, null);
        }

        TextView txtListChild1 = (TextView) convertView
                .findViewById(R.id.text);
        TextView txtListChild2 = (TextView) convertView
                .findViewById(R.id.text1);

        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        if(model.getLoanStatus().equals("Closed")){
            image.setImageResource(
                    this._res.getIdentifier(
                            "com.oozmakappa.oyeloans:drawable/closed"
                            ,null,null));
        }else if(model.getLoanStatus().equals("Pre- Closed")){
            image.setImageResource(
                    this._res.getIdentifier(
                            "com.oozmakappa.oyeloans:drawable/preclosed"
                            ,null,null));
        }else if(model.getLoanStatus().equals("Active")){
            image.setImageResource(
                    this._res.getIdentifier(
                            "com.oozmakappa.oyeloans:drawable/active"
                            ,null,null));
        }else{
            image.setImageResource(
                    this._res.getIdentifier(
                            "com.oozmakappa.oyeloans:drawable/application_icon"
                            ,null,null));
        }

        txtListChild1.setText(model.getLoanId());
        txtListChild2.setText("Status : "+model.getLoanStatus());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.summary_list_header, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
