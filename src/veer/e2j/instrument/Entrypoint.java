package veer.e2j.instrument;

import veer.e2j.collect.ClassCollector;
import veer.e2j.collect.Constraint;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Entrypoint {

    public static void premain(String options, Instrumentation instr) {
        Path output;
        try {
            output = (options == null || options.isEmpty())
                    ? Files.createTempFile(pwd(), "e2j-", ".dump.jar") : Paths.get(options);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        ClassCollector collector = new ClassCollector(output, new Constraint() {

            private ClassLoader target;

            public boolean accept(ClassLoader loader, String name) {
                 return true;
            }
        });
        collector.prepare();
        collector.attach(instr);
    }

    private static Path pwd() {
        return Paths.get(".");
    }
}
