package burkemw3.turboathena;
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

    public void addDependencies(HashMap<String, HashSet<String>> dependencies) throws ClassNotFoundException {
        ConstantPool pool = _clazz.getConstantPool();
        for (int i = 0; i < pool.getLength(); ++i) {
            Constant constant = pool.getConstant(i);
            if (null == constant) {
                continue;
            }
            Byte tag = constant.getTag();
            if (Constants.CONSTANT_Utf8 == tag) {
                String constantAsString = pool.constantToString(constant);
                Matcher matcher = Pattern.compile("L(([A-Za-z0-9]+/)+[A-Za-z0-9$]+)").matcher(constantAsString);
                while (matcher.find()) {
                    String providerName = matcher.group(1).replaceAll("/", ".");
                    String dependentName = _clazz.getClassName();
                    if (providerName.equals(dependentName)) {
                        continue;
                    }
                    if (false == dependencies.containsKey(providerName)) {
                        dependencies.put(providerName, new HashSet<String>());
                    }
                    dependencies.get(providerName).add(dependentName);
                }
            }
            if (Constants.CONSTANT_Class == tag) {
                String providerName = pool.constantToString(constant);
                String dependentName = _clazz.getClassName();
                if (false == dependencies.containsKey(providerName)) {
                    dependencies.put(providerName, new HashSet<String>());
                }
                dependencies.get(providerName).add(dependentName);
            }
        }
        for (JavaClass iface : _clazz.getAllInterfaces()) {
            String providerName = _clazz.getClassName();
            String dependentName = iface.getClassName();
            if (false == dependencies.containsKey(providerName)) {
                dependencies.put(providerName, new HashSet<String>());
            }
            dependencies.get(providerName).add(dependentName);
        }
    }
}