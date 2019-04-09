package de.fraunhofer.iem.authchecker.util;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ReportWebView extends Application {

  public static void showReport() {
    launch();
  }

  @Override
  public void start(Stage stage) {
    Scene scene = new Scene(new WebViewBrowser("./report/report.html"));
    stage.setTitle("HTML Report");
    stage.setScene(scene);
    stage.setMaximized(true);
    stage.show();
  }
}
