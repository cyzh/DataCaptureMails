package cn.j.data.dao;

/**
 * ObjectCompanyFilter entity. @author MyEclipse Persistence Tools
 */

public class ObjectCompanyFilter implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4696892605854186082L;
	private Integer id;
	private String filterWords;

	// Constructors

	/** default constructor */
	public ObjectCompanyFilter() {
	}

	/** full constructor */
	public ObjectCompanyFilter(String filterWords) {
		this.filterWords = filterWords;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFilterWords() {
		return this.filterWords;
	}

	public void setFilterWords(String filterWords) {
		this.filterWords = filterWords;
	}

}