// Generated by hibernate/SpringHibernateDaoImpl.vsl in andromda-spring-cartridge on 05/01/2020 16:24:30+0200.
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package bw.ub.ehealth.dhislink.patient;

import bw.ub.ehealth.dhislink.patient.vo.PatientVO;
import bw.ub.ehealth.dhislink.specimen.Specimen;
import bw.ub.ehealth.dhislink.specimen.vo.SpecimenVO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

/**
 * @see Patient
 */
@Repository("patientDao")
public class PatientDaoImpl
    extends PatientDaoBase
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected Patient handleGetPatientById(Long id)
    {
        
        return this.load(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Patient handleGetPatientByIdentityNo(String identityNo)
    {

    	CriteriaBuilder builder = getSession().getCriteriaBuilder();
    	CriteriaQuery<Patient> query = builder.createQuery(Patient.class);
    	Root<Patient> root = query.from(Patient.class);   
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		predicates.add(builder.like(root.<String>get("identityNo"), identityNo));
				
		if(!predicates.isEmpty()) {
			query.where();
	        Predicate[] pr = new Predicate[predicates.size()];
	        predicates.toArray(pr);
	        query.where(pr); 
		}
		
		TypedQuery<Patient> typedQuery = getSession().createQuery(query);
		List<Patient> patients = typedQuery.getResultList();
		
		if(patients == null || patients.size() == 0) {
			return null;
		}
				
		return patients.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toPatientVO(
        Patient source,
        PatientVO target)
    {
        // TODO verify behavior of toPatientVO
        super.toPatientVO(source, target);
        // WARNING! No conversion for target.specimen (can't convert source.getSpecimen():bw.ub.ehealth.dhislink.specimen.Specimen to bw.ub.ehealth.dhislink.specimen.vo.SpecimenVO
        
        //Collection<SpecimenVO> sp = new ArrayList<>();
        
        /*for(Specimen s : source.getSpecimen()) {
        	SpecimenVO tmp = new SpecimenVO();
        	tmp.setPatient(target);
        	tmp.setBatchNumber(s.getBatchNumber());
        	tmp.setId(s.getId());
        	
        	sp.add(tmp);
        }*/
        
        //target.setSpecimen(sp);
        
        if(source.getDateOfBirth() != null) {
        	target.setDateOfBirth(source.getDateOfBirth());
        }
        
        if(source.getDepartureDate() != null) {
        	target.setDepartureDate(source.getDateOfBirth());
        }
        
        target.setCreated(source.getCreated());
        target.setLastUpdated(source.getLastUpdated());
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PatientVO toPatientVO(final Patient entity)
    {
        // TODO verify behavior of toPatientVO
        return super.toPatientVO(entity);
    }

    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private Patient loadPatientFromPatientVO(PatientVO patientVO)
    {
    	
    	Patient patient = null;
    	
    	if(patientVO.getId() != null) {
    		patient = this.load(patientVO.getId());
    	} else {
    		patient = Patient.Factory.newInstance();
    	}
    	
    	return patient;
    }

    /**
     * {@inheritDoc}
     */
    public Patient patientVOToEntity(PatientVO patientVO)
    {
        // TODO verify behavior of patientVOToEntity
        Patient entity = this.loadPatientFromPatientVO(patientVO);
        this.patientVOToEntity(patientVO, entity, true);
        
        if(entity.getSpecimen() == null) {
        	entity.setSpecimen(new ArrayList<Specimen>());
        }
        
        for(SpecimenVO s : patientVO.getSpecimen()) {
        	
        	Specimen sp = Specimen.Factory.newInstance();
        	getSpecimenDao().specimenVOToEntity(s, sp, true);
        	sp.setPatient(entity);
        	entity.getSpecimen().add(sp);
        }

        
        if(patientVO.getDateOfBirth() != null) {
        	entity.setDateOfBirth(patientVO.getDateOfBirth());
        }
        
        if(patientVO.getDepartureDate() != null) {
        	entity.setDepartureDate(patientVO.getDateOfBirth());
        }
        
        entity.setCreated(patientVO.getCreated());
        entity.setLastUpdated(patientVO.getLastUpdated());
        
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void patientVOToEntity(
        PatientVO source,
        Patient target,
        boolean copyIfNull)
    {
        // TODO verify behavior of patientVOToEntity
        super.patientVOToEntity(source, target, copyIfNull);
        
        if(source.getDateOfBirth() != null) {
        	target.setDateOfBirth(source.getDateOfBirth());
        }
    }
}