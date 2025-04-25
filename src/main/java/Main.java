
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.jimple.common.stmt.JReturnStmt;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.model.SourceType;
import sootup.core.views.View;
import sootup.java.bytecode.inputlocation.ApkAnalysisInputLocation;
import sootup.java.bytecode.inputlocation.ArchiveBasedAnalysisInputLocation;
import sootup.java.core.views.JavaView;

import java.io.File;
import java.nio.file.Path;
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

        for (SootClass clazz : view.getClasses()) {
            for (SootMethod method : clazz.getMethods()) {
                ReachingDefinitionsAnalysis analysis = new ReachingDefinitionsAnalysis(method.getBody().getStmtGraph());
                for (Stmt d : method.getBody().getStmts()) {
                    if (d instanceof JReturnStmt) {
                        totalDefinitionsCnt += analysis.getFlowAfter(d).size();
                    }
                }
            }
        }

        System.out.println("Reachable Definitions: " + totalDefinitionsCnt);
    }
}