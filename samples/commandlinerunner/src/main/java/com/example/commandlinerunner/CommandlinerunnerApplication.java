package com.example.commandlinerunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ClassUtils;

@SpringBootApplication
public class CommandlinerunnerApplication {

	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		if (ClassUtils.isPresent("org.springframework.aot.MainBootstrap", null)) {
			System.out.println("Bootstrap mode");
			Method method = ClassUtils.forName("org.springframework.aot.MainBootstrap", null).getMethod("main", String[].class);
			method.invoke(null, new Object[] {args});
		} else {
			System.out.println("Regular mode");
			SpringApplication.run(CommandlinerunnerApplication.class, args);
		}
		Thread.currentThread().join(); // To be able to measure memory consumption
	}
	
}
