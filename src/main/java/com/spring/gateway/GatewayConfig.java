package com.spring.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GatewayConfig {

	@Bean
	RouteLocator routes(RouteLocatorBuilder builder) {

		System.out.println("Routing the request : ");
		return builder.routes()
				.route("product", p -> p.path("/api/products")
						.filters(f -> f.rewritePath("api/(?<service>.*)", "/${service}")
								.circuitBreaker(c->c.setName("UserCircuitBreaker").setFallbackUri("forward:/fallback")))
						.uri("lb://PRODUCT-SERVICE"))
				.route("product", p -> p.path("/api/products/**")
						.filters(f -> f.rewritePath("api/(?<service>.*)/(?<remaining>.*)", "/${service}/${remaining}")
								.circuitBreaker(c->c.setName("UserCircuitBreaker").setFallbackUri("forward:/fallback")))
						.uri("lb://PRODUCT-SERVICE"))
				
				.route("order",
						p -> p.path("/api/orders")
								.filters(f -> f.rewritePath("api/(?<service>.*)", "/${service}"))
								.uri("lb://ORDER-SERVICE"))
				.build();
	}

//	@Bean
//    GlobalFilter customGlobalFilter() {
//		return (exchange,chain)->{
//			ServerHttpRequest request = exchange.getRequest();
//			String requestMethod = request.getMethod().name();
//			String requestPath = request.getPath().toString();
//			System.out.println("Incoming request " + requestMethod +
//					" " + requestPath);
//			long startTime = System.currentTimeMillis();
//			
//			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//				ServerHttpResponse response = exchange.getResponse();
//				HttpStatusCode responseStatus =
//				response.getStatusCode();
//				long duration = System.currentTimeMillis() -
//				startTime;
//				
//				System.out.println("Outgoing response with status code " + responseStatus + " processed in " + duration + " ms");
//				
//			}));
//					
//		};
//	}

}
