package cn.j.data.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * ObjectTitleFilter entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see cn.j.data.dao.ObjectTitleFilter
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class ObjectTitleFilterDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory
			.getLog(ObjectTitleFilterDAO.class);
	// property constants
	public static final String FILTER_WORDS = "filterWords";

	public void save(ObjectTitleFilter transientInstance) {
		log.debug("saving ObjectTitleFilter instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ObjectTitleFilter persistentInstance) {
		log.debug("deleting ObjectTitleFilter instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ObjectTitleFilter findById(java.lang.Integer id) {
		log.debug("getting ObjectTitleFilter instance with id: " + id);
		try {
			ObjectTitleFilter instance = (ObjectTitleFilter) getSession().get(
					"cn.j.data.dao.ObjectTitleFilter", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ObjectTitleFilter instance) {
		log.debug("finding ObjectTitleFilter instance by example");
		try {
			List results = getSession()
					.createCriteria("cn.j.data.dao.ObjectTitleFilter")
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
		log.debug("finding ObjectTitleFilter instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ObjectTitleFilter as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByFilterWords(Object filterWords) {
		return findByProperty(FILTER_WORDS, filterWords);
	}

	public List findAll() {
		log.debug("finding all ObjectTitleFilter instances");
		try {
			String queryString = "from ObjectTitleFilter";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ObjectTitleFilter merge(ObjectTitleFilter detachedInstance) {
		log.debug("merging ObjectTitleFilter instance");
		try {
			ObjectTitleFilter result = (ObjectTitleFilter) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ObjectTitleFilter instance) {
		log.debug("attaching dirty ObjectTitleFilter instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ObjectTitleFilter instance) {
		log.debug("attaching clean ObjectTitleFilter instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}