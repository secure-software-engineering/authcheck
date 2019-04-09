package de.fraunhofer.iem.authchecker.util;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.io.OutputStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class LoggerOutputStream extends OutputStream {

  private Logger logger;

  private Level level;

  private String mem;

  public LoggerOutputStream(Logger logger, Level level) {
    this.logger = logger;
    this.level = level;
    this.mem = "";
  }

  public void write(int b) {
    byte[] bytes = new byte[1];
    bytes[0] = (byte) (b & 0xff);
    mem = mem + new String(bytes);

    if (mem.endsWith("\n")) {
      mem = mem.substring(0, mem.length() - 1);
      flush();
    }
  }

  public void flush() {
    logger.log(level, mem);
    mem = "";
  }
}





