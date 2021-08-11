package org.springframework.context.bootstrap.generator.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.CodeBlock.Builder;

import org.springframework.aot.beans.factory.BeanDefinitionRegistrar.InstanceSupplierContext;
import org.springframework.util.ReflectionUtils;

/**
 * Write the necessary code to {@link #writeInstantiation(Executable) create a bean
 * instance} or {@link #writeInjection(Member, boolean) inject dependencies}.
 * <p/>
 * The writer expects a number of variables to be available and/or accessible:
 * <ul>
 *     <li>{@code context}: the general {@code GenericApplicationContext}</li>
 *     <li>{@code instanceContext}: the {@link InstanceSupplierContext} callback</li>
 *     <li>{@code bean}: the variable that refers to the bean instance</li>
 * </ul>
 *
 * @author Stephane Nicoll
 */
class InjectionPointWriter {

	CodeBlock writeInstantiation(Executable creator) {
		if (creator instanceof Constructor) {
			return write((Constructor<?>) creator);
		}
		if (creator instanceof Method) {
			return writeMethodInstantiation((Method) creator);
		}
		throw new IllegalArgumentException("Could not handle creator " + creator);
	}

	CodeBlock writeInjection(Member member, boolean required) {
		if (member instanceof Method) {
			return writeMethodInjection((Method) member, required);
		}
		if (member instanceof Field) {
			return writeFieldInjection((Field) member, required);
		}
		throw new IllegalArgumentException("Could not handle member " + member);
	}

	private CodeBlock write(Constructor<?> creator) {
		CodeBlock.Builder code = CodeBlock.builder();
		Class<?> declaringType = creator.getDeclaringClass();
		boolean innerClass = isInnerClass(declaringType);
		Class<?>[] parameterTypes = Arrays.stream(creator.getParameters()).map(Parameter::getType).toArray(Class<?>[]::new);
		// Shortcut for common case
		if (!innerClass && parameterTypes.length == 0) {
			code.add("new $T()", declaringType);
			return code.build();
		}
		code.add("instanceContext.constructor(");
		code.add(Arrays.stream(parameterTypes).map((d) -> "$T.class").collect(Collectors.joining(", ")), (Object[]) parameterTypes);
		code.add(")\n").indent().indent();
		code.add(".create(context, (attributes) ->");
		List<CodeBlock> parameters = resolveParameters(creator.getParameters());
		if (innerClass) { // Remove the implicit argument
			parameters.remove(0);
		}

		code.add(" ");
		if (innerClass) {
			code.add("context.getBean($T.class).new $L(", declaringType.getEnclosingClass(), declaringType.getSimpleName());
		}
		else {
			code.add("new $T(", declaringType);
		}
		for (int i = 0; i < parameters.size(); i++) {
			code.add(parameters.get(i));
			if (i < parameters.size() - 1) {
				code.add(", ");
			}
		}
		code.add(")");
		code.add(")").unindent().unindent(); // end of invoke
		return code.build();
	}

	private static boolean isInnerClass(Class<?> type) {
		return type.isMemberClass() && !Modifier.isStatic(type.getModifiers());
	}

	private CodeBlock writeMethodInstantiation(Method injectionPoint) {
		if (injectionPoint.getParameterCount() == 0) {
			Builder code = CodeBlock.builder();
			Class<?> declaringType = injectionPoint.getDeclaringClass();
			if (Modifier.isStatic(injectionPoint.getModifiers())) {
				code.add("$T", declaringType);
			}
			else {
				code.add("context.getBean($T.class)", declaringType);
			}
			code.add(".$L()", injectionPoint.getName());
			return code.build();
		}
		return write(injectionPoint, (code) -> code.add(".create(context, (attributes) ->"), true);
	}

	private CodeBlock writeMethodInjection(Method injectionPoint, boolean required) {
		Consumer<Builder> attributesResolver = (code) -> {
			if (required) {
				code.add(".invoke(context, (attributes) ->");
			}
			else {
				code.add(".resolve(context, false).ifResolved((attributes) ->");
			}
		};
		return write(injectionPoint, attributesResolver, false);
	}

	private CodeBlock write(Method injectionPoint, Consumer<Builder> attributesResolver, boolean instantiation) {
		CodeBlock.Builder code = CodeBlock.builder();
		code.add("instanceContext.method(");
		if (instantiation) {
			code.add("$T.class, ", injectionPoint.getDeclaringClass());
		}
		code.add("$S, ", injectionPoint.getName());
		Class<?>[] parameterTypes = Arrays.stream(injectionPoint.getParameters()).map(Parameter::getType).toArray(Class<?>[]::new);
		code.add(Arrays.stream(parameterTypes).map((d) -> "$T.class").collect(Collectors.joining(", ")), (Object[]) parameterTypes);
		code.add(")\n").indent().indent();
		attributesResolver.accept(code);
		List<CodeBlock> parameters = resolveParameters(injectionPoint.getParameters());
		code.add(" ");
		if (instantiation) {
			if (Modifier.isStatic(injectionPoint.getModifiers())) {
				code.add("$T", injectionPoint.getDeclaringClass());
			}
			else {
				code.add("context.getBean($T.class)", injectionPoint.getDeclaringClass());
			}
		}
		else {
			code.add("bean");
		}
		code.add(".$L(", injectionPoint.getName());
		for (int i = 0; i < parameters.size(); i++) {
			code.add(parameters.get(i));
			if (i < parameters.size() - 1) {
				code.add(", ");
			}
		}
		code.add(")");
		code.add(")").unindent().unindent(); // end of invoke
		return code.build();
	}

	CodeBlock writeFieldInjection(Field injectionPoint, boolean required) {
		CodeBlock.Builder code = CodeBlock.builder();
		code.add("instanceContext.field($S, $T.class", injectionPoint.getName(), injectionPoint.getType());
		code.add(")\n").indent().indent();
		if (required) {
			code.add(".invoke(context, (attributes) ->");
		}
		else {
			code.add(".resolve(context, false).ifResolved((attributes) ->");
		}
		boolean hasAssignment = Modifier.isPrivate(injectionPoint.getModifiers());
		if (hasAssignment) {
			code.beginControlFlow("");
			String fieldName = String.format("%sField", injectionPoint.getName());
			code.addStatement("$T $L = $T.findField($T.class, $S, $T.class)", Field.class, fieldName, ReflectionUtils.class,
					injectionPoint.getDeclaringClass(), injectionPoint.getName(), injectionPoint.getType());
			code.addStatement("$T.makeAccessible($L)", ReflectionUtils.class, fieldName);
			code.addStatement("$T.setField($L, bean, attributes.get(0))", ReflectionUtils.class, fieldName);
			code.unindent().add("}");
		}
		else {
			code.add(" bean.$L = attributes.get(0)", injectionPoint.getName());
		}
		code.add(")");
		return code.build();
	}

	private List<CodeBlock> resolveParameters(Parameter[] parameters) {
		List<CodeBlock> parameterValues = new ArrayList<>();
		for (int i = 0; i < parameters.length; i++) {
			parameterValues.add(CodeBlock.of("attributes.get($L)", i));
		}
		return parameterValues;
	}

}
