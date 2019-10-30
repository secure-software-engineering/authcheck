package de.fraunhofer.iem.authchecker.adapter;
import java.io.File;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import de.fraunhofer.iem.authchecker.analysis.CweResult;

public class TemplateAdapter {

  public void buildReport(CweResult result) throws IOException {
    VelocityEngine ve = new VelocityEngine();
    ve.init();

    Template t = ve.getTemplate("./report.vm");
    
    File directory = new File("./report");
    
    if (!directory.exists()) {
    	if(!directory.mkdir()) {
    		throw new IOException("Can not create directory for the report.");
    	}
    }
    
    PrintWriter writer = new PrintWriter("./report/report.html");

    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.GERMANY);

    long seconds = (result.getFinishTime().getTime() - result.getStartTime().getTime()) / 1000;

    VelocityContext context = new VelocityContext();
    context.put("headline", "CWE Analysis");
    context.put("time", seconds);
    context.put("start", dateFormat.format(result.getStartTime()));
    context.put("finish", dateFormat.format(result.getFinishTime()));
    context.put("analysisResult", result);

    t.merge(context, writer);
    writer.flush();
  }

}
