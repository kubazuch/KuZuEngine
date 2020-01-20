package com.kuzu.engine.rendering;

import com.kuzu.engine.components.camera.Camera;
import com.kuzu.engine.components.light.BaseLight;
import com.kuzu.engine.core.GameObject;
import com.kuzu.engine.rendering.resources.MappedValues;
import com.kuzu.engine.rendering.shader.BasicShader;
import com.kuzu.engine.rendering.shader.Shader;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine extends MappedValues {
	private final Window window;

	private HashMap<String, Integer> samplerMap;
	private ArrayList<BaseLight> lights;
	private BaseLight activeLight;

	private Shader shader;
	private Camera mainCamera;

	public RenderingEngine(Window window) {
		super();
		this.window = window;
		lights = new ArrayList<>();

		samplerMap = new HashMap<>();
		samplerMap.put("diffuse", 0);
		samplerMap.put("normalMap", 1);
		samplerMap.put("dispMap", 2);
		samplerMap.put("shadowMap", 3);

		addVector3f("ambient", new Vector3f(0.1f, 0.1f, 0.1f));

		shader = new BasicShader();

		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

		glFrontFace(GL_CCW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);

		glEnable(GL_DEPTH_CLAMP);

		glEnable(GL_TEXTURE_2D);
	}

	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}

	public void render(GameObject object) {
		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		if (mainCamera == null) System.err.println("Main camera not found.");
		object.renderAll(shader, this, mainCamera);

//		glEnable(GL_BLEND);
//		glBlendFunc(GL_ONE, GL_ONE);
//		glDepthMask(false);
//		glDepthFunc(GL_EQUAL);
//
//		for(BaseLight light : lights) {
//			activeLight = light;
//			object.renderAll(light.getShader(), this);
//		}
//
//		glDepthFunc(GL_LESS);
//		glDepthMask(true);
//		glDisable(GL_BLEND);
	}

	public void addLight(BaseLight light) {
		lights.add(light);
	}

	public int getSamplerSlot(String samplerName) {
		return samplerMap.get(samplerName);
	}

	public BaseLight getActiveLight() {
		return activeLight;
	}

	public Camera getMainCamera() {
		return mainCamera;
	}

	public void setMainCamera(Camera mainCamera) {
		this.mainCamera = mainCamera;
	}

	public void onWindowResize() {
		shader.bind();
		shader.setProjectionMatrix(mainCamera.getProjection());
		shader.unbind();
	}
}