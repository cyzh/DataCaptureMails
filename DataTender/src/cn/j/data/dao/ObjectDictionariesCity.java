package cn.j.data.dao;

/**
 * ObjectDictionariesCity entity. @author MyEclipse Persistence Tools
 */

public class ObjectDictionariesCity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8182926916345633495L;
	private Integer objectDictionariesId;
	private Integer objectDictionariesCityId;
	private String objectDictionariesCityName;

	// Constructors

	/** default constructor */
	public ObjectDictionariesCity() {
	}

	/** full constructor */
	public ObjectDictionariesCity(Integer objectDictionariesCityId,
			String objectDictionariesCityName) {
		this.objectDictionariesCityId = objectDictionariesCityId;
		this.objectDictionariesCityName = objectDictionariesCityName;
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

	public String getObjectDictionariesCityName() {
		return this.objectDictionariesCityName;
	}

	public void setObjectDictionariesCityName(String objectDictionariesCityName) {
		this.objectDictionariesCityName = objectDictionariesCityName;
	}

}