package extension;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

public class HostReachableBeforeAllCallback implements BeforeAllCallback {
    private static final int TIMEOUT = 1_000;

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    public @interface HostReachable {
        String value();
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!AnnotationSupport.isAnnotated(context.getRequiredTestClass(), HostReachable.class)) {
            throw new Exception("User @HostReachable annotation");
        }
        String host = context.getRequiredTestClass().getAnnotation(HostReachable.class).value();
        if (host == null) {
            throw new Exception("User @HostReachable annotation with not null host name");
        }
        Assumptions.assumeTrue(checkHost(host), "Skipped test because host is unreachable");
    }

    private static boolean checkHost(String host) {
        return nativePing(host) || nativePing6(host);
    }

    private static boolean nativePing(String host) {
        return nativePingImpl("ping", host);
    }

    private static boolean nativePing6(String host) {
        return nativePingImpl("ping6", host);
    }

    private static boolean nativePingImpl(String cmd, String host) {
        try {
            Process pingProcess
                    = new ProcessBuilder(cmd, "-c", "1", host).start();
            if (!pingProcess.waitFor(TIMEOUT, TimeUnit.MILLISECONDS)) {
                return false;
            }
            return pingProcess.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
