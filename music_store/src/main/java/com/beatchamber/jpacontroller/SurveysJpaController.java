package com.beatchamber.jpacontroller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.beatchamber.entities.SurveyToChoice;
import com.beatchamber.entities.Surveys;
import com.beatchamber.exceptions.IllegalOrphanException;
import com.beatchamber.exceptions.NonexistentEntityException;
import com.beatchamber.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Yan and Massimo Di Girolamo
 */
@Named
@SessionScoped
public class SurveysJpaController implements Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(SurveysJpaController.class);

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public SurveysJpaController() {
    }

    public void create(Surveys surveys) throws RollbackFailureException {
        if (surveys.getSurveyToChoiceList() == null) {
            surveys.setSurveyToChoiceList(new ArrayList<SurveyToChoice>());
        }

        try {

            utx.begin();
            List<SurveyToChoice> attachedSurveyToChoiceList = new ArrayList<SurveyToChoice>();
            for (SurveyToChoice surveyToChoiceListSurveyToChoiceToAttach : surveys.getSurveyToChoiceList()) {
                surveyToChoiceListSurveyToChoiceToAttach = em.getReference(surveyToChoiceListSurveyToChoiceToAttach.getClass(), surveyToChoiceListSurveyToChoiceToAttach.getTablekey());
                attachedSurveyToChoiceList.add(surveyToChoiceListSurveyToChoiceToAttach);
            }
            surveys.setSurveyToChoiceList(attachedSurveyToChoiceList);
            em.persist(surveys);
            for (SurveyToChoice surveyToChoiceListSurveyToChoice : surveys.getSurveyToChoiceList()) {
                Surveys oldSurveyIdOfSurveyToChoiceListSurveyToChoice = surveyToChoiceListSurveyToChoice.getSurveyId();
                surveyToChoiceListSurveyToChoice.setSurveyId(surveys);
                surveyToChoiceListSurveyToChoice = em.merge(surveyToChoiceListSurveyToChoice);
                if (oldSurveyIdOfSurveyToChoiceListSurveyToChoice != null) {
                    oldSurveyIdOfSurveyToChoiceListSurveyToChoice.getSurveyToChoiceList().remove(surveyToChoiceListSurveyToChoice);
                    oldSurveyIdOfSurveyToChoiceListSurveyToChoice = em.merge(oldSurveyIdOfSurveyToChoiceListSurveyToChoice);
                }
            }
            utx.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            try {
                utx.rollback();
                LOG.error("Rollback");
            } catch (IllegalStateException | SecurityException | SystemException re) {
                LOG.error("Rollback2");

                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
        }
    }

    public void edit(Surveys surveys) throws NonexistentEntityException, Exception {

        try {

            utx.begin();
            Surveys persistentSurveys = em.find(Surveys.class, surveys.getSurveyId());
            List<SurveyToChoice> surveyToChoiceListOld = persistentSurveys.getSurveyToChoiceList();
            List<SurveyToChoice> surveyToChoiceListNew = surveys.getSurveyToChoiceList();
            List<SurveyToChoice> attachedSurveyToChoiceListNew = new ArrayList<SurveyToChoice>();
            for (SurveyToChoice surveyToChoiceListNewSurveyToChoiceToAttach : surveyToChoiceListNew) {
                surveyToChoiceListNewSurveyToChoiceToAttach = em.getReference(surveyToChoiceListNewSurveyToChoiceToAttach.getClass(), surveyToChoiceListNewSurveyToChoiceToAttach.getTablekey());
                attachedSurveyToChoiceListNew.add(surveyToChoiceListNewSurveyToChoiceToAttach);
            }
            surveyToChoiceListNew = attachedSurveyToChoiceListNew;
            surveys.setSurveyToChoiceList(surveyToChoiceListNew);
            surveys = em.merge(surveys);
            for (SurveyToChoice surveyToChoiceListOldSurveyToChoice : surveyToChoiceListOld) {
                if (!surveyToChoiceListNew.contains(surveyToChoiceListOldSurveyToChoice)) {
                    surveyToChoiceListOldSurveyToChoice.setSurveyId(null);
                    surveyToChoiceListOldSurveyToChoice = em.merge(surveyToChoiceListOldSurveyToChoice);
                }
            }
            for (SurveyToChoice surveyToChoiceListNewSurveyToChoice : surveyToChoiceListNew) {
                if (!surveyToChoiceListOld.contains(surveyToChoiceListNewSurveyToChoice)) {
                    Surveys oldSurveyIdOfSurveyToChoiceListNewSurveyToChoice = surveyToChoiceListNewSurveyToChoice.getSurveyId();
                    surveyToChoiceListNewSurveyToChoice.setSurveyId(surveys);
                    surveyToChoiceListNewSurveyToChoice = em.merge(surveyToChoiceListNewSurveyToChoice);
                    if (oldSurveyIdOfSurveyToChoiceListNewSurveyToChoice != null && !oldSurveyIdOfSurveyToChoiceListNewSurveyToChoice.equals(surveys)) {
                        oldSurveyIdOfSurveyToChoiceListNewSurveyToChoice.getSurveyToChoiceList().remove(surveyToChoiceListNewSurveyToChoice);
                        oldSurveyIdOfSurveyToChoiceListNewSurveyToChoice = em.merge(oldSurveyIdOfSurveyToChoiceListNewSurveyToChoice);
                    }
                }
            }
            utx.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            try {
                utx.rollback();
            } catch (IllegalStateException | SecurityException | SystemException re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = surveys.getSurveyId();
                if (findSurveys(id) == null) {
                    throw new NonexistentEntityException("The album with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, NotSupportedException, SystemException, RollbackFailureException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

        try {
            utx.begin();
            Surveys surveys;
            try {
                surveys = em.getReference(Surveys.class, id);
                surveys.getSurveyId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The surveys with id " + id + " no longer exists.", enfe);
            }
            List<SurveyToChoice> surveyToChoiceList = surveys.getSurveyToChoiceList();
            for (SurveyToChoice surveyToChoiceListSurveyToChoice : surveyToChoiceList) {
                surveyToChoiceListSurveyToChoice.setSurveyId(null);
                surveyToChoiceListSurveyToChoice = em.merge(surveyToChoiceListSurveyToChoice);
            }
            em.remove(surveys);
            utx.commit();
        } catch (NotSupportedException | SystemException | NonexistentEntityException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            try {
                utx.rollback();
            } catch (IllegalStateException | SecurityException | SystemException re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
    }

    public List<Surveys> findSurveysEntities() {
        return findSurveysEntities(true, -1, -1);
    }

    public List<Surveys> findSurveysEntities(int maxResults, int firstResult) {
        return findSurveysEntities(false, maxResults, firstResult);
    }

    private List<Surveys> findSurveysEntities(boolean all, int maxResults, int firstResult) {

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Surveys.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();

    }

    public Surveys findSurveys(Integer id) {

        return em.find(Surveys.class, id);

    }

    public int getSurveysCount() {

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Surveys> rt = cq.from(Surveys.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();

    }

}
