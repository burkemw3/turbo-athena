package burkemw3.turboathena.testfodder.tests;

import org.junit.Test;

import burkemw3.turboathena.testfodder.classes.ChildClass;

public class TestReferencingInherited {

    @Test
    public void testWithInheritence() {
        ChildClass child = new ChildClass();
        child.aMethod();
    }

}
