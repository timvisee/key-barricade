package me.keybarricade.voxeltex.component.light;

import me.keybarricade.voxeltex.light.Light;
import org.joml.Vector3f;

public interface LightSourceComponentInterface {

    /**
     * Get the light instance.
     *
     * @return Light instance.
     */
    Light getLight();

    /**
     * Get the light type.
     *
     * Will be one of:
     * - LIGHT_TYPE_DIRECTIONAL
     * - LIGHT_TYPE_POINT
     * - LIGHT_TYPE_SPOT
     *
     * @return Light type.
     */
    int getLightType();

    /**
     * Set the light type.
     *
     * Choose from:
     * - LIGHT_TYPE_DIRECTIONAL
     * - LIGHT_TYPE_POINT
     * - LIGHT_TYPE_SPOT
     *
     * @param type Light type.
     */
    void setLightType(int type);

    /**
     * Get the color intensity of the light.
     *
     * @return Color intensity.
     */
    Vector3f getLightColor();

    /**
     * Set the color intensity of the light.
     *
     * @param color Color intensity.
     */
    void setLightColor(Vector3f color);

    /**
     * Get the light brightness.
     *
     * @return Light brightness.
     */
    float getLightBrightness();

    /**
     * Set the light brightness.
     *
     * @param brightness Light brightness.
     */
    void setLightBrightness(float brightness);
}