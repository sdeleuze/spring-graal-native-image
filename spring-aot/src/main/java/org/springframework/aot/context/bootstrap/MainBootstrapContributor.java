package org.springframework.aot.context.bootstrap;

import com.squareup.javapoet.JavaFile;

import org.springframework.aot.BootstrapContributor;
import org.springframework.aot.BuildContext;
import org.springframework.aot.SourceFiles;
import org.springframework.context.bootstrap.generator.ContextBootstrapGenerator;
import org.springframework.context.bootstrap.generator.MainBootstrapGenerator;
import org.springframework.nativex.AotOptions;

public class MainBootstrapContributor implements BootstrapContributor {

	@Override
	public void contribute(BuildContext context, AotOptions aotOptions) {
		MainBootstrapGenerator mainGenerator = new MainBootstrapGenerator();
		JavaFile javaFile = mainGenerator.createClass("org.springframework.aot", "MainBootstrap");
		context.addSourceFiles(SourceFiles.fromJavaFile(javaFile));
	}

}
