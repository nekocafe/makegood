package com.piece_framework.makegood.core.runner.junitxmlreader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.runners.ErrorReportingRunner;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.piece_framework.makegood.core.runner.ErrorTestCaseResult;
import com.piece_framework.makegood.core.runner.FailureTestCaseResult;
import com.piece_framework.makegood.core.runner.TestCaseResult;
import com.piece_framework.makegood.core.runner.Result;
import com.piece_framework.makegood.core.runner.TestSuiteResult;
import com.piece_framework.makegood.core.runner.junitxmlreader.JUnitXMLReader;

public class JUnitXMLReaderTest {
    private static final String BASE_DIR = "/home/matsu/GITWORK/stagehand-testrunner";
    private static final String EXAMPLES_DIR = BASE_DIR + "/examples";
    private static final String SRC_DIR = BASE_DIR + "/src";
    private static final String RESULTS_DIR = System.getProperty("user.dir") +
                                              String.valueOf(File.separatorChar) +
                                              "stagehand-testrunner-results";
    private static final String TMP_DIR = System.getProperty("java.io.tmpdir") +
                                          String.valueOf(File.separatorChar) +
                                          "com.piece_framework.makegood.launch.test";

    @BeforeClass
    public static void beforeClass() {
        new File(TMP_DIR).mkdirs();
    }

    @After
    public void tearDown() {
        File tmpDir = new File(TMP_DIR);
        for (File file: tmpDir.listFiles()) {
            file.delete();
        }
    }

    @AfterClass
    public static void afterClass() {
        new File(TMP_DIR).delete();
    }

    @Test
    public void readJUnitXML() throws InterruptedException {
        JUnitXMLReader parser = new JUnitXMLReader(
                                      new File(RESULTS_DIR + 
                                               String.valueOf(File.separatorChar) +
                                               "alltests.xml"
                                               ));
        startParser(parser, null);
        Thread.sleep(500);
        parser.stop();
        Thread.sleep(500);

        List<Result> results = parser.getTestResults();

        assertTrue(!parser.isActive());
        assertEquals(1, results.size());

        TestSuiteResult firstSuite = assertTestSuite(results.get(0),
                                               "The test suite generated by Stagehand_TestRunner",
                                               21, 4, 4,
                                               null,
                                               13
                                               );

        {
            String expectedTargetClass = "Stagehand_TestRunner_PHPUnitDataProviderTest";
            String expectedFile = EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitDataProviderTest.php";
            TestSuiteResult suite = assertTestSuite(firstSuite.getChildren().get(0),
                                              expectedTargetClass,
                                              4, 1, 0,
                                              expectedFile,
                                              1
                                              );
            TestSuiteResult childSuite = assertTestSuite(suite.getChildren().get(0),
                                                   "passWithDataProvider",
                                                   4, 1, 0,
                                                   expectedFile,
                                                   4
                                                   );
            assertTestCases(childSuite,
                            expectedTargetClass,
                            expectedFile,
                            new String[] {"passWithDataProvider with data set #0",
                                         "passWithDataProvider with data set #1",
                                         "passWithDataProvider with data set #2",
                                         "passWithDataProvider with data set #3"
                                         },
                            new int[] {82, 82, 82, 82}
                            );
            String failureContent = "Stagehand_TestRunner_PHPUnitDataProviderTest::passWithDataProvider with data set #3 (1, 1, 3)\n" +
"\n" +
"Failed asserting that <integer:2> matches expected <integer:3>.\n" +
"\n" +
EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitDataProviderTest.php:84\n" +
SRC_DIR + "/Stagehand/TestRunner/Runner/PHPUnitRunner.php:135\n" +
SRC_DIR + "/Stagehand/TestRunner.php:390\n" +
SRC_DIR + "/Stagehand/TestRunner.php:186\n" +
"/usr/share/php/src/Stagehand/CLIController.php:101\n" +
"/tmp/com.piece_framework.makegood.launch/phpunitrunner:50\n";
            assertProblems(
                childSuite,
                new Class[] { TestCaseResult.class, TestCaseResult.class, TestCaseResult.class, FailureTestCaseResult.class },
                new String[] { null, null, null, "PHPUnit_Framework_ExpectationFailedException" },
                new String[] { null, null, null, failureContent }
            );
        }

        {
            String expectedTargetClass = "Stagehand_TestRunner_PHPUnitDependsTest";
            String expectedFile = EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitDependsTest.php";
            TestSuiteResult suite = assertTestSuite(firstSuite.getChildren().get(1),
                                              expectedTargetClass,
                                              1, 1, 1,
                                              expectedFile,
                                              2
                                              );
            assertTestCases(suite,
                            expectedTargetClass,
                            expectedFile,
                            new String[] {"pass", "(Error)"},
                            new int[] {83, 0}
                            );
            String failureContent = "Stagehand_TestRunner_PHPUnitDependsTest::pass\n" +
"\n" +
"Failed asserting that <boolean:false> is true.\n" +
"\n" +
EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitDependsTest.php:85\n" +
SRC_DIR + "/Stagehand/TestRunner/Runner/PHPUnitRunner.php:135\n" +
SRC_DIR + "/Stagehand/TestRunner.php:390\n" +
SRC_DIR + "/Stagehand/TestRunner.php:186\n" +
"/usr/share/php/src/Stagehand/CLIController.php:101\n" +
"/tmp/com.piece_framework.makegood.launch/phpunitrunner:50\n";
            String errorContent = "Skipped Test" + SRC_DIR + "/Stagehand/TestRunner/Runner/PHPUnitRunner.php:135\n" +
SRC_DIR + "/Stagehand/TestRunner.php:390\n" +
SRC_DIR + "/Stagehand/TestRunner.php:186\n" +
"/usr/share/php/src/Stagehand/CLIController.php:101\n" +
"/tmp/com.piece_framework.makegood.launch/phpunitrunner:50\n";
            assertProblems(
                suite,
                new Class[] { FailureTestCaseResult.class, ErrorTestCaseResult.class },
                new String[] { "PHPUnit_Framework_ExpectationFailedException", "PHPUnit_Framework_SkippedTestError" },
                new String[] { failureContent, errorContent }
            );
        }

        {
            String expectedTargetClass = "Stagehand_TestRunner_PHPUnitErrorTest";
            String expectedFile = EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitErrorTest.php";
            TestSuiteResult suite = assertTestSuite(firstSuite.getChildren().get(2),
                                              expectedTargetClass,
                                              1, 0, 1,
                                              expectedFile,
                                              1
                                              );
            assertTestCases(suite,
                            expectedTargetClass,
                            expectedFile,
                            new String[] {"isError"},
                            new int[] {83}
                            );
            String errorContent = "Stagehand_TestRunner_PHPUnitErrorTest::isError\n" +
"\n" +
"Undefined property: Stagehand_TestRunner_PHPUnitErrorTest::$foo\n" +
"\n" +
EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitErrorTest.php:85\n" +
SRC_DIR + "/Stagehand/TestRunner/Runner/PHPUnitRunner.php:135\n" +
SRC_DIR + "/Stagehand/TestRunner.php:390\n" +
SRC_DIR + "/Stagehand/TestRunner.php:186\n" +
"/usr/share/php/src/Stagehand/CLIController.php:101\n" +
"/tmp/com.piece_framework.makegood.launch/phpunitrunner:50\n";
            assertProblems(
                suite,
                new Class[] { ErrorTestCaseResult.class },
                new String[] { "PHPUnit_Framework_Error_Notice" },
                new String[] { errorContent }
            );
        }

        {
            String expectedTargetClass = "Stagehand_TestRunner_PHPUnitExtendedTest";
            String expectedFile = EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitExtendedTest.php";
            TestSuiteResult suite = assertTestSuite(firstSuite.getChildren().get(3),
                                              expectedTargetClass,
                                              2, 0, 0,
                                              expectedFile,
                                              2
                                              );
            assertTestCases(suite,
                            expectedTargetClass,
                            expectedFile,
                            new String[] {"testTestShouldPassExtended", "testTestShouldPassCommon"},
                            new int[] {80, 80}
                            );
            assertProblems(
                suite,
                new Class[] { TestCaseResult.class, TestCaseResult.class },
                new String[] { null, null },
                new String[] { null, null }
            );
        }

        {
            String expectedTargetClass = "Stagehand_TestRunner_PHPUnitFailureTest";
            String expectedFile = EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitFailureTest.php";
            TestSuiteResult suite = assertTestSuite(firstSuite.getChildren().get(4),
                                              expectedTargetClass,
                                              1, 1, 0,
                                              expectedFile,
                                              1
                                              );
            assertTestCases(suite,
                            expectedTargetClass,
                            expectedFile,
                            new String[] {"isFailure"},
                            new int[] {83}
                            );
            String failureContent = "Stagehand_TestRunner_PHPUnitFailureTest::isFailure\n" +
"\n" +
"This is an error message.\n" +
"Failed asserting that <boolean:false> is true.\n" +
"\n" +
EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitFailureTest.php:85\n" +
SRC_DIR + "/Stagehand/TestRunner/Runner/PHPUnitRunner.php:135\n" +
SRC_DIR + "/Stagehand/TestRunner.php:390\n" +
SRC_DIR + "/Stagehand/TestRunner.php:186\n" +
"/usr/share/php/src/Stagehand/CLIController.php:101\n" +
"/tmp/com.piece_framework.makegood.launch/phpunitrunner:50\n";
            assertProblems(
                suite,
                new Class[] { FailureTestCaseResult.class },
                new String[] { "PHPUnit_Framework_ExpectationFailedException" },
                new String[] { failureContent }
            );
        }

        {
            String expectedTargetClass = "Stagehand_TestRunner_PHPUnitImcompleteTest";
            String expectedFile = EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitIncompleteTest.php";
            TestSuiteResult suite = assertTestSuite(firstSuite.getChildren().get(5),
                                              expectedTargetClass,
                                              1, 0, 1,
                                              expectedFile,
                                              1
                                              );
            assertTestCases(suite,
                            expectedTargetClass,
                            expectedFile,
                            new String[] {"testTestShouldBeImcomplete"},
                            new int[] {80}
                            );
            String errorContent = "Incomplete Test" + EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitIncompleteTest.php:82\n" +
SRC_DIR + "/Stagehand/TestRunner/Runner/PHPUnitRunner.php:135\n" +
SRC_DIR + "/Stagehand/TestRunner.php:390\n" +
SRC_DIR + "/Stagehand/TestRunner.php:186\n" +
"/usr/share/php/src/Stagehand/CLIController.php:101\n" +
"/tmp/com.piece_framework.makegood.launch/phpunitrunner:50\n";
            assertProblems(
                suite,
                new Class[] { ErrorTestCaseResult.class },
                new String[] { "PHPUnit_Framework_IncompleteTestError" },
                new String[] { errorContent }
            );
        }

        {
            String expectedTargetClass = "Stagehand_TestRunner_PHPUnitMultipleClasses1Test";
            String expectedFile = EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitMultipleClassesTest.php";
            TestSuiteResult suite = assertTestSuite(firstSuite.getChildren().get(6),
                                              expectedTargetClass,
                                              2, 0, 0,
                                              expectedFile,
                                              2
                                              );
            assertTestCases(suite,
                            expectedTargetClass,
                            expectedFile,
                            new String[] {"pass1", "pass2"},
                            new int[] {94, 102}
                            );
            assertProblems(
                 suite,
                 new Class[] { TestCaseResult.class, TestCaseResult.class },
                 new String[] { null, null },
                 new String[] { null, null }
            );
        }

        {
            String expectedTargetClass = "Stagehand_TestRunner_PHPUnitMultipleClasses2Test";
            String expectedFile = EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitMultipleClassesTest.php";
            TestSuiteResult suite = assertTestSuite(firstSuite.getChildren().get(7),
                                              expectedTargetClass,
                                              2, 0, 0,
                                              expectedFile,
                                              2
                                              );
            assertTestCases(suite,
                            expectedTargetClass,
                            expectedFile,
                            new String[] {"pass1", "pass2"},
                            new int[] {166, 174}
                            );
            assertProblems(
                suite,
                new Class[] { TestCaseResult.class, TestCaseResult.class },
                new String[] { null, null },
                new String[] { null, null }
            );
        }

        {
            String expectedTargetClass = "Stagehand_TestRunner_PHPUnitNoTestsTest";
            String expectedFile = EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitNoTestsTest.php";
            TestSuiteResult suite = assertTestSuite(firstSuite.getChildren().get(8),
                                              expectedTargetClass,
                                              1, 1, 0,
                                              expectedFile,
                                              1
                                              );
            assertTestCases(suite,
                            null,
                            null,
                            new String[] {"Warning"},
                            new int[] {0}
                            );
            String failureContent = "Warning\n" + 
"\n" +
"No tests found in class \"Stagehand_TestRunner_PHPUnitNoTestsTest\".\n" +
"\n" +
SRC_DIR + "/Stagehand/TestRunner/Runner/PHPUnitRunner.php:135\n" +
SRC_DIR + "/Stagehand/TestRunner.php:390\n" +
SRC_DIR + "/Stagehand/TestRunner.php:186\n" +
"/usr/share/php/src/Stagehand/CLIController.php:101\n" +
"/tmp/com.piece_framework.makegood.launch/phpunitrunner:50\n";
            assertProblems(
                suite,
                new Class[] { FailureTestCaseResult.class },
                new String[] { "PHPUnit_Framework_AssertionFailedError" },
                new String[] { failureContent }
            );
        }

        {
            String expectedTargetClass = "Stagehand_TestRunner_PHPUnitPassTest";
            String expectedFile = EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitPassTest.php";
            TestSuiteResult suite = assertTestSuite(firstSuite.getChildren().get(9),
                                              expectedTargetClass,
                                              3, 0, 0,
                                              expectedFile,
                                              3
                                              );
            assertTestCases(suite,
                            expectedTargetClass,
                            expectedFile,
                            new String[] {"passWithAnAssertion", "passWithMultipleAssertions", "日本語を使用できる"},
                            new int[] {83, 91, 100}
                            );
            assertProblems(
                suite,
                new Class[] { TestCaseResult.class, TestCaseResult.class, TestCaseResult.class },
                new String[] { null, null, null },
                new String[] { null, null, null }
            );
        }

        {
            String expectedTargetClass = "Stagehand_TestRunner_PHPUnitSkippedTest";
            String expectedFile = EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitSkippedTest.php";
            TestSuiteResult suite = assertTestSuite(firstSuite.getChildren().get(10),
                                              expectedTargetClass,
                                              1, 0, 1,
                                              expectedFile,
                                              1
                                              );
            assertTestCases(suite,
                            expectedTargetClass,
                            expectedFile,
                            new String[] {"testTestShouldBeSkipped"},
                            new int[] {80}
                            );
            String errorContent = "Skipped Test" + EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitSkippedTest.php:82\n" +
SRC_DIR + "/Stagehand/TestRunner/Runner/PHPUnitRunner.php:135\n" +
SRC_DIR + "/Stagehand/TestRunner.php:390\n" +
SRC_DIR + "/Stagehand/TestRunner.php:186\n" +
"/usr/share/php/src/Stagehand/CLIController.php:101\n" +
"/tmp/com.piece_framework.makegood.launch/phpunitrunner:50\n";
            assertProblems(
                suite,
                new Class[] { ErrorTestCaseResult.class },
                new String[] { "PHPUnit_Framework_SkippedTestError" },
                new String[] { errorContent }
            );
        }

        {
            String expectedTargetClass = "Stagehand_TestRunner_PHPUnitTest1_PHPUnitPassTest";
            String expectedFile = EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitTest1/PHPUnitPassTest.php";
            TestSuiteResult suite = assertTestSuite(firstSuite.getChildren().get(11),
                                              expectedTargetClass,
                                              1, 0, 0,
                                              expectedFile,
                                              1
                                              );
            assertTestCases(suite,
                            expectedTargetClass,
                            expectedFile,
                            new String[] {"testTestShouldPass"},
                            new int[] {80}
                            );
            assertProblems(
                suite,
                new Class[] { TestCaseResult.class },
                new String[] { null },
                new String[] { null }
            );
        }

        {
            String expectedTargetClass = "Stagehand_TestRunner_PHPUnitTest2_PHPUnitPassTest";
            String expectedFile = EXAMPLES_DIR + "/Stagehand/TestRunner/PHPUnitTest2/PHPUnitPassTest.php";
            TestSuiteResult suite = assertTestSuite(firstSuite.getChildren().get(12),
                                              expectedTargetClass,
                                              1, 0, 0,
                                              expectedFile,
                                              1
                                              );
            assertTestCases(suite,
                            expectedTargetClass,
                            expectedFile,
                            new String[] {"testTestShouldPass"},
                            new int[] {80}
                            );
            assertProblems(
                suite,
                new Class[] { TestCaseResult.class },
                new String[] { null },
                new String[] { null }
            );
        }
    }

    @Test
    public void readJUnitXMLInRealTime() {
        String[] suiteNames = new String[]{"The test suite generated by Stagehand_TestRunner",
                                           "Stagehand_TestRunner_PHPUnitDataProviderTest",
                                           "passWithDataProvider"
                                           };
        String[] caseNames = new String[]{"passWithDataProvider with data set #0",
                                          "passWithDataProvider with data set #1",
                                          "passWithDataProvider with data set #2",
                                          "passWithDataProvider with data set #3"
                                          };
        Class[] problemTypes = new Class[]{ FailureTestCaseResult.class };
        NamesAssertionJUnitXMLReaderListener listener = new NamesAssertionJUnitXMLReaderListener(suiteNames, caseNames, problemTypes);

        File xmlFile = new File(TMP_DIR +
                                String.valueOf(File.separatorChar) +
                                "realtime.xml"
                                );
        JUnitXMLReader parser = new JUnitXMLReader(xmlFile);
        parser.addParserListener(listener);
        startParser(parser, null);

        MockTestRunner testRunner = new MockTestRunner(xmlFile,
                                                         listOfFragmentFiles(),
                                                         1000
                                                         );
        testRunner.start();
        testRunner.waitForEnd();

        parser.stop();

        assertTrue(listener.clearAll());
    }

    @Test
    public void stop() throws InterruptedException {
        File xmlFile = new File(TMP_DIR + 
                                String.valueOf(File.separatorChar) +
                                "realtime.xml"
                                );
        JUnitXMLReader parser = new JUnitXMLReader(xmlFile);
        startParser(parser, SAXParseException.class);

        MockTestRunner testRunner = new MockTestRunner(xmlFile,
                                                         listOfFragmentFiles(),
                                                         1000
                                                         );
        testRunner.start();
        Thread.sleep(2000);
        parser.stop();
        Thread.sleep(2000);

        assertTrue(!parser.isActive());

        testRunner.waitForEnd();
    }

    private List<File> listOfFragmentFiles() {
        List<File> fragmentFiles = new ArrayList<File>();
        int index = 1;
        while (true) {
            File fragmentFile = new File(RESULTS_DIR +
                                         String.valueOf(File.separatorChar) +
                                         "realtime" + String.format("%02d", index) + ".xml"
                                         );
            if (!fragmentFile.exists()) {
                break;
            }

            fragmentFiles.add(fragmentFile);
            index++;
        }
        return fragmentFiles;
    }

    private void startParser(final JUnitXMLReader parser,
                             final Class expectedExceptionClass
                             ) {
        Thread parserThread = new Thread() {
            @Override
            public void run() {
                Exception actualException = null;

                try {
                    parser.start();
                } catch (ParserConfigurationException e) {
                    actualException = e;
                } catch (SAXException e) {
                    actualException = e;
                } catch (IOException e) {
                    actualException = e;
                }

                if (expectedExceptionClass != null) {
                    if (actualException != null) {
                        assertEquals(expectedExceptionClass, actualException.getClass());
                    } else {
                        fail();
                    }
                } else if (actualException != null) { 
                    actualException.printStackTrace();
                    fail();
                }
            }
        };
        parserThread.start();
    }

    private TestSuiteResult assertTestSuite(Result result,
                                      String expectedName,
                                      int expectedTestCount,
                                      int expectedFailureCount,
                                      int expectedErrorCount,
                                      String expectedFile,
                                      int expectedTestResultsCount
                                      ) {
        assertTrue(result instanceof TestSuiteResult);

        TestSuiteResult suite = (TestSuiteResult) result;
        assertEquals(expectedName, suite.getName());
        assertEquals(expectedTestCount, suite.getTestCount());
        assertEquals(expectedFailureCount, suite.getFailureCount());
        assertEquals(expectedErrorCount, suite.getErrorCount());
        assertEquals(expectedFile, suite.getFile());
        assertEquals(expectedTestResultsCount, suite.getChildren().size());
        return suite;
    }

    private TestCaseResult assertTestCase(
        Result result,
        String expectedName,
        String expectedTargetClass,
        String expectedFile,
        int expectedLine) {
        assertTrue(result instanceof TestCaseResult);

        TestCaseResult testCase = (TestCaseResult) result;
        assertEquals(expectedName, testCase.getName());
        assertEquals(expectedTargetClass, testCase.getClassName());
        assertEquals(expectedFile, testCase.getFile());
        assertEquals(expectedLine, testCase.getLine());

        return testCase;
    }

    private TestCaseResult assertProblem(
        TestCaseResult result,
        Class expectedProblemType,
        String expectedTypeClass,
        String expectedContent) {
        assertTrue(result instanceof TestCaseResult);

        assertEquals(expectedProblemType, result.getClass());
        assertEquals(expectedTypeClass, result.getFailureType());
        assertEquals(expectedContent, result.getFailureTrace());

        return result;
    }

    private TestCaseResult[] assertTestCases(Result result,
                                       String expectedTargetClass,
                                       String expectedFile,
                                       String[] expectedNames,
                                       int[] expectedLines
                                       ) {
        assertTrue(result instanceof TestSuiteResult);

        TestSuiteResult suite = (TestSuiteResult) result;
        List<TestCaseResult> testCases = new ArrayList<TestCaseResult>();
        for (int i = 0; i < suite.getChildren().size(); ++i) {
            TestCaseResult testCase = assertTestCase(suite.getChildren().get(i),
                                               expectedNames[i],
                                               expectedTargetClass,
                                               expectedFile,
                                               expectedLines[i]
                                               );
            testCases.add(testCase);
        }
        return testCases.toArray(new TestCaseResult[] {});
    }

    private Class[] assertProblems(
        Result result,
        Class[] expectedProblemTypes,
        String[] expectedTypeClasses,
        String[] expectedContents) {
        assertTrue(result instanceof TestSuiteResult);

        TestSuiteResult suite = (TestSuiteResult) result;
        List<Class> problems = new ArrayList<Class>();
        for (int i = 0; i < suite.getChildren().size(); ++i) {
            TestCaseResult testCase = (TestCaseResult)suite.getChildren().get(i);
            TestCaseResult problem =
                assertProblem(
                    testCase,
                    expectedProblemTypes[i],
                    expectedTypeClasses[i],
                    expectedContents[i]
                );
            problems.add(problem.getClass());
        }

        return problems.toArray(new Class[] {});
    }
}
