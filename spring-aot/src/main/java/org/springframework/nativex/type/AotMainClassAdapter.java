package org.springframework.nativex.type;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AotMainClassAdapter extends ClassVisitor {

	private static final String fieldName = "aotSpringApplication";

	private TypeSystem ts;
	private boolean scanning = true;
	private Type type;
	private boolean isFieldPresent;

	public static void run(TypeSystem ts, InputStream inputStream) {
		try {
			AotMainClassAdapter node = new AotMainClassAdapter(ts, Opcodes.ASM9);
			ClassReader reader = new ClassReader(inputStream);
			reader.accept(node, ClassReader.SKIP_DEBUG);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private AotMainClassAdapter(TypeSystem ts, int api) {
		super(api, new ClassWriter(0));
		this.ts = ts;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		type = ts.resolveSlashed(name);
		if (!type.isAtSpringBootApplication()) {
			scanning = false;
		}
		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		if (scanning && worthVisiting(access) && name.equals("main")) {
			return new SpringBootMainMethodVisitor(Opcodes.ASM9);
		} else {
			return super.visitMethod(access, name, descriptor, signature, exceptions);
		}
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		if (scanning && name.equals(fieldName)) {
			isFieldPresent = true;
		}
		return cv.visitField(access, name, desc, signature, value);
	}
	@Override
	public void visitEnd() {
		if (!isFieldPresent) {
			FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "aotSpringApplication", "Lorg/springframework/boot/SpringApplication;", null, null);;
			if (fv != null) {
				fv.visitEnd();
			}
		} else {
			throw new IllegalStateException("The original class already contains a " + fieldName + " field");
		}
		cv.visitEnd();
	}

	private boolean worthVisiting(int access) {
		return (access & Opcodes.ACC_STATIC) != 0 && (access & Opcodes.ACC_PUBLIC) != 0;
	}

	class SpringBootMainMethodVisitor extends MethodVisitor {

		public SpringBootMainMethodVisitor(int api) {
			super(api);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean itface) {
			if (owner.equals("org/springframework/boot/SpringApplication")) {
				System.out.println(owner + " | " + name + " | " + descriptor);
			}
		}
	}

}