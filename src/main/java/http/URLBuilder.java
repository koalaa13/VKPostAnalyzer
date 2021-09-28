package http;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.utils.URIBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public class URLBuilder {
    public String buildHttpUrl(String host,
                               Integer port,
                               String path,
                               List<Pair<String, String>> parameters) {
        return buildUrl("http",
                host,
                port,
                path,
                parameters);
    }

    public String buildHttpsUrl(String host,
                                Integer port,
                                String path,
                                List<Pair<String, String>> parameters) {
        return buildUrl("https",
                host,
                port,
                path,
                parameters);
    }

    private String buildUrl(String scheme,
                            String host,
                            Integer port,
                            String path,
                            List<Pair<String, String>> parameters) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme(scheme);
        builder.setHost(host);
        if (port != null) {
            builder.setPort(port);
        }
        if (path != null) {
            builder.setPath(path);
        }
        if (parameters != null) {
            for (var param : parameters) {
                builder.setParameter(param.getLeft(), param.getRight());
            }
        }
        try {
            return builder.build().toURL().toString();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException("Can't build an URL", e);
        }
    }
}
