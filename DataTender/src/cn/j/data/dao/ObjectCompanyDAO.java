package cn.j.data.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * ObjectCompany entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see cn.j.data.dao.ObjectCompany
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class ObjectCompanyDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(ObjectCompanyDAO.class);
	// property constants
	public static final String OBJECT_COMPANY_NAME = "objectCompanyName";

	public void save(ObjectCompany transientInstance) {
		log.debug("saving ObjectCompany instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ObjectCompany persistentInstance) {
		log.debug("deleting ObjectCompany instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ObjectCompany findById(java.lang.Integer id) {
		log.debug("getting ObjectCompany instance with id: " + id);
		try {
			ObjectCompany instance = (ObjectCompany) getSession().get(
					"cn.j.data.dao.ObjectCompany", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ObjectCompany instance) {
		log.debug("finding ObjectCompany instance by example");
		try {
			List results = getSession()
					.createCriteria("cn.j.data.dao.ObjectCompany")
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
		log.debug("finding ObjectCompany instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ObjectCompany as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByObjectCompanyName(Object objectCompanyName) {
		return findByProperty(OBJECT_COMPANY_NAME, objectCompanyName);
	}

	public List findAll() {
		log.debug("finding all ObjectCompany instances");
		try {
			String queryString = "from ObjectCompany";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ObjectCompany merge(ObjectCompany detachedInstance) {
		log.debug("merging ObjectCompany instance");
		try {
			ObjectCompany result = (ObjectCompany) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ObjectCompany instance) {
		log.debug("attaching dirty ObjectCompany instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ObjectCompany instance) {
		log.debug("attaching clean ObjectCompany instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}