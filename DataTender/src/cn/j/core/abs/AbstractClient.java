package cn.j.core.abs;

import java.math.BigInteger;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.j.data.dao.Datacapture;
import cn.j.data.dao.ObjectCompany;
import cn.j.data.session.HSession;

public abstract class AbstractClient {
	protected static final Logger LOG = Logger.getLogger(AbstractClient.class);

	public abstract String doExecute(String location);

	public abstract Datacapture parsePage(String page, Datacapture datacapture);

	private boolean companyIsExist(String company) {
		Session session = null;
		try {
			session = HSession.getSession();
			Query query = session
					.createSQLQuery("SELECT COUNT(*) FROM object_company WHERE object_company_name=?");
			query.setString(0, company);
			int size = ((BigInteger) query.uniqueResult()).intValue();
			if (size > 0) {
				LOG.debug("company is exist");
				return false;
			} else {
				LOG.debug("company is not exist");
				return true;
			}
		} catch (Exception e) {
			LOG.debug("saved company failed, error: " + e.getMessage());
		} finally {
			session.close();
			HSession.closeSession();
		}
		return false;
	}

	protected boolean saveCompany(String company) {
		boolean flag = false;
		if (!companyIsExist(company)) {
			Session session = null;
			Transaction t = null;
			try {
				ObjectCompany oc = new ObjectCompany();
				oc.setObjectCompanyName(company);
				session = HSession.getSession();
				t = session.beginTransaction();
				int companyId = (Integer) session.save(oc);
				t.commit();
				LOG.debug("saved company id is: " + companyId);
			} catch (Exception e) {
				t.rollback();
				LOG.debug("saved company failed, error: " + e.getMessage());
			} finally {
				session.close();
				HSession.closeSession();
			}
		}
		return flag;
	}
}
