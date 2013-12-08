package burkemw3.turboathena.testfodder.classes;

public class SimpleDirectDependency {

    public void noop() {
        SimpleIndirectDependency d = new SimpleIndirectDependency();
        d.noop();
    }

}
