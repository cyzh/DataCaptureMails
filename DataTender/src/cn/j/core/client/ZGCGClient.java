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

public class ZGCGClient extends AbstractClient {

	private String loginUrl = "http://www.chinabidding.com.cn/cblcn/member.login/logincheck?name=nmzb&password=NMGZBGS!@%23-2015&url=http://www.chinabidding.com.cn/cgxx/zfcg/?pr=1";
	private CloseableHttpClient httpClient = null;
	private Map<String, String> headerParams = null;

	public ZGCGClient() {
		headerParams = new HashMap<String, String>();
		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.DEFAULT).setRedirectsEnabled(false)
				.setConnectTimeout(10000).setSocketTimeout(10000).build();
		httpClient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig).build();
		try {
			login();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void login() throws Exception {
		HttpGet httpGet = new HttpGet(loginUrl);
		CloseableHttpResponse response = httpClient.execute(httpGet);
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

	public String doExecute(String location) {
		String page = null;
		try {
			HttpGet httpGet = new HttpGet(location);
			CloseableHttpResponse response = httpClient.execute(httpGet);
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
			Elements elements = document.getElementsByClass("ll_nr1");
			if (elements != null && !elements.isEmpty()) {
				Element element = elements.get(0);
				String content = element.text();
				datacapture.setContent(content);

				Elements companyElement = element
						.getElementsByClass("nr_bt1_sf");
				if (companyElement.size() > 0) {
					String companyContent = companyElement.get(0).text();
					System.out.println(companyContent);
					int index = companyContent.indexOf("招标代理");
					if (index > -1) {
						String companyText = content.substring(index);
						index = companyText.indexOf(" ");
						if (index > -1 && index < 50) {
							companyText = companyText.substring(0, index);
							String[] companyTexts = companyText.split(":");
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
