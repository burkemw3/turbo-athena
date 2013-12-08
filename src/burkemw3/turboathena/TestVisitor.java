package burkemw3.turboathena;
import java.util.HashSet;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.JavaClass;

public class TestVisitor extends EmptyVisitor {
    private JavaClass _clazz;

    public TestVisitor(JavaClass jc) {
        _clazz = jc;
    }

    public void flagTests(HashSet<String> tests) {
        ConstantPool pool = _clazz.getConstantPool();
        for (int i = 0; i < pool.getLength(); ++i) {
            Constant constant = pool.getConstant(i);
            if (null == constant) {
                continue;
            }
            Byte tag = constant.getTag();
            if (Constants.CONSTANT_Utf8 != tag) {
                continue;
            }
            String constantAsString = pool.constantToString(constant);
            if (constantAsString.contains("Lorg/junit/Test")) {
                tests.add(_clazz.getClassName());
            }
        }
    }
}
