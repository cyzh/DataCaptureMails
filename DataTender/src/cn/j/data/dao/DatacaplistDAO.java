package cn.j.data.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * Datacaplist entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see cn.j.data.dao.Datacaplist
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class DatacaplistDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(DatacaplistDAO.class);
	// property constants
	public static final String SOURCE = "source";
	public static final String TITLE = "title";
	public static final String LINK = "link";
	public static final String PROVINCE = "province";
	public static final String PROVINCEID = "provinceid";
	public static final String CITY = "city";
	public static final String CITYID = "cityid";
	public static final String AREA = "area";
	public static final String AREAID = "areaid";
	public static final String COMPANY = "company";
	public static final String COMPANYID = "companyid";
	public static final String INDUSTRY = "industry";
	public static final String ISDOWNLOAD = "isdownload";

	public void save(Datacaplist transientInstance) {
		log.debug("saving Datacaplist instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Datacaplist persistentInstance) {
		log.debug("deleting Datacaplist instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Datacaplist findById(java.lang.Integer id) {
		log.debug("getting Datacaplist instance with id: " + id);
		try {
			Datacaplist instance = (Datacaplist) getSession().get(
					"cn.j.data.dao.Datacaplist", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Datacaplist instance) {
		log.debug("finding Datacaplist instance by example");
		try {
			List results = getSession()
					.createCriteria("cn.j.data.dao.Datacaplist")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Datacaplist instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Datacaplist as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySource(Object source) {
		return findByProperty(SOURCE, source);
	}

	public List findByTitle(Object title) {
		return findByProperty(TITLE, title);
	}

	public List findByLink(Object link) {
		return findByProperty(LINK, link);
	}

	public List findByProvince(Object province) {
		return findByProperty(PROVINCE, province);
	}

	public List findByProvinceid(Object provinceid) {
		return findByProperty(PROVINCEID, provinceid);
	}

	public List findByCity(Object city) {
		return findByProperty(CITY, city);
	}

	public List findByCityid(Object cityid) {
		return findByProperty(CITYID, cityid);
	}

	public List findByArea(Object area) {
		return findByProperty(AREA, area);
	}

	public List findByAreaid(Object areaid) {
		return findByProperty(AREAID, areaid);
	}

	public List findByCompany(Object company) {
		return findByProperty(COMPANY, company);
	}

	public List findByCompanyid(Object companyid) {
		return findByProperty(COMPANYID, companyid);
	}

	public List findByIndustry(Object industry) {
		return findByProperty(INDUSTRY, industry);
	}

	public List findByIsdownload(Object isdownload) {
		return findByProperty(ISDOWNLOAD, isdownload);
	}

	public List findAll() {
		log.debug("finding all Datacaplist instances");
		try {
			String queryString = "from Datacaplist";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Datacaplist merge(Datacaplist detachedInstance) {
		log.debug("merging Datacaplist instance");
		try {
			Datacaplist result = (Datacaplist) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Datacaplist instance) {
		log.debug("attaching dirty Datacaplist instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Datacaplist instance) {
		log.debug("attaching clean Datacaplist instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}