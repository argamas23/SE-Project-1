/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.roller.weblogger.ui.rendering;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.config.WebloggerContext;
import org.apache.roller.weblogger.ui.rendering.velocity.VelocityRenderer;
import org.apache.roller.weblogger.util.Utilities;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Renderer {

    private static final Logger logger = LoggerFactory.getLogger(Renderer.class);

    private static final String TEMPLATE_DIR = "templates";

    private Renderer() {}

    public static void render(HttpServletRequest request, HttpServletResponse response, String template) {
        try {
            VelocityEngine velocity = getVelocityEngine(request.getServletContext());
            Map<String, Object> model = getModel(request, response);

            response.setContentType("text/html");
            velocity.mergeTemplate(template, "utf-8", model, response.getWriter());
        } catch (Exception e) {
            logger.error("Error rendering template", e);
            throw new WebloggerException("Error rendering template", e);
        }
    }

    private static VelocityEngine getVelocityEngine(ServletContext context) {
        VelocityEngine velocity = (VelocityEngine) context.getAttribute(VelocityRenderer.VELOCITY_KEY);
        if (velocity == null) {
            velocity = new VelocityRenderer(context).getVelocityEngine();
            context.setAttribute(VelocityRenderer.VELOCITY_KEY, velocity);
        }
        return velocity;
    }

    private static Map<String, Object> getModel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> model = new HashMap<>();
        model.put("request", request);
        model.put("response", response);
        model.put("context", request.getServletContext());
        return model;
    }

    public static void main(String[] args) {
        // No main method implementation
    }
}