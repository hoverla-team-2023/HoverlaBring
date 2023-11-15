package com.bobocode.hoverla.bring.demo.dispatcher.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.bobocode.hoverla.bring.demo.annotation.Controller;
import com.bobocode.hoverla.bring.demo.annotation.RequestMapping;

public class SimpleHandlerMapping {

  private final PathSegmentTreeNode root = new PathSegmentTreeNode();

  private static class PathSegmentTreeNode {

    Map<String, PathSegmentTreeNode> children = new HashMap<>();
    Handler handler;

    PathSegmentTreeNode getChild(String segment) {
      return children.get(segment);
    }

    void addChild(String segment, PathSegmentTreeNode child) {
      children.put(segment, child);
    }

  }

  public void scanControllers(String basePackage) {
    Reflections reflections = new Reflections(basePackage);
    Set<Class<?>> subTypesOf = reflections.getTypesAnnotatedWith(Controller.class);

    for (Class<?> clazz : subTypesOf) {
      Annotation controllerAnnotation = clazz.getAnnotation(Controller.class);
      if (controllerAnnotation != null) {
        processControllerClass(clazz);
      }
    }
  }

  public Handler getHandler(String requestURI) {
    String[] segments = requestURI.substring(1).split("/");
    PathSegmentTreeNode currentNode = root;

    for (String segment : segments) {
      PathSegmentTreeNode nextNode = currentNode.getChild(segment);
      if (nextNode == null) {
        return null;
      }
      currentNode = nextNode;
    }
    return currentNode.handler;
  }

  private void processControllerClass(Class<?> controllerClass) {
    for (Method method : controllerClass.getDeclaredMethods()) {
      Annotation requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
      if (requestMappingAnnotation != null) {
        String[] paths = ((RequestMapping) requestMappingAnnotation).value();
        for (String path : paths) {
          addPathToTree(path, new Handler(controllerClass, method, path));
        }
      }
    }
  }

  private void addPathToTree(String path, Handler handler) {
    String[] segments = path.split("/");
    PathSegmentTreeNode currentNode = root;

    for (String segment : segments) {
      PathSegmentTreeNode nextNode = currentNode.getChild(segment);
      if (nextNode == null) {
        nextNode = new PathSegmentTreeNode();
        currentNode.addChild(segment, nextNode);
      }
      currentNode = nextNode;
    }
    currentNode.handler = handler;
  }

}
