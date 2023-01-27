package org.vizzoid.utils.engine;

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
