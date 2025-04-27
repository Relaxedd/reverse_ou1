import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.ses.re.assignment1.analysis.ReachingDefinitionsAnalysis;
import sootup.core.inputlocation.AnalysisInputLocation;
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
import java.util.Set;
import java.util.stream.Collectors;

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
        List<Stmt> stmts = test1.getBody().getStmts();

        // grab the three JAssignStmts whose definitions should be in the join‐set
        JAssignStmt i0 = (JAssignStmt) stmts.get(1);
        JAssignStmt b7 = (JAssignStmt) stmts.get(3);
        JAssignStmt a5 = (JAssignStmt) stmts.get(4);

        // fetch the last statement in the method
        JReturnVoidStmt lastAssign = (JReturnVoidStmt) stmts.get(stmts.size() - 1);

        Set<JAssignStmt> expectedAssignments = Set.of(i0, b7, a5);
        Set<JAssignStmt> actualAssignments = analysis.getFlowAfter(lastAssign);

        System.out.println("actualAssignment: " + actualAssignments);
        Assert.assertEquals("should see 3 assignments", 3, actualAssignments.size());

        Assert.assertEquals(
                "the reaching definitions should be {number#1, i#2}",
                expectedAssignments,
                actualAssignments
        );

    }

    @Test
    public void testapp_test_if_else() {
        ReachingDefinitionsAnalysis analysis = new ReachingDefinitionsAnalysis(test2.getBody().getStmtGraph());
        List<Stmt>  stmts = test2.getBody().getStmts();

        // grab the three JAssignStmts whose definitions should be in the join‐set
        JAssignStmt number0 = (JAssignStmt) stmts.get(1);
        JAssignStmt a1      = (JAssignStmt) stmts.get(3);
        JAssignStmt c3      = (JAssignStmt) stmts.get(9);

        // fetch the last statement in the method
        JReturnVoidStmt lastAssign = (JReturnVoidStmt) stmts.get(stmts.size() - 1);

        Set<JAssignStmt> expectedAssignments = Set.of(number0, a1, c3);

        Set<JAssignStmt> actualAssignment = analysis.getFlowAfter(lastAssign);
        System.out.println("actualAssignment: " + actualAssignment);
        Assert.assertEquals("should have exactly 3 reaching definitions", 3, actualAssignment.size());
        Assert.assertEquals("the reaching definitions after the join should be {number0,a1,c3}", expectedAssignments, actualAssignment);
    }

    @Test
    public void testapp_test_for_loop() {
        ReachingDefinitionsAnalysis analysis = new ReachingDefinitionsAnalysis(test3.getBody().getStmtGraph());
        List<Stmt> stmts = test3.getBody().getStmts();

        System.out.println("stmts: " + stmts);


        List<JAssignStmt> assigns = stmts.stream()
                .filter(s -> s instanceof JAssignStmt)
                .map(s -> (JAssignStmt)s)
                .collect(Collectors.toList());

        Assert.assertEquals("should see 4 assignments", 4, assigns.size());

        // fetch the two statements expecting to be in the join‐set
        JAssignStmt lastAssign   = assigns.get(assigns.size() - 1);
        JAssignStmt number1Assign = assigns.get(assigns.size() - 2);

        Set<JAssignStmt> expected = Set.of(number1Assign, lastAssign);
        Set<JAssignStmt> actual = analysis.getFlowAfter(lastAssign);
        System.out.println("actualAssignments: " + actual);

        Assert.assertEquals(
                "after the last assignment in the loop body, exactly 2 definitions should remain",
                2,
                actual.size()
        );
        Assert.assertEquals(
                "the reaching definitions should be {number#1, i#2}",
                expected,
                actual
        );
    }



}