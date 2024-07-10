package com.prooph_board.axon.helper;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.Method;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

public class AggregateHelper {

    private AggregateHelper() {
        //private initializer to prevent instantiation
    }

    private static final String AGGREGATE_PACKAGE = "command";
    private static final String COMMAND_PARAMETER = "command";
    private static final String EVENT_PARAMETER = "event";

    private static final String COMMAND_HANDLER_METHOD_NAME = "on";
    private static final String EVENT_HANDLER_METHOD_NAME = "handle";
    private static final String EVENT_COMMENT = "Optionally use the data from the event to change the state of the aggregate.";
    private static final String COMMAND_COMMENT = "Add additional validation, optionally using the build up state.";
    private static final String CONSTRUCTOR_BODY_FORMAT =
            asPrintStatement(COMMAND_COMMENT) + "apply(new %s(command.id()));";
    private static final String EVENT_HANDLER_BODY = "id = event.id();" + "\n" + asPrintStatement(EVENT_COMMENT);
    private static final String PRINTLN_FORMAT = "System.out.println(\"%s\");";

    public static void create(
            String packageName,
            String aggregateName,
            AggregatePair initial,
            Collection<AggregatePair> commands
    ) {
        final JavaClassSource javaClass = getOrCreateSource(packageName, aggregateName);

        if (javaClass.getMethods().stream().noneMatch(Method::isConstructor)) {
            MethodSource<JavaClassSource> constructor =
                    javaClass.addMethod()
                             .setConstructor(true)
                             .setPublic()
                             .setBody(String.format(CONSTRUCTOR_BODY_FORMAT, initial.event().getSimpleName()));
            constructor.addParameter(initial.command(), COMMAND_PARAMETER);
            constructor.addAnnotation(CommandHandler.class);
        }

        commands.forEach(
                pair -> {
                    if (!hasCommandHandler(javaClass, pair.command())){
                        MethodSource<JavaClassSource> commandHandler =
                                javaClass.addMethod()
                                         .setName(COMMAND_HANDLER_METHOD_NAME)
                                         .setPublic()
                                         .setBody(String.format(CONSTRUCTOR_BODY_FORMAT, pair.event().getSimpleName()));
                        commandHandler.addParameter(pair.command(), COMMAND_PARAMETER);
                        commandHandler.addAnnotation(CommandHandler.class);
                    }
                }
        );

        if (!hasEventHandler(javaClass, initial.event())){
            MethodSource<JavaClassSource> firstEventHandler =
                    javaClass.addMethod()
                             .setName(EVENT_HANDLER_METHOD_NAME)
                             .setProtected()
                             .setBody(EVENT_HANDLER_BODY);
            firstEventHandler.addParameter(initial.event(), EVENT_PARAMETER);
            firstEventHandler.addAnnotation(EventSourcingHandler.class);
        }

        commands.forEach(
                pair -> {
                    if(!hasEventHandler(javaClass, pair.event())){
                        MethodSource<JavaClassSource> eventHandler =
                                javaClass.addMethod()
                                         .setName(EVENT_HANDLER_METHOD_NAME)
                                         .setProtected()
                                         .setBody(asPrintStatement(EVENT_COMMENT));
                        eventHandler.addParameter(pair.event(), EVENT_PARAMETER);
                        eventHandler.addAnnotation(EventSourcingHandler.class);
                    }
                }
        );

        write(javaClass);
    }

    private static boolean hasCommandHandler(
            JavaClassSource javaClass, Class<?> command
    ) {
        return javaClass.getMethods().stream().anyMatch(
                it ->
                        it.getName().equals(COMMAND_HANDLER_METHOD_NAME) &&
                                it.getParameters().getFirst().getType().isType(command)
        );
    }

    private static boolean hasEventHandler(
            JavaClassSource javaClass, Class<?> event
    ) {
        return javaClass.getMethods().stream().anyMatch(
                it ->
                        it.getName().equals(EVENT_HANDLER_METHOD_NAME) &&
                                it.getParameters().getFirst().getType().isType(event)
        );
    }

    private static JavaClassSource getOrCreateSource(
            String packageName,
            String aggregateName
    ) {
        try {
            return (JavaClassSource) Roaster.parse(Files.newInputStream(Path.of(
                    "./src/main/java/" + packageName.replace('.', '/') + "/command/" + aggregateName + ".java")));
        } catch (Exception e) {
            System.out.println(e);
            JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
            javaClass.setPackage(packageName + "." + AGGREGATE_PACKAGE).setName(aggregateName);
            javaClass.addImport("org.axonframework.modelling.command.AggregateLifecycle.apply")
                     .setStatic(true);
            javaClass.addField()
                     .setName("id")
                     .setType("String")
                     .setPrivate()
                     .addAnnotation(AggregateIdentifier.class);
            return javaClass;
        }
    }

    private static void write(JavaClassSource javaClass) {
        try {
            Files.createDirectories(Path.of("./src/main/java/" + javaClass.getPackage().replace('.', '/')));
            Files.writeString(Path.of("./src/main/java/" + javaClass.getCanonicalName().replace('.', '/') + ".java"),
                              javaClass.toString()); // UTF
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static String asPrintStatement(String input) {
        return String.format(PRINTLN_FORMAT, input);
    }
}
