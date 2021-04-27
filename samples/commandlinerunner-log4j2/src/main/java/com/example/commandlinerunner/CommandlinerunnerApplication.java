package com.example.commandlinerunner;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.AccessBits;
import org.springframework.nativex.hint.InitializationHint;
import org.springframework.nativex.hint.InitializationTime;
import org.springframework.nativex.hint.MethodHint;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.ResourceHint;
import org.springframework.nativex.hint.TypeHint;

/**
 * TODO Should be a TypeProcessor or a NativeConfiguration that search factory annotations dynamically
 */
@TypeHint(types = org.apache.logging.log4j.core.config.PropertiesPlugin.class,
		methods = @MethodHint(name = "configureSubstitutor", parameterTypes = { Property[].class, Configuration.class }),
		access = AccessBits.DECLARED_METHODS)

@TypeHint(types = org.apache.logging.log4j.core.config.Property.class,
		methods = @MethodHint(name = "createProperty", parameterTypes = { String.class, String.class }),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(types = org.apache.logging.log4j.core.layout.PatternLayout.class,
		methods = @MethodHint(name = "newBuilder"),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(types = org.apache.logging.log4j.core.appender.ConsoleAppender.class,
		methods = @MethodHint(name = "newBuilder"),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(types = org.apache.logging.log4j.core.config.AppendersPlugin.class,
		methods = @MethodHint(name = "createAppenders", parameterTypes = Appender[].class),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(types = org.apache.logging.log4j.core.config.LoggerConfig.class,
		methods = @MethodHint(name = "createLogger", parameterTypes = {
				boolean.class,
				Level.class,
				String.class,
				String.class,
				AppenderRef[].class,
				Property[].class,
				Configuration.class,
				Filter.class
		}),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(types = org.apache.logging.log4j.core.config.AppenderRef.class,
		methods = @MethodHint(name = "createAppenderRef", parameterTypes = {
				String.class,
				Level.class,
				Filter.class
		}),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(typeNames = "org.apache.logging.log4j.core.config.LoggerConfig$RootLogger",
		methods = @MethodHint(name = "createLogger", parameterTypes = {
				String.class,
				Level.class,
				String.class,
				AppenderRef[].class,
				Property[].class,
				Configuration.class,
				Filter.class
		}),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(types = org.apache.logging.log4j.core.config.LoggersPlugin.class,
		methods = @MethodHint(name = "createLoggers", parameterTypes = LoggerConfig[].class),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(types = {
		org.apache.logging.log4j.core.config.plugins.visitors.PluginAttributeVisitor.class,
		org.apache.logging.log4j.core.config.plugins.validation.validators.RequiredValidator.class,
		org.apache.logging.log4j.core.config.plugins.visitors.PluginElementVisitor.class,
		org.apache.logging.log4j.core.config.plugins.visitors.PluginConfigurationVisitor.class,
		org.apache.logging.log4j.core.config.plugins.visitors.PluginValueVisitor.class,
		org.apache.logging.log4j.core.config.plugins.visitors.PluginBuilderAttributeVisitor.class,
		org.apache.logging.log4j.core.lookup.UpperLookup.class,
		org.apache.logging.log4j.core.lookup.SystemPropertiesLookup.class,
		org.apache.logging.log4j.core.lookup.StructuredDataLookup.class,
		org.apache.logging.log4j.core.lookup.MarkerLookup.class,
		org.apache.logging.log4j.core.lookup.MapLookup.class,
		org.apache.logging.log4j.core.lookup.MainMapLookup.class,
		org.apache.logging.log4j.core.lookup.LowerLookup.class,
		org.apache.logging.log4j.core.lookup.Log4jLookup.class,
		org.apache.logging.log4j.core.lookup.JavaLookup.class,
		org.apache.logging.log4j.core.lookup.EventLookup.class,
		org.apache.logging.log4j.core.lookup.EnvironmentLookup.class,
		org.apache.logging.log4j.core.lookup.DateLookup.class,
		org.apache.logging.log4j.core.lookup.ContextMapLookup.class,
		org.apache.logging.log4j.core.lookup.ResourceBundleLookup.class,
		org.apache.logging.log4j.core.lookup.JmxRuntimeInputArgumentsLookup.class,
		org.apache.logging.log4j.core.lookup.JndiLookup.class
}, typeNames = {
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$BooleanConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$BigDecimalConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$BigIntegerConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$ByteConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$ByteArrayConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$CharacterConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$CharArrayConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$CharsetConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$ClassConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$CronExpressionConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$DoubleConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$DurationConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$FileConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$FloatConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$InetAddressConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$IntegerConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$LevelConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$LongConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$PathConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$PatternConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$SecurityProviderConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$ShortConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$StringConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$UriConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$UrlConverter",
		"org.apache.logging.log4j.core.config.plugins.convert.TypeConverters$UuidConverter"
})
@TypeHint(types = org.apache.logging.log4j.core.pattern.DatePatternConverter.class,
		methods = @MethodHint(name = "newInstance", parameterTypes = { String[].class }),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(types = org.apache.logging.log4j.core.pattern.ThreadNamePatternConverter.class,
		methods = @MethodHint(name = "newInstance", parameterTypes = { String[].class }),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(types = org.apache.logging.log4j.core.pattern.LevelPatternConverter.class,
		methods = @MethodHint(name = "newInstance", parameterTypes = { String[].class }),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(types = org.apache.logging.log4j.core.pattern.LoggerPatternConverter.class,
		methods = @MethodHint(name = "newInstance", parameterTypes = { String[].class }),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(types = org.apache.logging.log4j.core.pattern.MessagePatternConverter.class,
		methods = @MethodHint(name = "newInstance", parameterTypes = { Configuration.class, String[].class }),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(types = org.apache.logging.log4j.core.pattern.LineSeparatorPatternConverter.class,
		methods = @MethodHint(name = "newInstance", parameterTypes = { String[].class }),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(types = org.apache.logging.log4j.core.pattern.ProcessIdPatternConverter.class,
		methods = @MethodHint(name = "newInstance", parameterTypes = { String[].class }),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(types = org.springframework.boot.logging.log4j2.ColorConverter.class,
		methods = @MethodHint(name = "newInstance", parameterTypes = { Configuration.class, String[].class }),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(typeNames = {
		"org.apache.logging.log4j.core.appender.AbstractAppender$Builder",
		"org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender$Builder",
		"org.apache.logging.log4j.core.appender.ConsoleAppender$Builder",
		"org.apache.logging.log4j.core.layout.PatternLayout$Builder",
		"org.apache.logging.log4j.core.layout.LevelPatternSelector$Builder"
}, access = AccessBits.DECLARED_FIELDS)


@TypeHint(types = org.springframework.boot.logging.log4j2.WhitespaceThrowablePatternConverter.class,
		methods = @MethodHint(name = "newInstance", parameterTypes = { Configuration.class, String[].class }),
		access = AccessBits.DECLARED_METHODS)
@TypeHint(types = org.springframework.boot.logging.log4j2.ExtendedWhitespaceThrowablePatternConverter.class,
		methods = @MethodHint(name = "newInstance", parameterTypes = { Configuration.class, String[].class }),
		access = AccessBits.DECLARED_METHODS)
@ResourceHint(patterns = { "log4j2.properties", "log4j2.yaml", "log4j2.yml", "log4j2.json", "log4j2.jsn", "log4j2.xml" })
@SpringBootApplication
public class CommandlinerunnerApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(CommandlinerunnerApplication.class, args);
		Thread.currentThread().join(); // To be able to measure memory consumption
	}
	
}
