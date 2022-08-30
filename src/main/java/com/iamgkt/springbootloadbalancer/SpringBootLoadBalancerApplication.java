package com.iamgkt.springbootloadbalancer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringBootLoadBalancerApplication {

	@Autowired
	Environment env;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootLoadBalancerApplication.class, args);
	}

	@GetMapping("/host-details")
	public Map<String, String> getPort() throws UnknownHostException {

		Map<String, String> response = new LinkedHashMap<>();

		final String port = env.getProperty("local.server.port");
		final String host = InetAddress.getLoopbackAddress().getHostName();
		final String hostAddress = InetAddress.getLoopbackAddress().getHostAddress();
		final String currentIp = InetAddress.getLocalHost().getHostAddress();
		response.put("Host", host);
		response.put("Host Address", hostAddress);
		response.put("Current Ip", currentIp);
		response.put("Port", port);
		return response;
	}

}
