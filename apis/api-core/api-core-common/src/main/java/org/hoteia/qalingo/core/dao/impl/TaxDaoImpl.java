/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hoteia.qalingo.core.dao.TaxDao;
import org.hoteia.qalingo.core.domain.Tax;
import org.hoteia.qalingo.core.fetchplan.FetchPlan;
import org.hoteia.qalingo.core.fetchplan.common.FetchPlanGraphCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository("taxDao")
public class TaxDaoImpl extends AbstractGenericDaoImpl implements TaxDao {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public Tax getTaxById(final Long taxId, Object... params) {
        Criteria criteria = createDefaultCriteria(Tax.class);
        
        FetchPlan fetchPlan = handleSpecificFetchMode(criteria, params);
        
        criteria.add(Restrictions.eq("id", taxId));
        Tax tax = (Tax) criteria.uniqueResult();
        tax.setFetchPlan(fetchPlan);
        return tax;
	}

	public Tax saveOrUpdateTax(final Tax tax) {
        if (tax.getId() != null) {
            if(em.contains(tax)){
                em.refresh(tax);
            }
            Tax mergedTax = em.merge(tax);
            em.flush();
            return mergedTax;
        } else {
            em.persist(tax);
            return tax;
        }
	}

	public void deleteTax(final Tax tax) {
		em.remove(tax);
	}

    @Override
    protected FetchPlan handleSpecificFetchMode(Criteria criteria, Object... params) {
        if (params != null && params.length > 0) {
            return super.handleSpecificFetchMode(criteria, params);
        } else {
            return super.handleSpecificFetchMode(criteria, FetchPlanGraphCommon.getDefaultTaxFetchPlan());
        }
    }
    
}