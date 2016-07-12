package cn.j.data.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * ObjectDictionariesArea entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see cn.j.data.dao.ObjectDictionariesArea
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class ObjectDictionariesAreaDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory
			.getLog(ObjectDictionariesAreaDAO.class);
	// property constants
	public static final String OBJECT_DICTIONARIES_CITY_ID = "objectDictionariesCityId";
	public static final String OBJECT_DICTIONARIES_AREA_ID = "objectDictionariesAreaId";
	public static final String OBJECT_DICTIONARIES_AREA_NAME = "objectDictionariesAreaName";

	public void save(ObjectDictionariesArea transientInstance) {
		log.debug("saving ObjectDictionariesArea instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ObjectDictionariesArea persistentInstance) {
		log.debug("deleting ObjectDictionariesArea instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ObjectDictionariesArea findById(java.lang.Integer id) {
		log.debug("getting ObjectDictionariesArea instance with id: " + id);
		try {
			ObjectDictionariesArea instance = (ObjectDictionariesArea) getSession()
					.get("cn.j.data.dao.ObjectDictionariesArea", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ObjectDictionariesArea instance) {
		log.debug("finding ObjectDictionariesArea instance by example");
		try {
			List results = getSession()
					.createCriteria("cn.j.data.dao.ObjectDictionariesArea")
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
		log.debug("finding ObjectDictionariesArea instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ObjectDictionariesArea as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByObjectDictionariesCityId(Object objectDictionariesCityId) {
		return findByProperty(OBJECT_DICTIONARIES_CITY_ID,
				objectDictionariesCityId);
	}

	public List findByObjectDictionariesAreaId(Object objectDictionariesAreaId) {
		return findByProperty(OBJECT_DICTIONARIES_AREA_ID,
				objectDictionariesAreaId);
	}

	public List findByObjectDictionariesAreaName(
			Object objectDictionariesAreaName) {
		return findByProperty(OBJECT_DICTIONARIES_AREA_NAME,
				objectDictionariesAreaName);
	}

	public List findAll() {
		log.debug("finding all ObjectDictionariesArea instances");
		try {
			String queryString = "from ObjectDictionariesArea";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ObjectDictionariesArea merge(ObjectDictionariesArea detachedInstance) {
		log.debug("merging ObjectDictionariesArea instance");
		try {
			ObjectDictionariesArea result = (ObjectDictionariesArea) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ObjectDictionariesArea instance) {
		log.debug("attaching dirty ObjectDictionariesArea instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ObjectDictionariesArea instance) {
		log.debug("attaching clean ObjectDictionariesArea instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}