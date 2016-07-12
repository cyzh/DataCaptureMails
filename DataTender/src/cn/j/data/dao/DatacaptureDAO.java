package cn.j.data.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * Datacapture2015 entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see cn.j.data.dao.Datacapture
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class DatacaptureDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(DatacaptureDAO.class);
	// property constants
	public static final String SOURCE = "source";
	public static final String AREA = "area";
	public static final String INDUSTRY = "industry";
	public static final String TENDEREE = "tenderee";
	public static final String CAPITAL = "capital";
	public static final String COMPETITOR = "competitor";
	public static final String PAGEURL = "pageurl";
	public static final String TITLE = "title";
	public static final String PROVINCE_ID = "provinceId";
	public static final String CITY_ID = "cityId";
	public static final String AREA_ID = "areaId";
	public static final String CONTENT = "content";
	public static final String IS_DOWNLOAD = "isDownload";

	public void save(Datacapture transientInstance) {
		log.debug("saving Datacapture2015 instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Datacapture persistentInstance) {
		log.debug("deleting Datacapture2015 instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Datacapture findById(java.lang.Integer id) {
		log.debug("getting Datacapture2015 instance with id: " + id);
		try {
			Datacapture instance = (Datacapture) getSession().get(
					"cn.j.data.dao.Datacapture2015", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Datacapture instance) {
		log.debug("finding Datacapture2015 instance by example");
		try {
			List results = getSession()
					.createCriteria("cn.j.data.dao.Datacapture2015")
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
		log.debug("finding Datacapture2015 instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Datacapture2015 as model where model."
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

	public List findByArea(Object area) {
		return findByProperty(AREA, area);
	}

	public List findByIndustry(Object industry) {
		return findByProperty(INDUSTRY, industry);
	}

	public List findByTenderee(Object tenderee) {
		return findByProperty(TENDEREE, tenderee);
	}

	public List findByCapital(Object capital) {
		return findByProperty(CAPITAL, capital);
	}

	public List findByCompetitor(Object competitor) {
		return findByProperty(COMPETITOR, competitor);
	}

	public List findByPageurl(Object pageurl) {
		return findByProperty(PAGEURL, pageurl);
	}

	public List findByTitle(Object title) {
		return findByProperty(TITLE, title);
	}

	public List findByProvinceId(Object provinceId) {
		return findByProperty(PROVINCE_ID, provinceId);
	}

	public List findByCityId(Object cityId) {
		return findByProperty(CITY_ID, cityId);
	}

	public List findByAreaId(Object areaId) {
		return findByProperty(AREA_ID, areaId);
	}

	public List findByContent(Object content) {
		return findByProperty(CONTENT, content);
	}

	public List findByIsDownload(Object isDownload) {
		return findByProperty(IS_DOWNLOAD, isDownload);
	}

	public List findAll() {
		log.debug("finding all Datacapture2015 instances");
		try {
			String queryString = "from Datacapture2015";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Datacapture merge(Datacapture detachedInstance) {
		log.debug("merging Datacapture2015 instance");
		try {
			Datacapture result = (Datacapture) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Datacapture instance) {
		log.debug("attaching dirty Datacapture2015 instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Datacapture instance) {
		log.debug("attaching clean Datacapture2015 instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}