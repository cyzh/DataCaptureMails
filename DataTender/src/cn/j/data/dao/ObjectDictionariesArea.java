package cn.j.data.dao;

/**
 * ObjectDictionariesArea entity. @author MyEclipse Persistence Tools
 */

public class ObjectDictionariesArea implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2889584551941147525L;
	private Integer objectDictionariesId;
	private Integer objectDictionariesCityId;
	private Integer objectDictionariesAreaId;
	private String objectDictionariesAreaName;

	// Constructors

	/** default constructor */
	public ObjectDictionariesArea() {
	}

	/** full constructor */
	public ObjectDictionariesArea(Integer objectDictionariesCityId,
			Integer objectDictionariesAreaId, String objectDictionariesAreaName) {
		this.objectDictionariesCityId = objectDictionariesCityId;
		this.objectDictionariesAreaId = objectDictionariesAreaId;
		this.objectDictionariesAreaName = objectDictionariesAreaName;
	}

	// Property accessors

	public Integer getObjectDictionariesId() {
		return this.objectDictionariesId;
	}

	public void setObjectDictionariesId(Integer objectDictionariesId) {
		this.objectDictionariesId = objectDictionariesId;
	}

	public Integer getObjectDictionariesCityId() {
		return this.objectDictionariesCityId;
	}

	public void setObjectDictionariesCityId(Integer objectDictionariesCityId) {
		this.objectDictionariesCityId = objectDictionariesCityId;
	}

	public Integer getObjectDictionariesAreaId() {
		return this.objectDictionariesAreaId;
	}

	public void setObjectDictionariesAreaId(Integer objectDictionariesAreaId) {
		this.objectDictionariesAreaId = objectDictionariesAreaId;
	}

	public String getObjectDictionariesAreaName() {
		return this.objectDictionariesAreaName;
	}

	public void setObjectDictionariesAreaName(String objectDictionariesAreaName) {
		this.objectDictionariesAreaName = objectDictionariesAreaName;
	}

}