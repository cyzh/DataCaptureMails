package cn.j.data.dao;

/**
 * ObjectCompany entity. @author MyEclipse Persistence Tools
 */

public class ObjectCompany implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1336052771556015958L;
	private Integer objectCompanyId;
	private String objectCompanyName;

	// Constructors

	/** default constructor */
	public ObjectCompany() {
	}

	/** full constructor */
	public ObjectCompany(String objectCompanyName) {
		this.objectCompanyName = objectCompanyName;
	}

	// Property accessors

	public Integer getObjectCompanyId() {
		return this.objectCompanyId;
	}

	public void setObjectCompanyId(Integer objectCompanyId) {
		this.objectCompanyId = objectCompanyId;
	}

	public String getObjectCompanyName() {
		return this.objectCompanyName;
	}

	public void setObjectCompanyName(String objectCompanyName) {
		this.objectCompanyName = objectCompanyName;
	}

}