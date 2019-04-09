package com.example.demo.controller;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import com.example.demo.entity.Version;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("meta")
public class MetaController {

  @RequestMapping(value = "version", method = RequestMethod.GET)
  public Version version() {
    return new Version("1.0.0");
  }
}