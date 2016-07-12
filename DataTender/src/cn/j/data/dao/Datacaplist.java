package cn.j.data.dao;

import java.util.Date;

/**
 * Datacaplist entity. @author MyEclipse Persistence Tools
 */

public class Datacaplist implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -8348006979712810812L;
	private Integer id;
	private String source;
	private String title;
	private String link;
	private String province;
	private Integer provinceid;
	private String city;
	private Integer cityid;
	private String area;
	private Integer areaid;
	private String company;
	private Integer companyid;
	private String industry;
	private Date ddate;
	private Integer isdownload;

	// Constructors

	/** default constructor */
	public Datacaplist() {
	}

	/** minimal constructor */
	public Datacaplist(String source, String title, String link,
			Integer provinceid, Integer cityid, Integer areaid,
			Integer companyid, Integer isdownload) {
		this.source = source;
		this.title = title;
		this.link = link;
		this.provinceid = provinceid;
		this.cityid = cityid;
		this.areaid = areaid;
		this.companyid = companyid;
		this.isdownload = isdownload;
	}

	/** full constructor */
	public Datacaplist(String source, String title, String link,
			String province, Integer provinceid, String city, Integer cityid,
			String area, Integer areaid, String company, Integer companyid,
			String industry, Date ddate, Integer isdownload) {
		this.source = source;
		this.title = title;
		this.link = link;
		this.province = province;
		this.provinceid = provinceid;
		this.city = city;
		this.cityid = cityid;
		this.area = area;
		this.areaid = areaid;
		this.company = company;
		this.companyid = companyid;
		this.industry = industry;
		this.ddate = ddate;
		this.isdownload = isdownload;
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

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Integer getProvinceid() {
		return this.provinceid;
	}

	public void setProvinceid(Integer provinceid) {
		this.provinceid = provinceid;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getCityid() {
		return this.cityid;
	}

	public void setCityid(Integer cityid) {
		this.cityid = cityid;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getAreaid() {
		return this.areaid;
	}

	public void setAreaid(Integer areaid) {
		this.areaid = areaid;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Integer getCompanyid() {
		return this.companyid;
	}

	public void setCompanyid(Integer companyid) {
		this.companyid = companyid;
	}

	public String getIndustry() {
		return this.industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public Date getDdate() {
		return this.ddate;
	}

	public void setDdate(Date ddate) {
		this.ddate = ddate;
	}

	public Integer getIsdownload() {
		return this.isdownload;
	}

	public void setIsdownload(Integer isdownload) {
		this.isdownload = isdownload;
	}

}