/*
 *  SoapUI Pro, copyright (C) 2007-2011 eviware software ab
 */

package soapui.demo

import com.eviware.soapui.model.support.TestRunListenerAdapter
import com.eviware.soapui.model.testsuite.TestRunContext
import com.eviware.soapui.model.testsuite.TestRunner
import groovy.util.logging.Slf4j

@Slf4j
public class DemoListener extends TestRunListenerAdapter {
    private long startTime

    public void beforeRun(TestRunner testRunner, TestRunContext runContext) {
        startTime = System.nanoTime()
    }

    public void afterRun(TestRunner testRunner, TestRunContext runContext) {
        long endTime = System.nanoTime()
        log.info("TestCase [{}] took {} groovy nanoseconds!",
                testRunner.testCase.name, (endTime - startTime));
    }
}
