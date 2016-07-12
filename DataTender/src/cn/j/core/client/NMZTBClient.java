package cn.j.core.client;

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

public class NMZTBClient extends AbstractClient {
	private CloseableHttpClient httpClient = null;

	public NMZTBClient() {
		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.DEFAULT).setRedirectsEnabled(false)
				.setConnectTimeout(10000).setSocketTimeout(10000).build();
		httpClient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig).build();
	}

	@Override
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
			Elements elements = document.getElementsByClass("tabss");
			if (elements != null && !elements.isEmpty()) {
				Element element = elements.get(0);
				String content = element.text();
				datacapture.setContent(content);

				int index = content.indexOf("招标代理机构");
				if (index > -1) {
					String companyText = content.substring(index);
					index = companyText.indexOf("公司") + 2;
					if (index > -1 && index < 50) {
						companyText = companyText.substring(0, index);
						companyText = companyText.split("：")[1].replace("\r",
								"").replace(" ", "");
						if (companyText != null && !companyText.isEmpty()
								&& companyText.length() > 3) {
							datacapture.setTenderee(companyText);
							saveCompany(companyText);
						}
					}
				}
			}
		}
		return datacapture;
	}

}
