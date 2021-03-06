// Generated by hibernate/SpringHibernateDaoImpl.vsl in andromda-spring-cartridge on 05/06/2020 19:06:14+0200.
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package bw.ub.ehealth.dhislink.redacap.data;

import bw.ub.ehealth.dhislink.redacap.data.vo.RedcapDataSearchCriteria;
import bw.ub.ehealth.dhislink.redacap.data.vo.RedcapDataVO;
import java.util.Collection;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

/**
 * @see RedcapData
 */
@Repository("redcapDataDao")
public class RedcapDataDaoImpl
    extends RedcapDataDaoBase
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<RedcapData> handleFindByCriteria(RedcapDataSearchCriteria searchCriteria)
    {
    	String queryStr = "select rd from bw.ub.ehealth.dhislink.redacap.data.RedcapData rd";
    	
    	StringBuilder where = new StringBuilder();
    	
    	if(searchCriteria.getEventId() != null) {
    		    		
    		where.append(" where eventId = :eventId");
    	}
    	
    	if(searchCriteria.getValue() != null) {
    		
    		if(where.length() == 0) {
    			where.append(" where ");
    		} else {
    			where.append(" and ");
    		}
    		
    		where.append("value = :value");
    	}
    	
    	if(searchCriteria.getFieldName() != null) {
    		
    		if(where.length() == 0) {
    			where.append(" where ");
    		} else {
    			where.append(" and ");
    		}
    		
    		if(searchCriteria.getFieldName().endsWith("%") || searchCriteria.getFieldName().startsWith("%")) {
    			where.append("fieldName like :fieldName");
    		} else {
    		
    			where.append("fieldName = :fieldName");
    		}
    	}
    	
    	if(searchCriteria.getRecord() != null) {
    		
    		if(where.length() == 0) {
    			where.append(" where ");
    		} else {
    			where.append(" and ");
    		}
    		
    		where.append("record = :record");
    	}
    	
    	if(searchCriteria.getProjectId() != null) {
    		
    		if(where.length() == 0) {
    			where.append(" where ");
    		} else {
    			where.append(" and ");
    		}
    		
    		where.append("projectId = :projectId");
    	}
    	
    	Query query = entityManager.createQuery(queryStr + where.toString());
    	
    	if(searchCriteria.getEventId() != null) {
    		
    		query.setParameter("eventId", searchCriteria.getEventId());
    	}
    	
    	if(searchCriteria.getValue() != null) {

    		query.setParameter("value", searchCriteria.getValue());
    	}
    	
    	if(searchCriteria.getFieldName() != null) {
    		
    		query.setParameter("fieldName", searchCriteria.getFieldName());
    	}
    	
    	if(searchCriteria.getRecord() != null) {
    		
    		query.setParameter("record", searchCriteria.getRecord());
    	}
    	
    	if(searchCriteria.getProjectId() != null) {
    		
    		query.setParameter("projectId", searchCriteria.getProjectId());
    	}
    	
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toRedcapDataVO(
        RedcapData source,
        RedcapDataVO target)
    {
        // TODO verify behavior of toRedcapDataVO
        super.toRedcapDataVO(source, target);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RedcapDataVO toRedcapDataVO(final RedcapData entity)
    {
        // TODO verify behavior of toRedcapDataVO
        return super.toRedcapDataVO(entity);
    }

    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private RedcapData loadRedcapDataFromRedcapDataVO(RedcapDataVO redcapDataVO)
    {
    	
    	RedcapDataSearchCriteria search = new RedcapDataSearchCriteria();
    	
    	search.setEventId(redcapDataVO.getEventId());
    	search.setFieldName(redcapDataVO.getFieldName());
    	search.setProjectId(redcapDataVO.getProjectId());
    	search.setRecord(redcapDataVO.getRecord());
    	search.setValue(redcapDataVO.getValue());
    	
    	List<RedcapData> r = (List<RedcapData>) findByCriteria(search);
    	
    	if(r == null || r.size() == 0) {
    		return RedcapData.Factory.newInstance();
    	} else {
    		return r.get(0);
    	}
    }

    /**
     * {@inheritDoc}
     */
    public RedcapData redcapDataVOToEntity(RedcapDataVO redcapDataVO)
    {
        // TODO verify behavior of redcapDataVOToEntity
        RedcapData entity = this.loadRedcapDataFromRedcapDataVO(redcapDataVO);
        this.redcapDataVOToEntity(redcapDataVO, entity, true);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void redcapDataVOToEntity(
        RedcapDataVO source,
        RedcapData target,
        boolean copyIfNull)
    {
        // TODO verify behavior of redcapDataVOToEntity
        super.redcapDataVOToEntity(source, target, copyIfNull);
    }

	/*
	 * @Override public Collection<RedcapData> findByFieldAndValue(String fieldName,
	 * String value) {
	 * 
	 * RedcapDataSearchCriteria criteria = new RedcapDataSearchCriteria();
	 * 
	 * criteria.setFieldName(fieldName); criteria.setValue(value);
	 * 
	 * return this.findByCriteria(criteria); }
	 */

	@Override
	protected Long handleFindMaxEvent(Long projectId) throws Exception {
		String queryStr = "select max(rd.eventId) from "
				+ "RedcapData rd where rd.projectId = :projectId";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("projectId", projectId);
		try {
			return (Long) query.getSingleResult();
		} catch(NoResultException | NonUniqueResultException e) {
			return null;
		}
	}

	@Override
	protected Collection<RedcapData> handleFindByFieldAndValue(String fieldName, String value) throws Exception {
		RedcapDataSearchCriteria criteria = new RedcapDataSearchCriteria();
		
		criteria.setFieldName(fieldName);
		criteria.setValue(value);
		
		return this.findByCriteria(criteria);
	}
}