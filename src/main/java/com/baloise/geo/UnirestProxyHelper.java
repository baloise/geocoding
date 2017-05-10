package com.baloise.geo;

import static java.lang.String.format;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;

import com.mashape.unirest.http.Unirest;

public class UnirestProxyHelper {

	static void initProxy(String spec) {
		try {
			Unirest.setTimeouts( 5000, 30000);
			URL url = new URL(spec);
			HttpClientBuilder clientBuilder = HttpClientBuilder.create();
			int port = url.getPort() > 0 ? url.getPort() : url.getDefaultPort();
			clientBuilder.setProxy(new HttpHost(url.getHost(), port));

			if (url.getUserInfo() != null) {
				String[] usrPwd = url.getUserInfo().split(":", 2);
				String pwd = usrPwd.length == 2 ? usrPwd[1] : "";
				String pwdStars = usrPwd.length == 2 ? ":*****" : "";
				System.out.println(format("Setting proxy %s://%s%s@%s:%s", url.getProtocol(), usrPwd[0], pwdStars, url.getHost(), port));
				CredentialsProvider credsProvider = new BasicCredentialsProvider();
				credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(usrPwd[0], pwd));
				clientBuilder.useSystemProperties();
				clientBuilder.setDefaultCredentialsProvider(credsProvider);
				clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
				Lookup<AuthSchemeProvider> authProviders = RegistryBuilder.<AuthSchemeProvider>create()
						.register(AuthSchemes.BASIC, new BasicSchemeFactory()).build();
				clientBuilder.setDefaultAuthSchemeRegistry(authProviders);
			} else {
				System.out.println(format("Setting proxy %s://%s:%s", url.getProtocol(), url.getHost(), port));
			}
			Unirest.setHttpClient(clientBuilder.build());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private static boolean done;

	public static void initProxy() {
		if (done)
			return;
		done = true;
		String http_proxy = System.getProperty("http_proxy");
		if (http_proxy == null)
			http_proxy = System.getenv("http_proxy");
		if (http_proxy != null)
			initProxy(http_proxy);
	}

}
