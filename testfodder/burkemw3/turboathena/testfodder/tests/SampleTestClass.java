package burkemw3.turboathena.testfodder.tests;

import org.junit.Test;

import burkemw3.turboathena.testfodder.classes.SimpleDirectDependency;

public class SampleTestClass {

    @Test
    public void aFakeTest() {
        SimpleDirectDependency clazz = new SimpleDirectDependency();
        clazz.noop();
    }

}
