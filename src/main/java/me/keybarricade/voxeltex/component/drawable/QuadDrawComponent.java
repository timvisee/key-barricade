package me.keybarricade.voxeltex.component.drawable;

import me.keybarricade.voxeltex.shader.Shader;
import me.keybarricade.voxeltex.texture.Texture;
import me.keybarricade.voxeltex.math.vector.Vector2fFactory;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class QuadDrawComponent extends AbstractDrawableComponent {

    /**
     * The size of the cube on all three axis.
     */
    private Vector2f size = Vector2fFactory.one();

    /**
     * The center of the cube.
     */
    private Vector2f center = Vector2fFactory.zero();

    @Override
    public void start() { }

    @Override
    public void update() { }

    @Override
    public void draw() {
        // Calculate half of the sizes
        float xHalf = size.x / 2f;
        float yHalf = size.y / 2f;

        // Enter quad drawing mode
        glBegin(GL_QUADS);

        // Draw the quad
        glColor3f(0.0f, 1.0f, 0.0f );
        glTexCoord2f(0, 0);
        glVertex3f(center.x + xHalf, 0, center.y + yHalf);
        glTexCoord2f(1, 0);
        glVertex3f(center.x + xHalf, 0, center.y - yHalf);
        glTexCoord2f(1, 1);
        glVertex3f(center.x - xHalf, 0, center.y - yHalf);
        glTexCoord2f(0, 1);
        glVertex3f(center.x - xHalf, 0, center.y + yHalf);

        // Finish drawing
        glEnd();

        Shader.unbind();
        Texture.unbind();
    }

    /**
     * Get the size of the quad.
     *
     * @return Quad size.
     */
    public Vector2f getSize() {
        return size;
    }

    /**
     * Set the size of the quad.
     *
     * @param size Quad size.
     */
    public void setSize(Vector2f size) {
        this.size = size;
    }

    /**
     * Get the center of the quad.
     *
     * @return Center.
     */
    public Vector2f getCenter() {
        return center;
    }

    /**
     * Set the center of the quad.
     *
     * @param center Center.
     */
    public void setCenter(Vector2f center) {
        this.center = center;
    }
}