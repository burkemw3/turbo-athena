import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class TurboAthena {
    public static void main(String[] args) throws IOException, ParseException {
        // parse command line options
        Options options = new Options();
        Option classOption = new Option("c", "class-directory", true, "Directory that contains class files");
        options.addOption(classOption);
        Option testOption = new Option("t", "test-directory", true, "Directory that contains test class files");
        options.addOption(testOption);
        Option modifiedOption = new Option("m", "modified-class", true, "Name of modified class (including package)");
        options.addOption(modifiedOption);

        CommandLineParser parser = new BasicParser();
        CommandLine line = parser.parse(options, args);

        ArrayList<String> implementationDirectories = new ArrayList<String>();
        implementationDirectories.addAll(Arrays.asList(line.getOptionValues(classOption.getLongOpt())));
        ArrayList<String> testDirectories = new ArrayList<String>();
        testDirectories.addAll(Arrays.asList(line.getOptionValues(testOption.getLongOpt())));
        ArrayList<String> modifiedClasses = new ArrayList<String>();
        modifiedClasses.addAll(Arrays.asList(line.getOptionValues(modifiedOption.getLongOpt())));

        // iterate directory to build dependency graph & tests
        HashMap<String, HashSet<String>> dependencies = new HashMap<String, HashSet<String>>();
        HashSet<String> tests = new HashSet<String>();
        for (String directoryPath : implementationDirectories) {
            File directory = new File(directoryPath);
            iterateDirectory(directory, dependencies, tests, false);
        }
        for (String testDirectoryPath : testDirectories) {
            File directory = new File(testDirectoryPath);
            iterateDirectory(directory, dependencies, tests, true);
        }

        // iterate changed classes to find affected tests
        TreeSet<String> testsToRun = new TreeSet<String>();
        HashSet<String> visited = new HashSet<String>();
        HashSet<String> toVisit = new HashSet<String>();
        toVisit.addAll(modifiedClasses);
        while (false == toVisit.isEmpty()) {
            HashSet<String> workingSet = new HashSet<String>(toVisit);
            toVisit.clear();
            for (String candidate : workingSet) {
                boolean isUnvisited = visited.add(candidate);
                if (false == isUnvisited) {
                    continue;
                }
                if (tests.contains(candidate)) {
                    testsToRun.add(candidate);
                }
                HashSet<String> futureVisits = dependencies.get(candidate);
                if (null != futureVisits) {
                    toVisit.addAll(futureVisits);
                }
            }
        }

        // print affected tests
        for (String testToRun : testsToRun) {
            System.out.println(testToRun);
        }
    }

    private static void iterateDirectory(File directory, HashMap<String, HashSet<String>> dependencies,
            HashSet<String> tests, boolean isTestDirectory) throws IOException {
        if (false == directory.isDirectory()) {
            throw new RuntimeException("passed directory is not directory");
        }
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                iterateDirectory(file, dependencies, tests, isTestDirectory);
            } else if (file.getName().endsWith(".class")) {
                ClassParser parser = new ClassParser(file.getAbsolutePath());
                JavaClass javaClass = parser.parse();

                DependencyVisitor dependencyVisitor = new DependencyVisitor(javaClass);
                dependencyVisitor.addDependencies(dependencies);

                if (isTestDirectory) {
                    TestVisitor testVisitor = new TestVisitor(javaClass);
                    testVisitor.flagTests(tests);
                }
            }
        }
    }
}
