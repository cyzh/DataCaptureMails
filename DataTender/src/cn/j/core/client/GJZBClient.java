package cn.j.core.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.j.core.abs.AbstractClient;
import cn.j.data.dao.Datacapture;

public class GJZBClient extends AbstractClient {

	private String loginUrl = "https://cas.ebnew.com/cas/login?username=dajinshiji&password=yy2020120&authorize=true&loginUrl=http%3A%2F%2Fwww.chinabidding.com%2Fbid%2Findex%2FloginIndex.htm&service=http%3A%2F%2Fwww.chinabidding.com%2Fbid%2Flogin%2Flogin.htm&realService=http%3A%2F%2F211.151.182.228%3A8091";
	private CloseableHttpClient httpClient = null;
	private Map<String, String> headerParams = null;

	public GJZBClient() {
		headerParams = new HashMap<String, String>();
		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.DEFAULT).setRedirectsEnabled(false)
				.setConnectTimeout(10000).setSocketTimeout(10000).build();
		httpClient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig).build();
	}

	public void login() throws Exception {
		String location = null;
		for (int i = 0; i < 5; i++) {
			CloseableHttpResponse response = null;
			if (i == 0) {
				HttpGet httpGet = new HttpGet(loginUrl);

				response = httpClient.execute(httpGet);
				Header[] locations = response.getHeaders("Location");
				location = locations[0].getValue();
				httpGet.abort();
			} else if (i == 1) {
				HttpGet httpGet = new HttpGet(location);

				response = httpClient.execute(httpGet);
				Header[] locations = response.getHeaders("Location");
				location = locations[0].getValue();
				httpGet.abort();
			} else if (i == 2) {
				HttpGet httpGet = new HttpGet(location);
				String cookie = "CASTGC=" + headerParams.get("CASTGC")
						+ ";_cookie_from_source="
						+ headerParams.get("_cookie_from_source");
				httpGet.setHeader("Cookie", cookie);

				response = httpClient.execute(httpGet);
				Header[] locations = response.getHeaders("Location");
				location = locations[0].getValue();
				httpGet.abort();
			} else if (i == 3) {
				HttpGet httpGet = new HttpGet(location);
				String cookie = "JSESSIONID=" + headerParams.get("JSESSIONID");
				httpGet.setHeader("Cookie", cookie);

				response = httpClient.execute(httpGet);
				Header[] locations = response.getHeaders("Location");
				location = locations[0].getValue();
				httpGet.abort();
			} else if (i == 4) {
				HttpGet httpGet = new HttpGet(location);
				String cookie = "JSESSIONID=" + headerParams.get("JSESSIONID");
				httpGet.setHeader("Cookie", cookie);

				response = httpClient.execute(httpGet);
				Header[] locations = response.getHeaders("Location");
				location = locations[0].getValue();
				httpGet.abort();
			}

			Header[] setCookie = response.getHeaders("Set-Cookie");
			if (setCookie != null) {
				for (int j = 0; j < setCookie.length; j++) {
					String cookie = setCookie[j].getValue().replace(" ", "");
					String[] values = cookie.split(";");
					for (int x = 0; x < values.length; x++) {
						String[] value = values[x].split("=");
						String k = value[0];
						if (value.length == 2) {
							String v = value[1];
							headerParams.put(k, v);
						} else {
							headerParams.put(k, "");
						}
					}
				}
			}
		}
	}

	public String doExecute(String location) {
		String page = null;
		try {
			HttpGet httpGet = new HttpGet(location);
			CloseableHttpResponse response = httpClient.execute(httpGet);

			Header[] headers = response.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				String name = headers[i].getName();
				String value = headers[i].getValue();
				System.out.println(name + ":" + value);
			}

			HttpEntity entity = response.getEntity();
			page = EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}

	@Override
	public Datacapture parsePage(String page, Datacapture datacapture) {
		if (page != null) {
			Document document = Jsoup.parse(page);
			Elements elements = document.getElementsByClass("text");
			if (elements != null && !elements.isEmpty()) {
				Element element = elements.get(0);
				String content = element.text();
				datacapture.setContent(content);

				int index = content.indexOf("采购代理机构名称");
				if (index <= -1) {
					index = content.indexOf("采购代理机构");
					if (index <= -1) {
						index = content.indexOf("采购代理");
						if (index <= -1) {
							index = content.indexOf("招标代理机构名称");
							if (index <= -1) {
								index = content.indexOf("招标代理机构");
								if (index <= -1) {
									index = content.indexOf("招标代理");
								}
							}
						}
					}
				}

				if (index >= 1) {
					String companyText = content.substring(index);
					index = companyText.indexOf("中心");
					if (index > -1 && index < 50) {
						companyText = companyText.substring(0, index + 2);
						String[] companyTexts = companyText.split("：");
						if (companyTexts.length == 2) {
							companyText = companyTexts[1].replace(" ", "");
							datacapture.setTenderee(companyText);
							saveCompany(companyText);
						}
					} else {
						index = companyText.indexOf("公司");
						if (index > -1 && index < 50) {
							companyText = companyText.substring(0, index + 2);
							String[] companyTexts = companyText.split("：");
							if (companyTexts.length == 2) {
								companyText = companyTexts[1].replace(" ", "");
								if (companyText != null
										&& !companyText.isEmpty()
										&& companyText.length() > 3) {
									datacapture.setTenderee(companyText);
									saveCompany(companyText);
								}
							}
						}
					}
				}
			}
		}
		return datacapture;
	}
}
