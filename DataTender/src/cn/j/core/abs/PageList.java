package cn.j.core.abs;

import java.util.List;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.select.Elements;

import cn.j.core.entity.DataInfo;

public abstract class PageList {

	protected static final Logger LOG = Logger.getLogger(PageList.class);
	protected boolean isOK = false;// 判断是否获得列表完毕
	protected int exceptCount = 0;// 如果同一页面异常连续出现5次则继续向下执行，如果连续出现15次异常则直接种植循环

	/**
	 * 执行请求
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract List<DataInfo> getPageList() throws Exception;

	/**
	 * 创建请求客户端
	 * 
	 * @param listUrl
	 * @return
	 * @throws Exception
	 */
	protected List<DataInfo> httpClient(String listUrl) throws Exception {
		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.DEFAULT).setRedirectsEnabled(false)
				.setConnectTimeout(10000).setSocketTimeout(10000).build();

		CloseableHttpClient client = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig).build();

		HttpGet httpGet = new HttpGet(listUrl);
		CloseableHttpResponse response = client.execute(httpGet);
		return httpResponse(response);

	}

	/**
	 * 接收响应
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected List<DataInfo> httpResponse(CloseableHttpResponse response)
			throws Exception {
		String pageHtml = EntityUtils.toString(response.getEntity());
		response.close();
		return httpList(pageHtml);

	}

	/**
	 * 获得list
	 * 
	 * @param pageHtml
	 * @return
	 */
	protected abstract List<DataInfo> httpList(String pageHtml);

	/**
	 * 解析list
	 * 
	 * @param list
	 * @return
	 */
	protected abstract List<DataInfo> parseList(Elements list);

}
