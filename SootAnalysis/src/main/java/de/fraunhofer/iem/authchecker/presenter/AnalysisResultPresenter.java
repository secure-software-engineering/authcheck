package de.fraunhofer.iem.authchecker.presenter;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
public interface AnalysisResultPresenter<T> {

  void present(T result);
}