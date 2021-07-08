package org.springframework.context.bootstrap.generator;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.bootstrap.infrastructure.BootstrapSpringApplication;

public class MainBootstrapGenerator {

	private static final Log logger = LogFactory.getLog(MainBootstrapGenerator.class);

	public JavaFile createClass(String packageName, String mainBootstrapClassName) {
		return JavaFile.builder(packageName, TypeSpec.classBuilder(mainBootstrapClassName).addModifiers(Modifier.PUBLIC)
				.addMethod(generateMainMethod()).build()).build();
	}

	private MethodSpec generateMainMethod() {
		MethodSpec.Builder method = MethodSpec.methodBuilder("main").addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.addParameter(String[].class, "args");
		ClassName contextBootstrapClassName = ClassName.bestGuess("org.springframework.aot.ContextBootstrap");
		method.addStatement("$T application = new $T(new $T()::bootstrap)", BootstrapSpringApplication.class, BootstrapSpringApplication.class,contextBootstrapClassName);
		method.addStatement("application.run(args)");
		return method.build();
	}
}
