package com.datacap.mail.data.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.datacap.mail.utils.JUtils;

public abstract class DataRequest {
	private static final Logger log = Logger.getLogger(DataRequest.class);
	protected String mSource; // 标记网页源
	protected String mUri; // uri根据实际的uri实时改变
	protected String mCharset; // 标记字符集
	protected boolean mIsOver = false; // 标记是否抓取完毕
	protected List<Map<String, Object>> mMailList = null; // 用于发送邮件的列表
	protected JUtils utils = null;

	public DataRequest() {
		utils = new JUtils();
	}

	/**
	 * 解析界面，并返回map列表，map包括title，link，date
	 * 
	 * @param document
	 * @return
	 */
	abstract protected Map<String, Object> parseHtml(Document document)
			throws Exception;

	/**
	 * 循环遍历页面，
	 */
	abstract protected void traversePage(CloseableHttpClient httpClient);

	/**
	 * get请求
	 * 
	 * @param httpClient
	 * @return 返回mMailList数量，-1表示为空
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected int doGet(CloseableHttpClient httpClient) {
		// 创建客户端
		HttpGet getMethod = new HttpGet(mUri);
		String html = null;
		try {
			html = GetHtml(httpClient, getMethod);
		} catch (Exception e) {
			log.debug("页面请求异常");
			mIsOver = true;
			return -1;
		}
		// 获得页面
		Document document = Jsoup.parse(html); // 获得document对象
		Map<String, Object> map = null;
		try {
			map = parseHtml(document);
		} catch (Exception e) {
			log.debug(mSource + "，页面解析异常");
			mIsOver = true;
		} // 获得获得的列表
		if (map == null || map.isEmpty()) {
			log.debug("map为null，跳过");
			return -1;
		}
		// 初始化提交列表
		if (mMailList == null) {
			log.debug("mMailList初始化");
			mMailList = new ArrayList<Map<String, Object>>();
		}
		mMailList.add(map); // 将解析到的内容加入列表
		return mMailList.size();
	}

	/**
	 * post请求
	 * 
	 * @param httpClient
	 * @param formDate
	 *            post提交form表单参数
	 * @return 返回mMailList数量，-1表示为空
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public int doPost(CloseableHttpClient httpClient,
			UrlEncodedFormEntity formDate) {

		HttpPost postMethod = new HttpPost(mUri);
		postMethod.setEntity(formDate);
		String html = null;
		try {
			html = PostHtml(httpClient, postMethod);
		} catch (Exception e) {
			log.debug("页面请求异常");
			mIsOver = true;
			return -1;
		}
		Document document = Jsoup.parse(html);
		Map<String, Object> map = null;
		try {
			map = parseHtml(document);
		} catch (Exception e) {
			log.debug(mSource + "，页面解析异常");
			mIsOver = true;
		}
		if (map == null || map.isEmpty()) {
			log.debug("map为null，跳过");
			return -1;
		}
		// 初始化提交列表
		if (mMailList == null) {
			log.debug("mMailList初始化");
			mMailList = new ArrayList<Map<String, Object>>();
		}
		mMailList.add(map); // 将解析到的内容加入列表
		return mMailList.size();
	}

	/**
	 * 开始解析界面内容
	 * 
	 * @param method
	 */
	protected void beginParse(String method) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		if ("get".equals(method) || "GET".equals(method)) {
			traversePage(httpclient);
		}
		if ("post".equals(method) || "POST".equals(method)) {
			traversePage(httpclient);
		}
		if (mMailList == null || mMailList.isEmpty()) {
			return;
		}
		log.debug("mMailList: " + mMailList.toString());
		// 发送邮件
		utils.sendMail(mSource, mMailList);
	}

	/**
	 * get的请求获得html页面
	 * 
	 * @param httpClient
	 * @param getMethod
	 * @param charset
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private String GetHtml(CloseableHttpClient httpClient, HttpGet getMethod)
			throws ClientProtocolException, IOException {
		CloseableHttpResponse response = httpClient.execute(getMethod);
		String html = EntityUtils.toString(response.getEntity(), mCharset);
		response.close();// 关闭响应
		return html;
	}

	/**
	 * post请求获得页面
	 * 
	 * @param httpClient
	 * @param postMethod
	 * @param charset
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private String PostHtml(CloseableHttpClient httpClient, HttpPost postMethod)
			throws ClientProtocolException, IOException {
		CloseableHttpResponse response = httpClient.execute(postMethod);
		String html = EntityUtils.toString(response.getEntity(), mCharset);
		response.close();// 关闭响应
		return html;
	}
}
