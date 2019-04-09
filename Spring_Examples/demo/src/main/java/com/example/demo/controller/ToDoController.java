package com.example.demo.controller;

/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/

import com.example.demo.entity.Todo;
import com.example.demo.exception.TodoValidationFailedException;
import com.example.demo.service.TodoService;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoController {

  @RequestMapping(value = "/todo/{id}", method = RequestMethod.GET)
  public Todo retrieve(@PathVariable Long id) {
    TodoService service = new TodoService();
    TodoController controller = new TodoController();
    controller.delete(id);
    return service.findWithId(id);
  }

  @RequestMapping(value = "/todo", method = RequestMethod.GET)
  public ArrayList<Todo> retrieveAll() {
    TodoService service = new TodoService();
    return service.list();
  }

  @RequestMapping(value = "/todo", method = RequestMethod.POST)
  public Todo create(@RequestBody String jsonString) {
    TodoService service = new TodoService();
    Todo todo = service.buildEntityFromJSONString(jsonString);
    service.add(todo);
    return todo;
  }

  @RequestMapping(value = "/todo/{id}", method = RequestMethod.PATCH)
  public Todo update(@PathVariable Long id, @RequestBody String jsonString) {
    TodoService service = new TodoService();
    Todo todo = service.findWithId(id);
    return service.update(todo, jsonString);
  }

  @RequestMapping(value = "/todo/{id}", method = RequestMethod.DELETE)
  public Todo delete(@PathVariable Long id) {
    TodoService service = new TodoService();
    Todo todo = service.findWithId(id);
    service.remove(todo);
    return todo;
  }
}
