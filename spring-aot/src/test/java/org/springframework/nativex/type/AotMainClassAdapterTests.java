package org.springframework.nativex.type;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class AotMainClassAdapterTests {

	private TypeSystem ts;

	@BeforeEach
	public void setup() throws Exception {
		ts = new TypeSystem(Arrays.asList(new File("./target/classes").toString(), new File("./target/test-classes").toString()));
	}

	@Test
	public void test() {
		String name = DemoApplication.class.getName().replace(".", "/");
		byte[] bytes = ts.find(name);
		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
			AotMainClassAdapter.run(ts, bais);
		} catch (IOException e) {
			throw new IllegalStateException("Unexpected IOException processing bytes for " + name);
		}
	}

	@SpringBootApplication
	static class DemoApplication {

		public static SpringApplication aotSpringApplication;

		public static void main(String[] args) {
			SpringApplication.run(DemoApplication.class);
		}

	}

}
