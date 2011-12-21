/**
 * GravityRS.java
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

import android.content.res.Resources;
import android.renderscript.*;

public class GravityRS {
    public static final int PART_COUNT = 10000; // Count of particles

    public GravityRS() {
    }

    private Resources mRes;
    private RenderScriptGL mRS;
    private ScriptC_gravity mScript;

    public void init(RenderScriptGL rs, Resources res, int width, int height) {
        mRS = rs;
        mRes = res;

        ProgramFragmentFixedFunction.Builder pfb = new ProgramFragmentFixedFunction.Builder(rs);
        pfb.setVaryingColor(true);
        rs.bindProgramFragment(pfb.create());
        
        ScriptField_Point points = new ScriptField_Point(mRS, PART_COUNT);
        
        Mesh.AllocationBuilder smb = new Mesh.AllocationBuilder(mRS);
        smb.addVertexAllocation(points.getAllocation());
        smb.addIndexSetType(Mesh.Primitive.POINT);
        Mesh sm = smb.create();

        mScript = new ScriptC_gravity(mRS, mRes, R.raw.gravity);
        mScript.set_partMesh(sm);
        mScript.bind_point(points);
        mRS.bindRootScript(mScript);
        
        mScript.invoke_initParticles(); // Initialize Particles
    }

    public void newTouchPosition(float x, float y, float pressure, int id) {
        mScript.set_gTouchX(x);
        mScript.set_gTouchY(y);
    }
}

