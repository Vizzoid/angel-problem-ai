package org.vizzoid.utils.engine;

public class Camera {

    private double scalingFactor;
    private double renderingDistance;
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

    public void setFov(double fov) {
        this.fov = fov;
        this.scalingFactor = 1 / Math.tan(Math.toRadians(fov) / 2); // cotangent
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

    public void reloadProjection() {
        this.projectionMultiplier = (renderingDistance / (renderingDistance - zNear));
        this.projectionSubtractive = (zNear * -projectionMultiplier);
    }

    public double getProjectionMultiplier() {
        return projectionMultiplier;
    }

    public double getProjectionSubtractive() {
        return projectionSubtractive;
    }

}
