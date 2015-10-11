package com.app.andy.bulkapp.data;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by Andy on 10/10/2015.
 * The test suite
 */
public class FullTestSuite extends TestSuite {

	public static Test suite() {
		return new TestSuiteBuilder(FullTestSuite.class).includeAllPackagesUnderHere().build();
	}

	public FullTestSuite() {super();}
}
