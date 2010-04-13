package com.piece_framework.makegood.ui.views;

import com.piece_framework.makegood.core.result.TestSuiteResult;

class RunProgress {
    private boolean isInitialized;
    private long processTime;
    private long startTimeForTestCase;
    private long processTimeForTestCase;
    private TestSuiteResult suite;

    RunProgress() {
        suite = new TestSuiteResult(null);
    }

    void initialize(TestSuiteResult suite) {
        this.suite = suite;
        startTimeForTestCase = System.nanoTime();
        processTime = 0;
        isInitialized = true;
    }

    boolean isInitialized() {
        return isInitialized;
    }

    int getAllTestCount() {
        return suite.getAllTestCount();
    }

    int getTestCount() {
        return suite.getTestCount();
    }

    int getPassCount() {
        return suite.getPassCount();
    }

    int getFailureCount() {
        return suite.getFailureCount();
    }

    int getErrorCount() {
        return suite.getErrorCount();
    }

    long getProcessTime() {
        return processTime;
    }

    int calculateRate() {
        if (suite.getAllTestCount() == 0) {
            return 0;
        }

        int rate = (int) (((double) suite.getTestCount() / (double) suite.getAllTestCount()) * 100d);
        return rate <= 100 ? rate : 100;
    }

    long calculateProcessTimeAverage() {
        if (suite.getTestCount() == 0) {
            return 0;
        }

        return getProcessTime() / suite.getTestCount();
    }

    void startTestCase() {
        startTimeForTestCase = System.nanoTime();
    }

    void endTestCase() {
        processTimeForTestCase = System.nanoTime() - startTimeForTestCase;
        processTime += processTimeForTestCase;
    }

    long getProcessTimeForTestCase() {
        return processTimeForTestCase;
    }

    boolean hasFailures() {
        return suite.hasFailures() || suite.hasErrors();
    }
}