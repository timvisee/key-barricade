/******************************************************************************
 * Copyright (c) Tim Visee 2016. All rights reserved.                         *
 *                                                                            *
 * @author Tim Visee                                                          *
 * @website http://timvisee.com/                                              *
 *                                                                            *
 * Open Source != No Copyright                                                *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software"), *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * You should have received a copy of The MIT License (MIT) along with this   *
 * program. If not, see <http://opensource.org/licenses/MIT/>.                *
 ******************************************************************************/

package com.timvisee.voxeltex.engine.light;

import com.timvisee.voxeltex.architecture.gameobject.AbstractGameObject;
import com.timvisee.voxeltex.module.shader.Shader;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class LightManager {

    /**
     * Maximum number of lights that can be handled by the light shader.
     */
    public static final int SHADER_LIGHT_LIMIT = 16;

    /**
     * Light sources available in the current scene.
     */
    private List<Light> lights = new ArrayList<>();

    /**
     * List of lights queued to be removed.
     */
    private List<Light> lightsRemoveQueue = new ArrayList<>();

    /**
     * Number of buffered lights.
     */
    private int bufferedLightCount = -1;

    /**
     * Types of the buffered lights.
     */
    private IntBuffer lightTypeBuffer;

    /**
     * Positions of the buffered lights.
     */
    private FloatBuffer lightPositionBuffer;

    /**
     * Rotation of the buffered lights.
     */
    private FloatBuffer lightRotationBuffer;

    /**
     * Color of the buffered lights.
     */
    private FloatBuffer lightColorBuffer;

    /**
     * Constructor.
     */
    public LightManager() { }

    /**
     * Constructor.
     *
     * @param type Light type.
     * @param gameObject Game object at the position of the light source.
     * @param color Light color intensity.
     * @param brightness Light brightness.
     */
    public Light createLight(int type, AbstractGameObject gameObject, Vector3f color, float brightness) {
        return addLight(new Light(type, gameObject, color, brightness));
    }

    /**
     * Constructor.
     *
     * @param type Light type.
     * @param position Light position in world space.
     * @param rotation Light rotation in world space.
     * @param color Light color intensity.
     * @param brightness Light brightness.
     */
    public Light createLight(int type, Vector3f position, Quaternionf rotation, Vector3f color, float brightness) {
        return addLight(new Light(type, position, rotation, color, brightness));
    }

    /**
     * Constructor.
     *
     * @param type Light type.
     * @param position Light position in world space.
     * @param rotation Light rotation in world space.
     * @param color Light color intensity.
     * @param brightness Light brightness.
     */
    public Light createLight(int type, Vector3f position, Vector3f rotation, Vector3f color, float brightness) {
        return addLight(new Light(type, position, rotation, color, brightness));
    }

    /**
     * Add a new light to the manager.
     *
     * @param light Light.
     */
    public Light addLight(Light light) {
        // Add the light
        this.lights.add(light);

        // Show a warning if there are more lights than can be handled
        if(this.lights.size() > SHADER_LIGHT_LIMIT)
            System.out.println("Warning: Some lights might not be rendered because the current number of lights " +
                    "exceeds the shader light limit of " + this.lights.size() + "/" + SHADER_LIGHT_LIMIT);

        // Return the instance
        return light;
    }

    /**
     * Get the list of lights.
     *
     * @return List of lights.
     */
    public List<Light> getLights() {
        return this.lights;
    }

    /**
     * Get the number of lights in the manager/
     *
     * @return Light count.
     */
    public int getLightCount() {
        return this.lights.size();
    }

    /**
     * Set the list of lights.
     *
     * @param lights List of lights.
     */
    private void setLights(List<Light> lights) {
        this.lights = lights;
    }

    /**
     * Remove the given light from the manager.
     *
     * @param light Light to remove.
     *
     * @return True if the light was removed, false if not.
     */
    public boolean removeLight(Light light) {
        return this.lightsRemoveQueue.add(light);
    }

    /**
     * Remove the light at the given light manager light index.
     *
     * @param i Index of the light to remove.
     *
     * @return Removed light, or null.
     */
    public Light removeLight(int i) {
        // Get the light
        Light light = this.lights.get(i);

        // Remove the light and return it
        removeLight(light);
        return light;
    }

    /**
     * Update the light manager.
     */
    public void update() {
        // Remove all lights that were queued to be removed
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, size = this.lightsRemoveQueue.size(); i < size; i++)
            // Remove the lights
            this.lights.remove(this.lightsRemoveQueue.get(i));

        // Clear the list of lights queued to be removed
        this.lightsRemoveQueue.clear();

        // Buffer the lights
        buffer();
    }

    /**
     * Buffer all current light data so it can be be send to shaders.
     */
    public void buffer() {
        // Compare the current number of lights to the buffered count, to check whether we should recreate the buffers
        if(getLightCount() != this.bufferedLightCount) {
            // Set the buffered count
            this.bufferedLightCount = getLightCount();

            // Recreate the buffers
            this.lightTypeBuffer = BufferUtils.createIntBuffer(this.bufferedLightCount);
            this.lightPositionBuffer = BufferUtils.createFloatBuffer(this.bufferedLightCount * 3);
            this.lightRotationBuffer = BufferUtils.createFloatBuffer(this.bufferedLightCount * 3);
            this.lightColorBuffer = BufferUtils.createFloatBuffer(this.bufferedLightCount * 4);
        }

        // Clear the buffers
        this.lightTypeBuffer.clear();
        this.lightPositionBuffer.clear();
        this.lightRotationBuffer.clear();
        this.lightColorBuffer.clear();

        // Add the lights to the buffers
        for(int i = 0, size = Math.min(this.bufferedLightCount, SHADER_LIGHT_LIMIT); i < size; i++) {
            this.lightTypeBuffer.put(this.lights.get(i).getType());

            this.lightPositionBuffer.put(this.lights.get(i).getPosition().x);
            this.lightPositionBuffer.put(this.lights.get(i).getPosition().y);
            this.lightPositionBuffer.put(this.lights.get(i).getPosition().z);

            this.lightRotationBuffer.put(this.lights.get(i).getRotation().x);
            this.lightRotationBuffer.put(this.lights.get(i).getRotation().y);
            this.lightRotationBuffer.put(this.lights.get(i).getRotation().z);

            this.lightColorBuffer.put(this.lights.get(i).getColor().x);
            this.lightColorBuffer.put(this.lights.get(i).getColor().y);
            this.lightColorBuffer.put(this.lights.get(i).getColor().z);
            this.lightColorBuffer.put(this.lights.get(i).getBrightness());
        }

        // Flip all buffers
        this.lightTypeBuffer.flip();
        this.lightPositionBuffer.flip();
        this.lightRotationBuffer.flip();
        this.lightColorBuffer.flip();
    }

    /**
     * Send the currently buffered data to the shader.
     *
     * @param shader The shader to send the light data to.
     */
    public void sendToShader(Shader shader) {
        // Buffer the lights if they haven't been buffered yet
        if(this.lightTypeBuffer == null)
            buffer();

        // Send the number of lights
        shader.setUniform1i("lightCount", this.bufferedLightCount);

        // Send the data
        shader.setUniform1iv("lightType", this.lightTypeBuffer);
        shader.setUniform3fv("lightPosition", this.lightPositionBuffer);
        shader.setUniform3fv("lightRotation", this.lightRotationBuffer);
        shader.setUniform4fv("lightColor", this.lightColorBuffer);
    }
}
