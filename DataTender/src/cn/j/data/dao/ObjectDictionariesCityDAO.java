package cn.j.data.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * ObjectDictionariesCity entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see cn.j.data.dao.ObjectDictionariesCity
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class ObjectDictionariesCityDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory
			.getLog(ObjectDictionariesCityDAO.class);
	// property constants
	public static final String OBJECT_DICTIONARIES_CITY_ID = "objectDictionariesCityId";
	public static final String OBJECT_DICTIONARIES_CITY_NAME = "objectDictionariesCityName";

	public void save(ObjectDictionariesCity transientInstance) {
		log.debug("saving ObjectDictionariesCity instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ObjectDictionariesCity persistentInstance) {
		log.debug("deleting ObjectDictionariesCity instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ObjectDictionariesCity findById(java.lang.Integer id) {
		log.debug("getting ObjectDictionariesCity instance with id: " + id);
		try {
			ObjectDictionariesCity instance = (ObjectDictionariesCity) getSession()
					.get("cn.j.data.dao.ObjectDictionariesCity", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ObjectDictionariesCity instance) {
		log.debug("finding ObjectDictionariesCity instance by example");
		try {
			List results = getSession()
					.createCriteria("cn.j.data.dao.ObjectDictionariesCity")
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
		log.debug("finding ObjectDictionariesCity instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ObjectDictionariesCity as model where model."
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

	public List findByObjectDictionariesCityName(
			Object objectDictionariesCityName) {
		return findByProperty(OBJECT_DICTIONARIES_CITY_NAME,
				objectDictionariesCityName);
	}

	public List findAll() {
		log.debug("finding all ObjectDictionariesCity instances");
		try {
			String queryString = "from ObjectDictionariesCity";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ObjectDictionariesCity merge(ObjectDictionariesCity detachedInstance) {
		log.debug("merging ObjectDictionariesCity instance");
		try {
			ObjectDictionariesCity result = (ObjectDictionariesCity) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ObjectDictionariesCity instance) {
		log.debug("attaching dirty ObjectDictionariesCity instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ObjectDictionariesCity instance) {
		log.debug("attaching clean ObjectDictionariesCity instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}