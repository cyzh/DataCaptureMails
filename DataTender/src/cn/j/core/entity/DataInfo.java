package cn.j.core.entity;

import java.io.Serializable;

/**
 * 抓取列表内容
 * 
 * @author WenC
 * 
 */
public class DataInfo implements Serializable {

	private static final long serialVersionUID = 7435975764919337685L;

	private String source = null;
	private String title = null;
	private String industry = null;
	private String time = null;
	private String href = null;
	private String city = null;
	private int cityId = 0;
	private String area = null;
	private int areaId = 0;
	private String company = null;
	private int companyId = 0;
	private String fullTitle = null;

	public DataInfo() {
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getFullTitle() {
		return fullTitle;
	}

	public void setFullTitle(String fullTitle) {
		this.fullTitle = fullTitle;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return "DataInfo [source=" + source + ", title=" + title
				+ ", industry=" + industry + ", time=" + time + ", href="
				+ href + ", city=" + city + ", cityId=" + cityId + ", area="
				+ area + ", areaId=" + areaId + ", company=" + company
				+ ", companyId=" + companyId + ", fullTitle=" + fullTitle + "]";
	}

}
