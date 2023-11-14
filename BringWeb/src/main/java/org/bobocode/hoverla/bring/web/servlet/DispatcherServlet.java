package org.bobocode.hoverla.bring.web.servlet;

import java.io.IOException;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DispatcherServlet extends HttpServlet {

  private final ServletContext servletContext;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    log.info("Received a request by dispatcher servlet. Path: {}", req.getPathInfo());
    super.doGet(req, resp);
  }

}
