package burkemw3.turboathena.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.TreeSet;

import org.junit.Test;

import burkemw3.turboathena.TurboAthena;

public class AbstractBaseClasseTest {

    @Test
    public void abstractBaseClassWithTestsModified_findTests_baseClassNotFlagged() throws Exception {
        ArrayList<String> implementationDirectories = new ArrayList<String>();
        implementationDirectories.add("bin/");

        ArrayList<String> testDirectories = new ArrayList<String>();
        testDirectories.add("bin/burkemw3/turboathena/testfodder/tests");

        ArrayList<String> modifiedClasses = new ArrayList<String>();
        modifiedClasses.add("burkemw3.turboathena.testfodder.tests.AbstractTestBaseClass");

        TreeSet<String> tests =
                TurboAthena.findTestsToRun(implementationDirectories, testDirectories, modifiedClasses);

        assertEquals(1, tests.size());
        assertFalse(tests.contains("burkemw3.turboathena.testfodder.tests.AbstractTestBaseClass"));
        assertTrue(tests.contains("burkemw3.turboathena.testfodder.tests.ConcreteTestBaseClass"));
    }

}
