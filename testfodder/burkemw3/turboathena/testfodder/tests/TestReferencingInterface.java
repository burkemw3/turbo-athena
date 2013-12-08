package burkemw3.turboathena.testfodder.tests;

import org.junit.Test;

import burkemw3.turboathena.testfodder.classes.AnInterface;

public class TestReferencingInterface {

    @SuppressWarnings("null")
    @Test
    public void interfaceTest() {
        AnInterface iface = null;
        iface.aMethod();
    }

}
