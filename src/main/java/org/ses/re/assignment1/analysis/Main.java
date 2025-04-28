package org.ses.re.assignment1.analysis;

import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.jimple.common.stmt.JAssignStmt;
import sootup.core.jimple.common.stmt.JReturnStmt;
import sootup.core.jimple.common.stmt.JReturnVoidStmt;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.model.SourceType;
import sootup.core.views.View;
import sootup.java.bytecode.inputlocation.ApkAnalysisInputLocation;
import sootup.java.bytecode.inputlocation.ArchiveBasedAnalysisInputLocation;
import sootup.java.core.views.JavaView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class Main {


    public static void main(String[] args) {

        File input = new File(args[0]);
        if (!input.exists() || !input.isFile()) {
            System.err.println("The file " + args[0] + " does not exist.");
            System.exit(1);
        }

        List<AnalysisInputLocation> analysisInputLocations = new ArrayList<>();

        if (args[0].endsWith(".apk")) {
            analysisInputLocations.add(new ApkAnalysisInputLocation(input.toPath(), SourceType.Application));
        } else if (args[0].endsWith(".jar")) {
            analysisInputLocations.add(new ArchiveBasedAnalysisInputLocation(input.toPath(), SourceType.Library));
        } else {
            System.err.println("Unknown input type");
            System.exit(1);
        }

        View view = new JavaView(analysisInputLocations);

        int totalDefinitionsCnt = 0;
        int returnCnt = 0;
        int returnVoidCnt = 0;


        for (SootClass clazz : view.getClasses()) {
            for (SootMethod method : clazz.getMethods()) {
                ReachingDefinitionsAnalysis analysis = new ReachingDefinitionsAnalysis(method.getBody().getStmtGraph());
                for (Stmt d : method.getBody().getStmts()) {
                    if (d instanceof JAssignStmt) {
                        totalDefinitionsCnt += analysis.getFlowAfter(d).size();
                    } else if (d instanceof JReturnStmt) {
                        returnCnt++;
                    } else if (d instanceof JReturnVoidStmt) {
                        returnVoidCnt++;
                    }
                }
            }
        }
        System.out.println("Return Statements: " + returnCnt);
        System.out.println("Return Void Statements: " + returnVoidCnt);
        System.out.println("Reachable Definitions: " + totalDefinitionsCnt);
    }
}