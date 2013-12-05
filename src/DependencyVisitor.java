import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.JavaClass;

public class DependencyVisitor extends EmptyVisitor {
    private JavaClass _clazz;

    public DependencyVisitor(JavaClass jc) {
        _clazz = jc;
    }

    public void addDependencies(HashMap<String, HashSet<String>> dependencies) {
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
            Matcher matcher = Pattern.compile("L(([A-Za-z0-9]+/)+[A-Za-z0-9$]+)").matcher(constantAsString);
            while (matcher.find()) {
                String className = matcher.group(1).replaceAll("/", ".");
                if (className.equals(_clazz.getClassName())) {
                    continue;
                }
                if (false == dependencies.containsKey(className)) {
                    dependencies.put(className, new HashSet<String>());
                }
                dependencies.get(className).add(_clazz.getClassName());
            }
        }
    }
}