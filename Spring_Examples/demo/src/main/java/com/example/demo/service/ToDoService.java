package com.example.demo.service;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import com.example.demo.entity.Todo;
import com.example.demo.exception.TodoNotFoundException;
import com.example.demo.exception.TodoValidationFailedException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Simple service to handle in memory to do objects.
 */
public class TodoService {

  private final static ArrayList<Todo> TODO_LIST = new ArrayList<>();
  private static long id = 0;

  /**
   * Adds a new to do to the list.
   *
   * @param todo the entity of the todo that should be added
   */
  public void add(Todo todo) {
    TODO_LIST.add(todo);
  }

  /**
   * Removes a specific to do from the list.
   */
  public void remove(Todo todo) {
    TODO_LIST.remove(todo);
  }

  /**
   * Finds a specific to do with id.
   *
   * @param id the id of the to do that should be found
   * @return the specific to do entity
   */
  public Todo findWithId(long id) {
    for (Todo todo : TODO_LIST) {
      if (todo.getId() == id) {
        return todo;
      }
    }
    throw new TodoNotFoundException(id);
  }

  /**
   * Returns an array list of to dos.
   *
   * @return A list with to dos
   */
  public ArrayList<Todo> list() {
    return TODO_LIST;
  }

  /**
   * Builds a new to do entity from a json string and validates it indirectly.
   *
   * @param jsonString json representation of the object
   * @return If validation is successful it generates a new to do entity
   * @throws TodoValidationFailedException if the validation fails
   */
  public Todo buildEntityFromJSONString(String jsonString) throws TodoValidationFailedException {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode obj = mapper.readTree(jsonString);
      return new Todo(this.getNextId(), obj.get("content").asText());
    } catch (IOException | NullPointerException e) {
      throw new TodoValidationFailedException();
    }
  }

  /**
   * Updates the content of a given to do.
   */
  public Todo update(Todo todo, String jsonString) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode obj = mapper.readTree(jsonString);
      todo.setContent(obj.get("content").asText());
      return todo;
    } catch (IOException | NullPointerException e) {
      throw new TodoValidationFailedException();
    }
  }

  /**
   * Always returns the id for the next to do.
   *
   * @return next id
   */
  private long getNextId() {
    long act = id;
    id++;
    return act;
  }
}