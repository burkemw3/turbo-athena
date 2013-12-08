package burkemw3.turboathena.testfodder.tests;

import org.junit.Test;

import burkemw3.turboathena.testfodder.classes.ClassWithInnerClass;

public class TestReferencingInnerClass {

    @Test
    public void referenceInnerClass() {
        new ClassWithInnerClass.InnerClass();
    }

}
