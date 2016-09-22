package com.oozmakappa.oyeloans;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.oozmakappa.oyeloans.Models.LoanSummaryModel;
import com.oozmakappa.oyeloans.Models.SuccessModel;
import com.oozmakappa.oyeloans.helper.WebServiceCallHelper;
import com.oozmakappa.oyeloans.utils.SharedDataManager;
import com.oozmakappa.oyeloans.utils.Utils;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest(){
        super(Application.class);
    }
}