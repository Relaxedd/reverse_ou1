import sootup.analysis.intraprocedural.ForwardFlowAnalysis;
import sootup.core.graph.BasicBlock;
import sootup.core.graph.StmtGraph;
import sootup.core.jimple.basic.Local;
import sootup.core.jimple.common.stmt.JAssignStmt;
import sootup.core.jimple.common.stmt.Stmt;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class ReachingDefinitionsAnalysis extends ForwardFlowAnalysis<Set<JAssignStmt>> {
    public <B extends BasicBlock<B>> ReachingDefinitionsAnalysis(StmtGraph<B> graph) {
        super(graph);
        execute();
    }

    /**
     * Soot uses (SSA) static single assignment form. E.g.:
     * int x = 0;
     * x = 2;
     * is represented as:
     * x#0 = 0
     * x#1 = 2
     * in jimple. Use String.contains() and String.split() methods to separate the variable name from the assignment number.     *
     * @param in
     * @param d
     * @param out
     */
    @Override
    protected void flowThrough(@Nonnull Set<JAssignStmt> in, Stmt d, @Nonnull Set<JAssignStmt> out) {
        // TODO
    }

    @Nonnull
    @Override
    protected Set<JAssignStmt> newInitialFlow() {
        return new HashSet<>();
    }

    @Override
    protected void merge(@Nonnull Set<JAssignStmt> in1, @Nonnull Set<JAssignStmt> in2, @Nonnull Set<JAssignStmt> out) {
        // TODO
    }

    @Override
    protected void copy(@Nonnull Set<JAssignStmt> in, @Nonnull Set<JAssignStmt> out) {
        out.addAll(in);
    }
}