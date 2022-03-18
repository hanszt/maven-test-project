package org.hzt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.function.Supplier;

public final class CallBackJavascriptEquivalent {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallBackJavascriptEquivalent.class);

    private CallBackJavascriptEquivalent() {
    }

    private static final String JAVASCRIPT_EQUIVALENT = "function hello(firstName, lastName, callback) {\n" +
                                                        "    print(firstName);\n" +
                                                        "    var result = firstName;\n" +
                                                        "        if (lastName) {\n" +
                                                        "    result.concat(lastName);\n" +
                                                        "        } else {\n" +
                                                        "    result.concat(callback());\n" +
                                                        "    }\n" +
                                                        "    return result;\n" +
                                                        "}\n";

    @VisibleForTesting
    static String setupEngine(ThrowingFunction<ScriptEngine, String> function) throws Exception {
        final var version = System.getProperty("java.specification.version");
        final var allDigits = version.chars().allMatch(Character::isDigit);
        final var scriptEngineManager = new ScriptEngineManager();
        LOGGER.info("Java version: {}", version);
        if ((allDigits && Integer.parseInt(version) <= 11) || "1.8".equals(version)) {
            ScriptEngine engine = scriptEngineManager.getEngineByName("nashorn");
            return function.apply(engine);
        }
        LOGGER.error("nashorn not working at java version: {}", version);
        return "No working engine";
    }

    @FunctionalInterface
    public interface ThrowingFunction<T, R> {

        R apply(T t) throws Exception;
    }

    @NotNull
    static String runHelloScript(Supplier<String> supplier, ScriptEngine engine) throws ScriptException {
        Bindings bindings = engine.createBindings();
        bindings.put("firstName", "Piet");
        bindings.put("lastName", "Klaassen");
        bindings.put("callback", supplier);
        Object result = engine.eval(JAVASCRIPT_EQUIVALENT);
        LOGGER.info("result = {}", result);
        return "Hello script run";
    }

    @NotNull
    static String runHelloWorldScript(ScriptEngine engine) throws ScriptException {
        engine.eval("var greeting='hello world';" +
                    "print(greeting);" +
                    "greeting");
        return "Hello world script run";
    }

    @NotNull
    static String runScriptWithParams(ScriptEngine engine) throws ScriptException {
        Bindings bindings = engine.createBindings();
        bindings.put("count", 3);
        bindings.put("name", "baeldung");

        String script = "var greeting='Hello ';" +
                        "for(var i=count;i>0;i--) { " +
                        "greeting+=name + ' '" +
                        "}" +
                        "greeting";

        Object bindingsResult = engine.eval(script, bindings);
        LOGGER.info("Binding result: {}", bindingsResult);
        return (String) bindingsResult;
    }

    @VisibleForTesting
    static String hello(String firstName, String lastName, Supplier<String> messageSupplier) {
        StringBuilder sb = new StringBuilder();
        sb.append(firstName);
        if (lastName != null) {
            sb.append(" ").append(lastName);
        } else {
            sb.append(", ").append(messageSupplier.get());
        }
        return sb.toString();
    }
}
