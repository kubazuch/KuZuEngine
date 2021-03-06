package com.kuzu.engine.rendering.shader;

import com.kuzu.engine.components.camera.Camera;
import com.kuzu.engine.core.Transform;
import com.kuzu.engine.rendering.Material;
import com.kuzu.engine.rendering.RenderingEngine;

public class BasicShader extends Shader {
	public BasicShader() {
		super("basic", false);
	}

	@Override
	public void updateUniforms(Transform transform, Material material, RenderingEngine engine, Camera camera) {
		material.getTexture("diffuse").bind(0);
		setUniform("transformMat", transform.getTransformation());
		setUniform("viewMat", engine.getMainCamera().getView());
		setUniform("projectionMat", camera.getProjection());
	}
}
