package cn.j.data.dao;

import java.util.Date;

/**
 * Datacapture2015 entity. @author MyEclipse Persistence Tools
 */

public class Datacapture implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3712597174896848695L;
	private Integer id;
	private String source;
	private String area;
	private String industry;
	private Date publishdate;
	private Date startdate;
	private Date enddate;
	private String tenderee;
	private String capital;
	private String competitor;
	private String pageurl;
	private String title;
	private Integer provinceId;
	private Integer cityId;
	private Integer areaId;
	private String content;
	private Integer isDownload;

	// Constructors

	/** default constructor */
	public Datacapture() {
	}

	/** minimal constructor */
	public Datacapture(Integer isDownload) {
		this.isDownload = isDownload;
	}

	/** full constructor */
	public Datacapture(String source, String area, String industry,
			Date publishdate, Date startdate, Date enddate, String tenderee,
			String capital, String competitor, String pageurl, String title,
			Integer provinceId, Integer cityId, Integer areaId, String content,
			Integer isDownload) {
		this.source = source;
		this.area = area;
		this.industry = industry;
		this.publishdate = publishdate;
		this.startdate = startdate;
		this.enddate = enddate;
		this.tenderee = tenderee;
		this.capital = capital;
		this.competitor = competitor;
		this.pageurl = pageurl;
		this.title = title;
		this.provinceId = provinceId;
		this.cityId = cityId;
		this.areaId = areaId;
		this.content = content;
		this.isDownload = isDownload;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getIndustry() {
		return this.industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public Date getPublishdate() {
		return this.publishdate;
	}

	public void setPublishdate(Date publishdate) {
		this.publishdate = publishdate;
	}

	public Date getStartdate() {
		return this.startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public String getTenderee() {
		return this.tenderee;
	}

	public void setTenderee(String tenderee) {
		this.tenderee = tenderee;
	}

	public String getCapital() {
		return this.capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getCompetitor() {
		return this.competitor;
	}

	public void setCompetitor(String competitor) {
		this.competitor = competitor;
	}

	public String getPageurl() {
		return this.pageurl;
	}

	public void setPageurl(String pageurl) {
		this.pageurl = pageurl;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getIsDownload() {
		return this.isDownload;
	}

	public void setIsDownload(Integer isDownload) {
		this.isDownload = isDownload;
	}

}