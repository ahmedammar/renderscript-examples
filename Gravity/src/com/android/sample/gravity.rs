/**
 * gravity.rs
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
 
#pragma version(1)
#pragma rs java_package_name(com.android.sample)
#pragma stateFragment(parent)

#include "rs_graphics.rsh"

static int newPart = 0;
static int initialized = 0;

float gTouchX = 50.f;
float gTouchY = 50.f;

typedef struct __attribute__((packed, aligned(4))) Point {
    float2 delta;
    float2 position;
    uchar4 color;
} Point_t;
Point_t *point;

rs_mesh partMesh;

/**
 * Initialize the particles
 */
void initParticles()
{
    int size = rsAllocationGetDimX(rsGetAllocation(point));
    float width = rsgGetWidth();
    float height = rsgGetHeight();
    uchar4 c = rsPackColorTo8888(0.0f, 0.9f, 0.9f);
    Point_t *p = point;
    for (int i = 0; i < size; i++) {
    	p->position.x = rsRand(width);
    	p->position.y = rsRand(height);
    	p->delta.x = 0;
    	p->delta.y = 0;
	p->color = c;
	p++;
    }
    initialized = 1;
}

/**
 * root() is called every time a frame refresh occurs
 */
int root() {
		
    float width = rsgGetWidth();
    float height = rsgGetHeight();
    
    rsgClearColor(0.f, 0.f, 0.f, 1.f);
    
    int size = rsAllocationGetDimX(rsGetAllocation(point));
    Point_t *p = point;

    for (int i = 0; i < size; i++) {
        float diff_x = gTouchX - p->position.x;
        float diff_y = gTouchY - p->position.y;
        float acc = 50.f/(diff_x * diff_x + diff_y * diff_y);
        float acc_x = acc * diff_x;
        float acc_y = acc * diff_y;
        p->delta.x += acc_x;
        p->delta.y += acc_y;
	p->position.x += p->delta.x;
	p->position.y += p->delta.y;
        p->delta.x *= 0.96;
        p->delta.y *= 0.96;
	if (p->position.x > width) {
	    p->position.x = 0;
	} else if (p->position.x < 0) {
	    p->position.x = width;
	}
	if (p->position.y > height) {
	    p->position.y = 0;
	} else if (p->position.y < 0) {
	    p->position.y = height;
	}
        p++;
    }    
	
    rsgDrawMesh(partMesh);
    
    return 1;
}