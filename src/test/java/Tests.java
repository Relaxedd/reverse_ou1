import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.ses.re.assignment1.analysis.ReachingDefinitionsAnalysis;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.jimple.basic.Local;
import sootup.core.jimple.common.stmt.JAssignStmt;
import sootup.core.jimple.common.stmt.JReturnVoidStmt;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.SootMethod;
import sootup.core.model.SourceType;
import sootup.core.types.ClassType;
import sootup.core.types.VoidType;
import sootup.core.views.View;
import sootup.java.bytecode.inputlocation.ArchiveBasedAnalysisInputLocation;
import sootup.java.core.views.JavaView;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tests {

    public static Path TEST_APPLICATION_PATH = Path.of("target/assignment-1-testapp.jar");
    public static String TestClass = "org.ses.re.assignment1.testapp.TestApp";
    public View view;

    public SootMethod test1;
    public SootMethod test2;
    public SootMethod test3;


    /**
     * Boilerplate code to load the test application into a view and retrieve the SootMethods from the view.
     */
    @Before
    public void before() {
        List<AnalysisInputLocation> analysisInputLocations = new ArrayList<>();
        analysisInputLocations.add(new ArchiveBasedAnalysisInputLocation(TEST_APPLICATION_PATH, SourceType.Library));
        this.view = new JavaView(analysisInputLocations);

        ClassType testClassType = view.getIdentifierFactory().getClassType(TestClass);
        test1 = view.getMethod(view.getIdentifierFactory().getMethodSignature(testClassType, "test1", VoidType.getInstance(), Collections.emptyList())).orElse(null);
        test2 = view.getMethod(view.getIdentifierFactory().getMethodSignature(testClassType, "test2", VoidType.getInstance(), Collections.emptyList())).orElse(null);
        test3 = view.getMethod(view.getIdentifierFactory().getMethodSignature(testClassType, "test3", VoidType.getInstance(), Collections.emptyList())).orElse(null);

        Assert.assertNotNull(test1);
    }

    @Test
    public void testapp_test_basic() {
        ReachingDefinitionsAnalysis analysis = new ReachingDefinitionsAnalysis(test1.getBody().getStmtGraph());
        for (Stmt d : test1.getBody().getStmts()) {
            System.out.println("Stmt: " + d);
            System.out.println(analysis.getFlowAfter(d).toString());

        }
    }

    @Test
    public void testapp_test_if_else() {
        ReachingDefinitionsAnalysis analysis = new ReachingDefinitionsAnalysis(test2.getBody().getStmtGraph());
        for (Stmt d : test2.getBody().getStmts()) {
            // TODO
        }
    }

    @Test
    public void testapp_test_for_loop() {
        ReachingDefinitionsAnalysis analysis = new ReachingDefinitionsAnalysis(test3.getBody().getStmtGraph());
        for (Stmt d : test3.getBody().getStmts()) {
            // TODO
        }
    }



}