package de.fraunhofer.iem.authchecker.adapter;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class MessageAdapter {

  public String renderMessage(String message, Map<String, String> additionalInformation)
      throws IOException {
    VelocityEngine ve = new VelocityEngine();
    ve.init();

    StringWriter writer = new StringWriter();

    VelocityContext context = new VelocityContext();
    context.put("additionalInformation", additionalInformation);

    ve.evaluate(context, writer, "message", message);
    return writer.toString();
  }
}
