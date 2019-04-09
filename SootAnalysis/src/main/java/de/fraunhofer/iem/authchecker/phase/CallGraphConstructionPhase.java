package de.fraunhofer.iem.authchecker.phase;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;

import de.fraunhofer.iem.authchecker.analysis.CweConfiguration;
import de.fraunhofer.iem.authchecker.artifact.Artifact;
import de.fraunhofer.iem.authchecker.artifact.CallGraphArtifact;
import de.fraunhofer.iem.authchecker.artifact.InputModelArtifact;
import de.fraunhofer.iem.authchecker.model.InputModel;
import de.fraunhofer.iem.authchecker.parser.AnnotationParser;
import de.fraunhofer.iem.authchecker.parser.ConfigurationParser;
import de.fraunhofer.iem.authchecker.transformer.AnnotationParserTransformer;
import de.fraunhofer.iem.authchecker.transformer.CallGraphConstructionTransformer;
import de.fraunhofer.iem.authchecker.transformer.ConfigurationParserTransformer;
import de.fraunhofer.iem.authchecker.util.LoggerOutputStream;
import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;

public class CallGraphConstructionPhase extends Phase<CweConfiguration> {

  private AnnotationParser annotationParser;

  private ConfigurationParser configurationParser;

  public CallGraphConstructionPhase(CweConfiguration config,
      Artifact... phaseArtifacts) {
    super(config, "callGraphConstruction", phaseArtifacts);
    this.annotationParser = new AnnotationParser();
    this.configurationParser = new ConfigurationParser();
  }

  public void run() {
    this.configureSoot();
    this.runSootTransformers();
  }

  private void configureSoot() {
    Options.v().set_no_bodies_for_excluded(true);
    Options.v().set_allow_phantom_refs(true);
    Options.v().set_output_format(Options.output_format_none);

    switch(this.config.getSootAlgorithm()) {
      case CHA:
        Options.v().setPhaseOption("cg.cha", "enabled:true");
        break;
      case RTA:
        Options.v().setPhaseOption("cg.spark", "on");
        Options.v().setPhaseOption("cg.spark", "rta:true");
        Options.v().setPhaseOption("cg.spark", "on-fly-cg:false");
        break;
      case VTA:
        Options.v().setPhaseOption("cg.spark", "on");
        Options.v().setPhaseOption("cg.spark", "vta:true");
        Options.v().setPhaseOption("cg.spark", "on-fly-cg:false");
        break;
      case SPARK:
        //not sure which kind of algorithm is used here... should be checked
        Options.v().setPhaseOption("cg.spark", "on");
        break;
      default:
        Options.v().setPhaseOption("cg.cha", "enabled:true");
    }

    Options.v().set_whole_program(true);
    Options.v().setPhaseOption("cg", "all-reachable:true");
    Options.v().setPhaseOption("cg", "verbose:true");

    String classPath = this.config.getRtJarPath()
        + ":" + this.config.getJceJarPath()
        + ":" + this.config.getProgramDirectory();

    Options.v().set_soot_classpath(classPath);
    Options.v().set_main_class(this.config.getMainClass());

    //logging soot directly to LOGGER
    LoggerOutputStream loggerOutputStream = new LoggerOutputStream(LOGGER, Level.TRACE);
    G.v().out = new PrintStream(loggerOutputStream);
  }

  private void runSootTransformers() {
    CallGraphConstructionTransformer cgTrans = new CallGraphConstructionTransformer(
        this.getInputModel(), this.config.getApplicationPackage());
    PackManager.v().getPack("wjtp").add(new Transform("wjtp.callGraphConstruction", cgTrans));

    ConfigurationParserTransformer configParserTrans = new ConfigurationParserTransformer(
        this.configurationParser,
        this.config.getConfigurationMethod(),
        this.config.getConfigurationClass()
    );

    PackManager.v().getPack("jtp").add(new Transform("jtp.configurationParser", configParserTrans));

    AnnotationParserTransformer annotationParserTrans = new AnnotationParserTransformer(
        this.annotationParser,
        this.config.getControllerPackage()
    );

    PackManager.v().getPack("jtp")
        .add(new Transform("jtp.annotationParser", annotationParserTrans));

    List<String> argsList = new ArrayList<String>();
    argsList.add(this.config.getMainClass());
    argsList.add(this.config.getConfigurationClass());
    argsList.addAll(this.config.getControllerClasses());

    String[] args = new String[argsList.size()];
    args = argsList.toArray(args);

    soot.Main.main(args);

    //transfer results to artifacts
    CallGraphArtifact cgArtifact = this.getCallGraphArtifact();
    cgArtifact.setCallGraphModel(cgTrans.getCallGraphModel());
    cgArtifact.setAnnotationEntities(annotationParser.getAnnotations());
    cgArtifact.setPatternAuthorizationEntities(configurationParser.getPatternEntities());
  }

  private InputModel getInputModel() {
    InputModelArtifact artifact = (InputModelArtifact) this.getArtifact("inputModel");
    return artifact.getInputModel();
  }

  private CallGraphArtifact getCallGraphArtifact() {
    return (CallGraphArtifact) this.getArtifact("callGraph");
  }
}