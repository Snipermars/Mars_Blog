package com.liupeidong.spring.gateway.config;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentationConfig.class);
    private final RouteLocator routeLocator;

    public DocumentationConfig(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList<>();
        List<Route> routes = routeLocator.getRoutes();
        Collections.sort(routes, new Comparator<Route>() {
            @Override
            public int compare(Route r1, Route r2) {
                return r1.getId().compareTo(r2.getId());
            }
        });
        routes.forEach(route -> {
            if (!(StringUtils.contains(route.getId(), "crland-gateway")
                    || StringUtils.contains(route.getId(), "oauth-server")
                    || StringUtils.contains(route.getId(), "hap-config-server"))) {
                resources.add(swaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs")));
            }
        });
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("1.0");
        return swaggerResource;
    }
}
