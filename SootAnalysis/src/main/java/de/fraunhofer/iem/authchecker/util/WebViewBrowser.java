package de.fraunhofer.iem.authchecker.util;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.io.File;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;

class WebViewBrowser extends Region {

  private WebView webView = new WebView();

  public WebViewBrowser(String htmlFilePath) {
    File f = new File(htmlFilePath);
    webView.getEngine().load(f.toURI().toString());
    getChildren().add(webView);
  }

  @Override
  protected void layoutChildren() {
    double w = getWidth();
    double h = getHeight();
    layoutInArea(webView, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
  }
}
