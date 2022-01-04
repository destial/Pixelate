package xyz.destiall.pixelate.gui;

import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Updateable;

public interface View extends Renderable, Updateable, Listener {
    void destroy();
}
