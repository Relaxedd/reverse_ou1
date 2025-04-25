package org.ses.re.assignment1.analysis;

import sootup.analysis.intraprocedural.ForwardFlowAnalysis;
import sootup.core.graph.BasicBlock;
import sootup.core.graph.StmtGraph;
import sootup.core.jimple.basic.Local;
import sootup.core.jimple.common.stmt.JAssignStmt;
import sootup.core.jimple.common.stmt.Stmt;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Iterator;
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
        // Initialize the out set with the in set
        // Only handle assignment statements
        if (d instanceof JAssignStmt) {
            JAssignStmt assign = (JAssignStmt) d;
            System.out.println("Assign: " + assign);
            if (assign.getLeftOp() instanceof Local) {
                System.out.println("Local: " + assign.getLeftOp());
                // Extract the variable name (before the SSA index)
                String lhsRepr = assign.getLeftOp().toString();
                String varName = lhsRepr.contains("#")
                        ? lhsRepr.split("#")[0]
                        : lhsRepr;

                // KILL: remove any earlier defs of the same variable
                Iterator<JAssignStmt> it = out.iterator();
                while (it.hasNext()) {
                    JAssignStmt prev = it.next();
                    String prevLhs = prev.getLeftOp().toString();
                    String prevName = prevLhs.contains("#")
                            ? prevLhs.split("#")[0]
                            : prevLhs;

                    if (prevName.equals(varName)) {
                        it.remove();
                    }
                }

                // GEN: add the current definition
                out.add(assign);
            }
        }

    }

    @Nonnull
    @Override
    protected Set<JAssignStmt> newInitialFlow() {
        return new HashSet<>();
    }

    @Override
    protected void merge(@Nonnull Set<JAssignStmt> in1, @Nonnull Set<JAssignStmt> in2, @Nonnull Set<JAssignStmt> out) {
        out.clear();
        out.addAll(in1);
        out.addAll(in2);
    }

    @Override
    protected void copy(@Nonnull Set<JAssignStmt> in, @Nonnull Set<JAssignStmt> out) {
        out.clear();
        out.addAll(in);
    }
}