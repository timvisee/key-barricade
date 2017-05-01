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

public class SmoothFollowComponent extends BaseComponent {

    /**
     * Target game object to follow.
     */
    private AbstractGameObject target = null;

    /**
     * True to follow the target's position.
     */
    private boolean followPosition = true;

    /**
     * True to follow the target's rotation.
     */
    private boolean followRotation = true;

    /**
     * Component that is used to smoothly move to the target.
     */
    private SmoothToComponent smoothComponent = new SmoothToComponent();

    /**
     * Constructor.
     */
    public SmoothFollowComponent() {}

    /**
     * Constructor.
     *
     * @param target Target game object.
     */
    public SmoothFollowComponent(AbstractGameObject target) {
        setTarget(target);
    }

    /**
     * Constructor.
     *
     * @param target         Target game object.
     * @param followPosition True to follow the position of the target object.
     * @param followRotation True to follow the rotation of the target object.
     */
    public SmoothFollowComponent(AbstractGameObject target, boolean followPosition, boolean followRotation) {
        this(target);

        // Set whether to follow the position and/or rotation
        this.followPosition = followPosition;
        this.followRotation = followRotation;
    }

    @Override
    public void create() {
        // Add the smoothing component to the owner
        getOwner().addComponent(this.smoothComponent);
    }

    @Override
    public void start() {
        // Call the super
        super.start();

        // Update the target position immediately
        update();
    }

    @Override
    public void update() {
        // Make sure there's a target game object
        if (this.target == null)
            return;

        // Set the target position and rotation
        if (this.followPosition)
            this.target.getTransform().getWorldPosition(this.smoothComponent.getTargetPosition());
        if (this.followRotation)
            this.target.getTransform().getWorldRotation(this.smoothComponent.getTargetRotation());
    }

    /**
     * Get the target game object.
     *
     * @return Target game object.
     */
    public AbstractGameObject getTarget() {
        return target;
    }

    /**
     * Set the target game object.
     *
     * @param target Target game object.
     */
    public void setTarget(AbstractGameObject target) {
        this.target = target;
    }

    /**
     * Reset the target.
     */
    public void resetTarget() {
        this.target = null;
    }

    /**
     * Check whether there's any target to follow.
     *
     * @return True if there's any target to follow, false if not.
     */
    public boolean hasTarget() {
        return target != null;
    }

    /**
     * Check whether the target's position is followed.
     *
     * @return True if followed, false if not.
     */
    public boolean isFollowPosition() {
        return followPosition;
    }

    /**
     * Set whether to follow the target's position.
     *
     * @param followPosition True to follow, false if not.
     */
    public void setFollowPosition(boolean followPosition) {
        this.followPosition = followPosition;
    }

    /**
     * Check whether the target's rotation is followed.
     *
     * @return True if followed, false if not.
     */
    public boolean isFollowRotation() {
        return followRotation;
    }

    /**
     * Set whether to follow the target's rotation.
     *
     * @param followRotation True to follow, false if not.
     */
    public void setFollowRotation(boolean followRotation) {
        this.followRotation = followRotation;
    }

    /**
     * Get the component that is used for smoothing.
     *
     * @return Smoothing component.
     */
    public SmoothToComponent getSmoothComponent() {
        return smoothComponent;
    }

    /**
     * Get the positional lerp damping factor.
     *
     * @return Positional lerp damping factor.
     */
    public float getPositionDamping() {
        return getSmoothComponent().getPositionDamping();
    }

    /**
     * Set the positional lerp damping factor.
     *
     * @param positionDamping Positional lerp damping factor.
     */
    public void setPositionDamping(float positionDamping) {
        getSmoothComponent().setPositionDamping(positionDamping);
    }

    /**
     * Get the rotational lerp damping factor.
     *
     * @return Rotational lerp damping factor.
     */
    public float getRotationDamping() {
        return getSmoothComponent().getRotationDamping();
    }

    /**
     * Set the rotational lerp damping factor.
     *
     * @param rotationDamping Rotational lerp damping factor.
     */
    public void setRotationDamping(float rotationDamping) {
        getSmoothComponent().setRotationDamping(rotationDamping);
    }
}
