package com.prooph_board.axon.helper;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.nio.file.Files;
import java.nio.file.Path;

public class AggregateHelper {

    private AggregateHelper() {
        //private initializer to prevent instantiation
    }

    private static final String AGGREGATE_PACKAGE = "command";

    public static void create(
            String packageName,
            String aggregateName
    ) {
        final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
        javaClass.setPackage(packageName + "." + AGGREGATE_PACKAGE).setName(aggregateName);
        javaClass.addField()
                 .setName("serialVersionUID")
                 .setType("long")
                 .setLiteralInitializer("1L")
                 .setPrivate()
                 .setStatic(true)
                 .setFinal(true);

        javaClass.addProperty(Integer.class, "id").setMutable(false);
        javaClass.addProperty(String.class, "firstName");
        javaClass.addProperty("String", "lastName");

        javaClass.addMethod()
                 .setConstructor(true)
                 .setPublic()
                 .setBody("this.id = id;")
                 .addParameter(Integer.class, "id");
        write(javaClass);
    }

    private static void write(JavaClassSource javaClass) {
        try {
            Files.createDirectories(Path.of("./src/main/java/" + javaClass.getPackage().replace('.', '/')));
            Files.writeString(Path.of("./src/main/java/"  + javaClass.getCanonicalName().replace('.', '/') + ".java"), javaClass.toString()); // UTF
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
