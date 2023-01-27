package org.vizzoid.utils.engine;

/**
 * Mesh is an object composed of several triangles that can form a cube, pyramid, etc.
 * Draws all triangles into to avoid adding to Engine3D as whole and provides mutability.
 */
public class Mesh implements Object3D {

    private final Triangle[] triangles;

    public Mesh(Triangle... triangles) {
        this.triangles = triangles;
    }

    public Triangle[] getTriangles() {
        return triangles;
    }
    
    @Override
    public void draw(Graphics graphics, Engine3D engine) {
        for (Triangle triangle : triangles) {
            triangle.draw(graphics, engine);
        }
    }
    
}
