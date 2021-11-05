package xyz.destiall.pixelate.states;

import android.graphics.Canvas;

import xyz.destiall.pixelate.graphics.Updateable;

public interface State extends Updateable {
    void render(Canvas canvas);
}
