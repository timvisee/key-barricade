package me.keybarricade.demo;

import me.keybarricade.gameobject.SandSurfacePrefab;
import me.keybarricade.voxeltex.component.light.LightSourceComponent;
import me.keybarricade.voxeltex.light.Light;
import me.keybarricade.voxeltex.material.Material;
import me.keybarricade.voxeltex.prefab.camera.FpsCameraPrefab;
import me.keybarricade.voxeltex.prefab.light.LightPrefab;
import me.keybarricade.voxeltex.prefab.primitive.CubePrefab;
import me.keybarricade.voxeltex.scene.Scene;
import me.keybarricade.voxeltex.texture.Image;
import me.keybarricade.voxeltex.texture.Texture;
import me.keybarricade.voxeltex.util.Color;

public class DemoScene extends Scene {

    @Override
    public void load() {
        // Load the super
        super.load();

        // Load box textures
        Texture boxTexture = Texture.fromImage(Image.loadFromEngineAssets("images/box.png"));
        Material boxMaterial = new Material(boxTexture);

        // Add a light simulating the sun
        LightPrefab sunLight = new LightPrefab("Sun", Light.LIGHT_TYPE_DIRECTIONAL, new Color(0xFDDC5C).toVector3f(), 0.3f);
        sunLight.getTransform().getRotation().set(90, 45, 90).normalize();
        sunLight.getTransform().getPosition().set(-5, 1, -3);
        addGameObject(sunLight);

        // Create a surface
        addGameObject(new SandSurfacePrefab());


        // Demo code here:


        FpsCameraPrefab camera = new FpsCameraPrefab();
        camera.getTransform().setPosition(0, 2, 3);
        addGameObject(camera);

        for(int i = 0; i < 16; i++) {
            CubePrefab box = new CubePrefab();
            box.getTransform().setPosition(i, 0.5f, 0);
            box.setMaterial(boxMaterial);
            box.addComponent(new LightSourceComponent(Color.random()));
            addGameObject(box);
        }
    }
}