package cn.j.data.dao;

/**
 * ObjectTitleFilter entity. @author MyEclipse Persistence Tools
 */

public class ObjectTitleFilter implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1925845898852730969L;
	private Integer id;
	private String filterWords;

	// Constructors

	/** default constructor */
	public ObjectTitleFilter() {
	}

	/** full constructor */
	public ObjectTitleFilter(String filterWords) {
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