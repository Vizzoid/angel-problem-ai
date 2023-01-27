package org.vizzoid.utils.engine;

/**
 * Camera represents the user's view and from where they can see. It contains multiple settings such as rendering distance, zNear, and fov, but it also includes
 * useful content such as scaling factor and projection calculations which are calculated beforehand to be used repeatedly without unnecessary computation power
 */
public class Camera {

    private double scalingFactor;
    private double renderingDistance; // zFar
    private double zNear;
    private double projectionMultiplier;
    private double projectionSubtractive;
    private double fov;

    public Camera() {
        this.zNear = 0.1;
        this.renderingDistance = 1000;
        reloadProjection();
        setFov(90);
    }

    public double getFov() {
        return fov;
    }

    /**
     * Sets fov of camera, also calculates scaling factor before hand and stores it to be used without computing during paint process
     */
    public void setFov(double fov) {
        this.fov = fov;
        this.scalingFactor = 1 / Math.tan(Math.toRadians(fov) / 2); // cotangent
        // scaling factor is used so as the fov increases the point moves closer to off screen and vice-versa
    }

    public double getScalingFactor() {
        return scalingFactor;
    }

    public double getRenderingDistance() {
        return renderingDistance;
    }

    public double getZNear() {
        return zNear;
    }

    public void setRenderingDistance(double renderingDistance) {
        this.renderingDistance = renderingDistance;
        reloadProjection();
    }

    public void setZNear(double zNear) {
        this.zNear = zNear;
        reloadProjection();
    }

    /**
     * Reloads projection calculations which are used to project 3d point onto 2d plane (screen)
     */
    public void reloadProjection() {
        this.projectionMultiplier = (renderingDistance / (renderingDistance - zNear));
        this.projectionSubtractive = (zNear * -projectionMultiplier);
        
        // calculations of z
        // z * projectionMultiplier - projectionSubtractive
        // (z * (renderingDistance / (renderingDistance - zNear))) - (zNear * -(renderingDistance / (renderingDistance - zNear)))
        // (z * (zFar / (zFar - zNear))) - (zNear * -(zFar / (zFar - zNear)))
    }

    public double getProjectionMultiplier() {
        return projectionMultiplier;
    }

    public double getProjectionSubtractive() {
        return projectionSubtractive;
    }

}
