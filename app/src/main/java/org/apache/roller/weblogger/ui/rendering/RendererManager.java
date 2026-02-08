package org.apache.roller.weblogger.ui.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages rendering of Roller UI components.
 */
public class RendererManager {

    private static final int MAX_RENDERERS = 10;

    private static Map<String, Renderer> renderers = new HashMap<>();

    private static Renderer defaultRenderer;

    static {
        // Initialize default renderer.
        defaultRenderer = new DefaultRenderer();
    }

    private RendererManager() {}

    public static void registerRenderer(String rendererType, Renderer renderer) {
        if (renderers.size() >= MAX_RENDERERS) {
            throw new RuntimeException("Maximum number of renderers reached");
        }
        renderers.put(rendererType, renderer);
    }

    public static Renderer getRenderer(String rendererType) {
        return renderers.getOrDefault(rendererType, defaultRenderer);
    }

    public static void render(String rendererType, Object component) {
        Renderer renderer = getRenderer(rendererType);
        if (renderer != null) {
            renderComponent(renderer, component);
        }
    }

    private static void renderComponent(Renderer renderer, Object component) {
        renderer.render(component);
    }

    public static void unregisterRenderer(String rendererType) {
        renderers.remove(rendererType);
    }

    public static List<Renderer> getRegisteredRenderers() {
        return new ArrayList<>(renderers.values());
    }

    private static class DefaultRenderer implements Renderer {
        @Override
        public void render(Object component) {
            // Default rendering logic
        }
    }

    public interface Renderer {
        void render(Object component);
    }
}