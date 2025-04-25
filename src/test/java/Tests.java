import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.ses.re.assignment1.analysis.ReachingDefinitionsAnalysis;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.jimple.basic.Local;
import sootup.core.jimple.common.stmt.JAssignStmt;
import sootup.core.jimple.common.stmt.JReturnStmt;
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
import java.util.Set;

public class Tests {

    public static Path TEST_APPLICATION_PATH = Path.of("target/assignment-1-testapp.jar");
    public static String TestClass = "org.ses.re.assignment1.testapp.TestApp";
    public View view;

    public SootMethod test1;
    public SootMethod test2;
    public SootMethod test3;
    private ReachingDefinitionsAnalysis analysis;


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
        int expectedDefinitions = 3; // a = 5, i = 0, b = 7
        int actualDefinitions = 0;
        for (Stmt d : test1.getBody().getStmts()) {
            System.out.println("Stmt: " + d);
            System.out.println("size: " + analysis.getFlowAfter(d).size());
            actualDefinitions = analysis.getFlowAfter(d).size();

        }
        // check that the number of definitions is correct
        Assert.assertEquals(expectedDefinitions, actualDefinitions);
    }

    @Test
    public void testapp_test_if_else() {
        ReachingDefinitionsAnalysis analysis = new ReachingDefinitionsAnalysis(test2.getBody().getStmtGraph());
        List<Stmt>  stmts = test2.getBody().getStmts();
        // grab the three JAssignStmts whose defs should be in the join‐set
        System.out.println(stmts.get(8));
        JAssignStmt number0 = (JAssignStmt) stmts.get(1);
        JAssignStmt a1      = (JAssignStmt) stmts.get(3);
        JReturnVoidStmt ret      = (JReturnVoidStmt) stmts.get(8);
        JAssignStmt c3      = (JAssignStmt) stmts.get(9);

        Set<JAssignStmt> expectedAssignments = Set.of(number0, a1, c3);
        int actualAssignmentsCount = 0;


        for (Stmt d : test2.getBody().getStmts()) {
            if (d instanceof JAssignStmt) {
                JAssignStmt assign = (JAssignStmt) d;
                System.out.println("flow before: " + analysis.getFlowBefore(assign));
                Set<JAssignStmt> flowAfter = analysis.getFlowAfter(assign);
                System.out.println("flowAfter: " + flowAfter);
                if (flowAfter.contains(a1)) {
                    Assert.assertTrue(flowAfter.contains(number0));
                }
            } else if (d instanceof JReturnVoidStmt) {
                JReturnVoidStmt retStmt = (JReturnVoidStmt) d;
                Set<JAssignStmt> flowAfter = analysis.getFlowAfter(retStmt);
                System.out.println("flowAfter: " + flowAfter);
                Assert.assertEquals(expectedAssignments, flowAfter);
            }
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