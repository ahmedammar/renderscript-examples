/**
 * GravityView.java
 * Copyright (c) 2011 daoki2
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.android.sample;

import android.renderscript.RSSurfaceView;
import android.renderscript.RenderScript;
import android.renderscript.RenderScriptGL;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.MotionEvent;

public class GravityView extends RSSurfaceView {

    public GravityView(Context context) {
        super(context);
    }

    private RenderScriptGL mRS;
    private GravityRS mRender;

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        super.surfaceChanged(holder, format, w, h);
        if (mRS == null) {
            RenderScriptGL.SurfaceConfig sc = new RenderScriptGL.SurfaceConfig();
            mRS = createRenderScriptGL(sc);
            mRS.setSurface(holder, w, h);

            mRender = new GravityRS();
            mRender.init(mRS, getResources(), w, h);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mRS != null) {
            mRS = null;
            destroyRenderScriptGL();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        mRender.newTouchPosition(ev.getX(0), ev.getY(0), ev.getPressure(0), ev.getPointerId(0));        
        return true;
    }
}