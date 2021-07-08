package org.springframework.context.bootstrap.infrastructure;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ResourceLoader;

/**
 * An extension of {@link SpringApplication} that does not handle primary sources at
 * all.
 */
public class BootstrapSpringApplication extends SpringApplication {

	public BootstrapSpringApplication(Consumer<GenericApplicationContext> bootstrap) {
		super((ResourceLoader) null, Object.class);
		setInitializers(Arrays.asList((context) -> bootstrap.accept((GenericApplicationContext) context)));
	}

	@Override
	public void addPrimarySources(Collection<Class<?>> additionalPrimarySources) {
		throw new UnsupportedOperationException("Sources can't be set.");
	}

	@Override
	public void setSources(Set<String> sources) {
		throw new UnsupportedOperationException("Sources can't be set.");
	}

	@Override
	protected void load(ApplicationContext context, Object[] sources) {
		// this effectively ignore any source that was registered.
	}

	@Override
	public ConfigurableApplicationContext createApplicationContext() {
		return super.createApplicationContext();
	}

}
