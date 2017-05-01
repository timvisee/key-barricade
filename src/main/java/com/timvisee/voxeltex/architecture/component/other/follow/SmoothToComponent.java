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

package com.timvisee.voxeltex.architecture.component.other.follow;

import com.timvisee.voxeltex.architecture.component.BaseComponent;
import com.timvisee.voxeltex.architecture.gameobject.AbstractGameObject;
import com.timvisee.voxeltex.module.transform.Transform;
import com.timvisee.voxeltex.runtime.global.Time;
import com.timvisee.voxeltex.util.math.quaternion.QuaternionfFactory;
import com.timvisee.voxeltex.util.math.vector.Vector3fFactory;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SmoothToComponent extends BaseComponent {

    /**
     * Define whether the target positions are in world space.
     */
    public boolean worldSpace = true;

    /**
     * Target position to smooth to, or null.
     */
    private Vector3f targetPosition = Vector3fFactory.identity();

    /**
     * Define whether to use the target position.
     */
    private boolean useTargetPosition = false;

    /**
     * Target rotation to smooth to, or null.
     */
    private Quaternionf targetRotation = QuaternionfFactory.identity();

    /**
     * Define whether to use the target rotation.
     */
    private boolean useTargetRotation = false;

    /**
     * Positional damping.
     */
    private float positionDamping = 4.5f;

    /**
     * Rotational damping.
     */
    private float rotationDamping = 3f;

    /**
     * Constructor.
     */
    public SmoothToComponent() { }

    /**
     * Constructor.
     *
     * @param worldSpace True if the target position and rotation are in world space, false if they are local.
     * @param targetPosition Target position to smooth to.
     */
    public SmoothToComponent(boolean worldSpace, Vector3f targetPosition) {
        this(worldSpace, targetPosition, null);
    }

    /**
     * Constructor.
     *
     * @param worldSpace True if the target position and rotation are in world space, false if they are local.
     * @param targetPosition Target position to smooth to.
     * @param positionDamping Amount of positional damping while smoothing.
     */
    public SmoothToComponent(boolean worldSpace, Vector3f targetPosition, float positionDamping) {
        this(worldSpace, targetPosition);
        this.positionDamping = positionDamping;
    }

    /**
     * Constructor.
     *
     * @param worldSpace True if the target position and rotation are in world space, false if they are local.
     * @param targetRotation Target rotation to smooth to.
     */
    public SmoothToComponent(boolean worldSpace, Quaternionf targetRotation) {
        this(worldSpace, null, targetRotation);
    }

    /**
     * Constructor.
     *
     * @param worldSpace True if the target position and rotation are in world space, false if they are local.
     * @param targetRotation Target rotation to smooth to.
     * @param rotationDamping Amount of rotational damping while smoothing.
     */
    public SmoothToComponent(boolean worldSpace, Quaternionf targetRotation, float rotationDamping) {
        this(worldSpace, targetRotation);
        this.rotationDamping = rotationDamping;
    }

    /**
     * Constructor.
     *
     * @param worldSpace True if the target position and rotation are in world space, false if they are local.
     * @param targetPosition Target position to smooth to.
     * @param targetRotation Target rotation to smooth to.
     */
    public SmoothToComponent(boolean worldSpace, Vector3f targetPosition, Quaternionf targetRotation) {
        // Set whether to use world space
        this.worldSpace = worldSpace;

        // Copy the target position and rotation if set
        if(targetPosition != null)
            this.targetPosition.set(targetPosition);
        if(targetRotation != null)
            this.targetRotation.set(targetRotation);

        // Set whether to use the target position and rotation
        this.useTargetPosition = targetPosition != null;
        this.useTargetRotation = targetRotation != null;
    }

    /**
     * Constructor.
     *
     * @param worldSpace True if the target position and rotation are in world space, false if they are local.
     * @param targetPosition Target position to smooth to.
     * @param targetRotation Target rotation to smooth to.
     * @param positionDamping Amount of positional damping while smoothing.
     * @param rotationDamping Amount of rotational damping while smoothing.
     */
    public SmoothToComponent(boolean worldSpace, Vector3f targetPosition, Quaternionf targetRotation, float positionDamping, float rotationDamping) {
        // Set the world space mode
        this.worldSpace = worldSpace;

        // Copy the target position and rotation if set
        if(targetPosition != null)
            this.targetPosition.set(targetPosition);
        if(targetRotation != null)
            this.targetRotation.set(targetRotation);

        // Set whether to use the target position and rotation
        this.useTargetPosition = targetPosition != null;
        this.useTargetRotation = targetRotation != null;

        // Set the positional and rotational damping
        this.positionDamping = positionDamping;
        this.rotationDamping = rotationDamping;
    }

    @Override
    public void create() {}

    @Override
    public void start() {
        // Call the super
        super.start();

        // Move the camera to it's initial position
        smoothUpdate(1f, 1f);
    }

    @Override
    public void update() {
        // Update the position
        smoothUpdate();
    }

    /**
     * Smoothly update the position with the default parameters.
     */
    public void smoothUpdate() {
        smoothUpdate(this.positionDamping * Time.deltaTimeFloat, this.rotationDamping * Time.deltaTimeFloat);
    }

    /**
     * Smoothly update the position with the given position and rotation lerp factor.
     *
     * @param positionFactor Positional lerp factor.
     * @param rotationFactor Rotational lerp factor.
     */
    public void smoothUpdate(float positionFactor, float rotationFactor) {
        // TODO: Implement usage of world / local space!

        // Lerp to the target position with the specified position damping
        if(this.useTargetPosition)
            getTransform().getPosition().lerp(targetPosition, positionFactor);

        // Lerp to the target rotation with the specified rotation damping
        if(this.useTargetRotation)
            getTransform().getRotation().nlerp(targetRotation, rotationFactor);
    }

    /**
     * Set the target to match the given transform position and rotation.
     * If a null transform object is given, the target position and rotation are reset.
     *
     * @param transform Transform to use as target, or null.
     */
    public void setTarget(Transform transform) {
        // Make sure the transform object isn't null
        if(transform == null) {
            resetTargetPosition();
            resetTargetRotation();
            return;
        }

        // Enable world space targets
        this.worldSpace = true;

        // Set the target position and rotation
        transform.getWorldPosition(this.targetPosition);
        transform.getWorldRotation(this.targetRotation);
        this.useTargetPosition = true;
        this.useTargetRotation = true;
    }

    /**
     * Set the target to match the given transform position and rotation.
     *
     * @param gameObject Transform to use as target.
     */
    public void setTarget(AbstractGameObject gameObject) {
        setTarget(gameObject.getTransform());
    }

    /**
     * Get the target position.
     *
     * @return Target position.
     */
    public Vector3f getTargetPosition() {
        return targetPosition;
    }

    /**
     * Get the target position, and write it in the destination vector.
     *
     * @param dest Destination vector.
     *
     * @return Target position.
     */
    public Vector3f getTargetPosition(Vector3f dest) {
        return dest.set(this.targetPosition);
    }

    /**
     * Set the target position.
     *
     * @param targetPosition Target position.
     */
    public void setTargetPosition(Vector3f targetPosition) {
        // Set the target position
        if(targetPosition != null)
            this.targetPosition.set(targetPosition);

        // Set whether to use it
        this.useTargetPosition = targetPosition != null;
    }

    /**
     * Reset the target position.
     * This will disable any target position that is currently specified.
     */
    public void resetTargetPosition() {
        setTargetPosition(null);
    }

    /**
     * Check whether the target position is used.
     *
     * @return True if used, false if not.
     */
    public boolean isTargetPositionUsed() {
        return this.useTargetPosition;
    }

    /**
     * Get the target rotation.
     *
     * @return Target rotation.
     */
    public Quaternionf getTargetRotation() {
        return this.targetRotation;
    }

    /**
     * Get the target rotation, and write it in the destination parameter.
     *
     * @param dest Destination parameter.
     *
     * @return Target rotation.
     */
    public Quaternionf getTargetRotation(Quaternionf dest) {
        return dest.set(this.targetRotation);
    }

    /**
     * Set the target rotation.
     *
     * @param targetRotation Target rotation, or null to ignore rotation.
     */
    public void setTargetRotation(Quaternionf targetRotation) {
        // Set the target rotation
        if(targetRotation != null)
            this.targetRotation.set(targetRotation);

        // Set whether to use
        this.useTargetRotation = targetRotation != null;
    }

    /**
     * Reset the target rotation.
     * This will disable any target rotation that is currently specified.
     */
    public void resetTargetRotation() {
        setTargetRotation(null);
    }

    /**
     * Check whether the target rotation is used.
     *
     * @return True if used, false if not.
     */
    public boolean isTargetRotationUsed() {
        return this.useTargetRotation;
    }

    /**
     * Get the positional lerp damping factor.
     *
     * @return Positional lerp damping factor.
     */
    public float getPositionDamping() {
        return this.positionDamping;
    }

    /**
     * Set the positional lerp damping factor.
     *
     * @param positionDamping Positional lerp damping factor.
     */
    public void setPositionDamping(float positionDamping) {
        this.positionDamping = positionDamping;
    }

    /**
     * Get the rotational lerp damping factor.
     *
     * @return Rotational lerp damping factor.
     */
    public float getRotationDamping() {
        return this.rotationDamping;
    }

    /**
     * Set the rotational lerp damping factor.
     *
     * @param rotationDamping Rotational lerp damping factor.
     */
    public void setRotationDamping(float rotationDamping) {
        this.rotationDamping = rotationDamping;
    }
}
