	
package net.insolutions.rtquote.service;

import java.util.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.EntityTransaction;
//import javax.persistence.Query;

import javax.jdo.PersistenceManagerFactory;
import javax.jdo.PersistenceManager;
import javax.jdo.JDOHelper;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.Extent;

import org.apache.log4j.Logger;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import clear.transaction.*;
import clear.transaction.identity.PropertyRack;
import clear.utils.MessagingUtils;
import clear.cdb.utils.SessionFactoryUtils;
import clear.messaging.ThreadLocals;

import flex.messaging.messages.Message;
import flex.data.*;
import flex.data.messages.DataMessage;

import org.apache.log4j.Logger;

import net.insolutions.rtquote.entity.Applicant;
import net.insolutions.rtquote.entity.Vehicle;
import net.insolutions.rtquote.entity.Driver;
import net.insolutions.rtquote.entity.IncidentClaim;

public class QuoteService {

	static Logger logger;
	static 	{
	        logger = Logger.getLogger(BatchGateway.class);
	}
	
	//private static final EntityManagerFactory 
		//	emf = Persistence.createEntityManagerFactory("RealTimeQuotesGAE");	
	
	private static final PersistenceManagerFactory 
			pmf = JDOHelper.getPersistenceManagerFactory("RealTimeQuotesJDOGAE");
	
	public java.util.List<net.insolutions.rtquote.entity.Applicant> getApplicants() {
		
		//EntityManager em = emf.createEntityManager();
		//EntityTransaction tx = em.getTransaction();

		PersistenceManager pm = pmf.getPersistenceManager();
//		Transaction tx = pm.currentTransaction();

		Query query = pm.newQuery(Applicant.class);
		
		try {
//			tx.begin();

			@SuppressWarnings("unchecked")
			List<Applicant> results = (List<Applicant>)query.execute();
			int size = results.size();
//			logger.info("Size of Applicants is: " + size);
			
//		    tx.commit();
			return results;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		finally
		{
		    query.closeAll();
		    pm.close();
		}	
	}
	
 	public List<ChangeObject> getApplicants_sync(List<ChangeObject> items) throws Throwable {
		try {
	 		getApplicants_deleteItems(items);
	 		getApplicants_updateItems(items);
	 		getApplicants_insertItems(items);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
			
 		return items;
 	}

	public List<ChangeObject> getApplicants_deleteItems(List<ChangeObject> items) throws Exception {
 		List<ChangeObject> list = null;
		PersistenceManager pm = pmf.getPersistenceManager(); 

		Query query = pm.newQuery(Applicant.class);
		query.setFilter("id==applicantId");
		query.declareParameters("Long applicantId");			

		ChangeObject co = null;
		Iterator<ChangeObject> iterator = items.iterator();
		list = new ArrayList<ChangeObject>();
		while (iterator.hasNext()) {
			co = (ChangeObject) iterator.next();
			if (co.isDelete()) {
				net.insolutions.rtquote.entity.Applicant item = (net.insolutions.rtquote.entity.Applicant) co.getPreviousVersion();
				if (item != null) {
					try {
						query.deletePersistentAll(item.getId());
					} catch (Exception e) {
						query.closeAll();
					    pm.close();
						
						DataMessage cause = new DataMessage();
	                	Map<String, String> identity = new HashMap<String, String>();
						
						identity.put("id", item.getId() + "");
						
	                	cause.setIdentity(identity);
	                  	cause.setOperation(DataMessage.DELETE_OPERATION);                       	
	                  	cause.setBody(new Object[]{null,item, null});
	                  	cause.setDestination("net.insolutions.rtquote.service.IQuoteService");
	                  	cause.setHeader("method", "getApplicants");

	                	DataSyncException dse =  new DataSyncException(co);
	                	dse.setConflictCause(cause); 
	                	dse.setDetails(e.getMessage());
	                    throw dse;
					}
				}
				list.add(co);
			}
		}

		query.closeAll();
	    pm.close();
		
		return list;
	} 	

	public List<ChangeObject> getApplicants_updateItems(List<ChangeObject> items) throws Exception {
 		List<ChangeObject> list = null;
		PersistenceManager pm = pmf.getPersistenceManager(); 

		ChangeObject co = null;
		Iterator<ChangeObject> iterator = items.iterator();
		list = new ArrayList<ChangeObject>();
		while (iterator.hasNext()) {
			co = (ChangeObject) iterator.next();
			if (co.isUpdate()) {
				net.insolutions.rtquote.entity.Applicant item = (net.insolutions.rtquote.entity.Applicant) co.getNewVersion();
				if (item != null) {
					net.insolutions.rtquote.entity.Applicant entity = new net.insolutions.rtquote.entity.Applicant();
					entity.setId(item.getId());
							
					if (entity != null) {					
					
						entity.setBillingcity(item.getBillingcity());							
							
						entity.setBillingcounty(item.getBillingcounty());							
							
						entity.setBillingishome(item.getBillingishome());							
							
						entity.setBillingstate(item.getBillingstate());							
							
						entity.setBillingstreet(item.getBillingstreet());							
							
						entity.setBillingsuite(item.getBillingsuite());							
							
						entity.setBillingzip(item.getBillingzip());							
							
						entity.setEmail(item.getEmail());							
							
						entity.setFirstname(item.getFirstname());							
							
						entity.setHomecity(item.getHomecity());							
							
						entity.setHomecounty(item.getHomecounty());							
							
						entity.setHomephone(item.getHomephone());							
							
						entity.setHomestate(item.getHomestate());							
							
						entity.setHomestreet(item.getHomestreet());							
							
						entity.setHomesuite(item.getHomesuite());							
							
						entity.setHomezip(item.getHomezip());							
							
						entity.setLastname(item.getLastname());							

						//entity.setId(item.getId());							

						try {
							pm.makePersistent(entity);
						} catch (Exception e) {
						    pm.close();
							
							DataMessage cause = new DataMessage();
		                	Map<String, String> identity = new HashMap<String, String>();
							
							identity.put("id", item.getId() + "");
							
		                	cause.setIdentity(identity);
		                  	cause.setOperation(DataMessage.UPDATE_OPERATION);                       	
		                  	cause.setBody(new Object[]{null,item, null});
		                  	cause.setDestination("net.insolutions.rtquote.service.IQuoteService");
		                  	cause.setHeader("method", "getApplicants");

		                	DataSyncException dse =  new DataSyncException(co);
		                	dse.setConflictCause(cause); 
		                	dse.setDetails(e.getMessage());
		                    throw dse;
						}
					}
				}
				list.add(co);
			}
		}
	    pm.close();
		
		return list;	
	} 	

	public List<ChangeObject> getApplicants_insertItems(List<ChangeObject> items) throws Exception {
 		List<ChangeObject> list = null;
 		
		PersistenceManager pm = pmf.getPersistenceManager(); 

		ChangeObject co = null;
		Iterator<ChangeObject> iterator = items.iterator();
		list = new ArrayList<ChangeObject>();
		while (iterator.hasNext()) {
			co = (ChangeObject) iterator.next();
			if (co.isCreate()) {
				net.insolutions.rtquote.entity.Applicant item = (net.insolutions.rtquote.entity.Applicant) co.getNewVersion();
				if (item!=null) {
					
					net.insolutions.rtquote.entity.Applicant entity = new net.insolutions.rtquote.entity.Applicant();
					
					entity.setBillingcity(item.getBillingcity());							
							
					entity.setBillingcounty(item.getBillingcounty());							
							
					entity.setBillingishome(item.getBillingishome());							
							
					entity.setBillingstate(item.getBillingstate());							
							
					entity.setBillingstreet(item.getBillingstreet());							
							
					entity.setBillingsuite(item.getBillingsuite());							
							
					entity.setBillingzip(item.getBillingzip());							
							
					entity.setEmail(item.getEmail());							
							
					entity.setFirstname(item.getFirstname());							
							
					entity.setHomecity(item.getHomecity());							
							
					entity.setHomecounty(item.getHomecounty());							
							
					entity.setHomephone(item.getHomephone());							
							
					entity.setHomestate(item.getHomestate());							
							
					entity.setHomestreet(item.getHomestreet());							
							
					entity.setHomesuite(item.getHomesuite());							
							
					entity.setHomezip(item.getHomezip());							
							
					entity.setLastname(item.getLastname());							

					entity.setId(null);							
							
					try {
						pm.makePersistent(entity);
					} catch (Exception e) {
					    pm.close();
						
						DataMessage cause = new DataMessage();
	                	Map<String, String> identity = new HashMap<String, String>();
						
						identity.put("id", item.getId() + "");
						
	                	cause.setIdentity(identity);
	                  	cause.setOperation(DataMessage.CREATE_OPERATION);                       	
	                  	cause.setBody(new Object[]{null,item, null});
	                  	cause.setDestination("net.insolutions.rtquote.service.IQuoteService");
	                  	cause.setHeader("method", "getApplicants");
	                  	
	                	DataSyncException dse =  new DataSyncException(co);
	                	dse.setConflictCause(cause);
	                	dse.setDetails(e.getMessage());
	                    throw dse;
					}
					
					Object oldValue1 = item.getId();
					item.setId(entity.getId());
					Object newValue1 = item.getId();
					if (oldValue1!=null && !oldValue1.equals(newValue1))
					      co.addChangedPropertyName("id");
					      PropertyRack.setEntity("net.insolutions.rtquote.entity.Applicant", "id", oldValue1, newValue1);
					
					co.setNewVersion(item);
				}
				list.add(co);
			}
		}
	    pm.close();
			
		return list;
	} 	
		
	public java.util.List<net.insolutions.rtquote.entity.Vehicle> getVehicles(java.lang.Long applicantId) {
		PersistenceManager pm = pmf.getPersistenceManager();
//		Transaction tx = pm.currentTransaction();
		
		Query query = pm.newQuery("SELECT FROM " + Vehicle.class.getName() +
			" WHERE applicant == applicantObj parameters Applicant applicantObj");
		
		try {
//			tx.begin();
			
			Applicant applicantObj = pm.getObjectById(Applicant.class, applicantId);
			@SuppressWarnings("unchecked")
			List<Vehicle> results = (List<Vehicle>)query.execute(applicantObj);
			int size = results.size();
//			logger.info("Size of Vehicles is: " + size);

			//@SuppressWarnings("unchecked")
			//List<Vehicle> results = (List<Vehicle>)query.execute(applicantId);
//			tx.commit();

			return results;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		finally
		{
			query.closeAll();
			pm.close();
		}	
	}
	
 	public List<ChangeObject> getVehicles_sync(List<ChangeObject> items) throws Throwable {
		try {
	 		getVehicles_deleteItems(items);
	 		getVehicles_updateItems(items);
	 		getVehicles_insertItems(items);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
			
 		return items;
 	}

	public List<ChangeObject> getVehicles_deleteItems(List<ChangeObject> items) throws Exception {
 		List<ChangeObject> list = null;
		PersistenceManager pm = pmf.getPersistenceManager(); 

		Query query = pm.newQuery(Vehicle.class);
		query.setFilter("id==vehicleId");
		query.declareParameters("String vehicleId");
		
		ChangeObject co = null;
		Iterator<ChangeObject> iterator = items.iterator();
		list = new ArrayList<ChangeObject>();
		while (iterator.hasNext()) {
			co = (ChangeObject) iterator.next();
			if (co.isDelete()) {
				net.insolutions.rtquote.entity.Vehicle item = (net.insolutions.rtquote.entity.Vehicle) co.getPreviousVersion();
				if (item!=null) {
					try {
						query.deletePersistentAll(item.getId());
					} catch (Exception e) {
						query.closeAll();
					    pm.close();
						
						DataMessage cause = new DataMessage();
	                	Map<String, String> identity = new HashMap<String, String>();
						
						identity.put("id", item.getId() + "");
						
	                	cause.setIdentity(identity);
	                    cause.setOperation(DataMessage.DELETE_OPERATION);                       	
	                    cause.setBody(new Object[]{null,item, null});
	                    cause.setDestination("net.insolutions.rtquote.service.IQuoteService");
	                    cause.setHeader("method", "getVehicles");

	                	DataSyncException dse =  new DataSyncException(co);
	                	dse.setConflictCause(cause); 
	                	dse.setDetails(e.getMessage());
	                    throw dse;
					}
				}
				list.add(co);
			}
		}

		query.closeAll();
	    pm.close();
			
		return list;
	} 	

	public List<ChangeObject> getVehicles_updateItems(List<ChangeObject> items) throws Exception {
 		List<ChangeObject> list = null;
 		
		PersistenceManager pm = pmf.getPersistenceManager(); 
//		Transaction tx = pm.currentTransaction();

		ChangeObject co = null;
		Iterator<ChangeObject> iterator = items.iterator();
		list = new ArrayList<ChangeObject>();

//		tx.begin();

		Applicant parent = null;
		Vehicle entity = null;
		
		while (iterator.hasNext()) {
			co = (ChangeObject) iterator.next();
			if (co.isUpdate()) {
				net.insolutions.rtquote.entity.Vehicle item = (net.insolutions.rtquote.entity.Vehicle) co.getNewVersion();
				if (item != null) {
					parent = (Applicant)pm.getObjectById(Applicant.class, item.getApplicant().getId());
					
					for(Vehicle v : parent.getVehicles()) {
						if (v.getId().contains(item.getId())) {
							entity = v;
							break;
						}
					}
							
					if (entity != null) {					
					
						//entity.setApplicant(item.getApplicant());							
							
						//entity.setId(item.getId());							
							
						entity.setMake(item.getMake());							
							
						entity.setModel(item.getModel());							
							
						entity.setVin(item.getVin());							
							
						entity.setYear(item.getYear());							

						parent.getVehicles().add(entity);
						
//						entity.setApplicant(parent);

						try {
							pm.makePersistent(parent);
						} catch (Exception e) {
						    pm.close();
							
							DataMessage cause = new DataMessage();
		                	Map<String, String> identity = new HashMap<String, String>();
							
							identity.put("id", item.getId() + "");
							
		                	cause.setIdentity(identity);
		                    cause.setOperation(DataMessage.UPDATE_OPERATION);                       	
		                    cause.setBody(new Object[]{null,item, null});
		                    cause.setDestination("net.insolutions.rtquote.service.IQuoteService");
		                    cause.setHeader("method", "getVehicles");

		                	DataSyncException dse =  new DataSyncException(co);
		                	dse.setConflictCause(cause); 
		                	dse.setDetails(e.getMessage());
		                    throw dse;
						}
					}
				}
				list.add(co);
			}
		}
	    pm.close();
		
		return list;	
	} 	

	public List<ChangeObject> getVehicles_insertItems(List<ChangeObject> items) throws Exception {
 		List<ChangeObject> list = null;
 		
		PersistenceManager pm = pmf.getPersistenceManager(); 
 		
		ChangeObject co = null;
		Iterator<ChangeObject> iterator = items.iterator();
		list = new ArrayList<ChangeObject>();
		while (iterator.hasNext()) {
			co = (ChangeObject) iterator.next();
			if (co.isCreate()) {
				net.insolutions.rtquote.entity.Vehicle item = (net.insolutions.rtquote.entity.Vehicle) co.getNewVersion();
				if (item!=null) {
					
					if (item.getApplicant() != null) {
						Object parentValue1 = PropertyRack.getEntity("net.insolutions.rtquote.entity.Applicant", "id", item.getApplicant().getId());
						
						item.getApplicant().setId((java.lang.Long) parentValue1);
					}
								
					net.insolutions.rtquote.entity.Vehicle entity = new net.insolutions.rtquote.entity.Vehicle();
					
					entity.setId(null);
							
					entity.setMake(item.getMake());							
							
					entity.setModel(item.getModel());							
							
					entity.setVin(item.getVin());							
							
					entity.setYear(item.getYear());							
							
					try {
						Applicant parent = (Applicant)pm.getObjectById(Applicant.class, item.getApplicant().getId());
						entity.setApplicant(parent);
						parent.getVehicles().add(entity);
					} catch (Exception e) {
					    pm.close();
						
						DataMessage cause = new DataMessage();
	                	Map<String, String> identity = new HashMap<String, String>();
						
						identity.put("id", item.getId() + "");
						
	                	cause.setIdentity(identity);
	                    cause.setOperation(DataMessage.CREATE_OPERATION);                       	
	                    cause.setBody(new Object[]{null,item, null});
	                    cause.setDestination("net.insolutions.rtquote.service.IQuoteService");
	                    cause.setHeader("method", "getVehicles");

	                	DataSyncException dse =  new DataSyncException(co);
	                	dse.setConflictCause(cause); 
	                	dse.setDetails(e.getMessage());
	                    throw dse;
					}
					
					Object oldValue1 = item.getId();
					item.setId(entity.getId());
					Object newValue1 = item.getId();
					if (oldValue1!=null && !oldValue1.equals(newValue1))
					      co.addChangedPropertyName("id");
					PropertyRack.setEntity("net.insolutions.rtquote.entity.Vehicle", "id", oldValue1, newValue1);
					
					co.setNewVersion(item);
				}
				list.add(co);
			}
		}
	    pm.close();
			
		return list;
	} 	
	
	public java.util.List<net.insolutions.rtquote.entity.Driver> getDrivers(java.lang.Long applicantId) {

//		logger.info("Get Drivers for Applicant id : " + applicantId);
		
		PersistenceManager pm = pmf.getPersistenceManager();
//		Transaction tx = pm.currentTransaction();
		
		Query query = pm.newQuery("SELECT FROM " + Driver.class.getName() +
			" WHERE applicant == applicantObj parameters Applicant applicantObj");
		
		try {
//			tx.begin();
		
			Applicant applicantObj = pm.getObjectById(Applicant.class, applicantId);
			@SuppressWarnings("unchecked")
			List<Driver> results = (List<Driver>)query.execute(applicantObj);
			results.size();

//			tx.commit();

			return results;
		} catch (Throwable e) {
//			try {
//				tx.rollback();
//			} catch (Throwable th) {
//				throw new RuntimeException(th);
//			}
			throw new RuntimeException(e);
		}
		finally
		{
			query.closeAll();
//			if (tx.isActive())
//			{
//				tx.rollback();
//			}
			pm.close();
		}	
	}
	
 	public List<ChangeObject> getDrivers_sync(List<ChangeObject> items) throws Throwable {
		try {
	 		getDrivers_deleteItems(items);
	 		getDrivers_updateItems(items);
	 		getDrivers_insertItems(items);
		} catch (Throwable e) {
			try {
			} catch (Throwable th) {
				throw new RuntimeException(th);
			}
			throw new RuntimeException(e);
		}
			
 		return items;
 	}

	public List<ChangeObject> getDrivers_deleteItems(List<ChangeObject> items) throws Exception {
 		List<ChangeObject> list = null;
		PersistenceManager pm = pmf.getPersistenceManager(); 

		Query query = pm.newQuery(Driver.class);
		query.setFilter("id==driverId");
		query.declareParameters("String driverId");
		
		ChangeObject co = null;
		Iterator<ChangeObject> iterator = items.iterator();
		list = new ArrayList<ChangeObject>();
		while (iterator.hasNext()) {
			co = (ChangeObject) iterator.next();
			if (co.isDelete()) {
				net.insolutions.rtquote.entity.Driver item = (net.insolutions.rtquote.entity.Driver) co.getPreviousVersion();
				if (item!=null) {
					try {
						query.deletePersistentAll(item.getId());
					} catch (Exception e) {
						query.closeAll();
					    pm.close();
						
						DataMessage cause = new DataMessage();
	                	Map<String, String> identity = new HashMap<String, String>();
						
						identity.put("id", item.getId() + "");
						
	                	cause.setIdentity(identity);
	                    cause.setOperation(DataMessage.DELETE_OPERATION);                       	
	                    cause.setBody(new Object[]{null,item, null});
	                    cause.setDestination("net.insolutions.rtquote.service.IQuoteService");
	                    cause.setHeader("method", "getDrivers");

	                	DataSyncException dse =  new DataSyncException(co);
	                	dse.setConflictCause(cause); 
	                	dse.setDetails(e.getMessage());
	                    throw dse;
					}
				}
				list.add(co);
			}
		}
			
		query.closeAll();
	    pm.close();

	    return list;
	} 	

	public List<ChangeObject> getDrivers_updateItems(List<ChangeObject> items) throws Exception {
 		List<ChangeObject> list = null;
 		
		PersistenceManager pm = pmf.getPersistenceManager(); 
//		Transaction tx = pm.currentTransaction();
 		
		ChangeObject co = null;
		Iterator<ChangeObject> iterator = items.iterator();
		list = new ArrayList<ChangeObject>();

//		tx.begin();

		Applicant parent = null;
		Driver entity = null;
		
		while (iterator.hasNext()) {
			co = (ChangeObject) iterator.next();
			if (co.isUpdate()) {
				net.insolutions.rtquote.entity.Driver item = (net.insolutions.rtquote.entity.Driver) co.getNewVersion();
				if (item != null) {
					
					parent = (Applicant)pm.getObjectById(Applicant.class, item.getApplicant().getId());
					
					for(Driver v : parent.getDrivers()) {
						if (v.getId().contains(item.getId())) {
							entity = v;
							break;
						}
					}
					
					if (entity != null) {					
						entity.setDob(item.getDob());							
							
						entity.setFirstname(item.getFirstname());							
							
						entity.setGender(item.getGender());							
							
						entity.setIsdriver(item.getIsdriver());							
							
						entity.setLastname(item.getLastname());							
							
						entity.setLicensenumber(item.getLicensenumber());							
							
						entity.setMaritalstatus(item.getMaritalstatus());							
							
						entity.setPrevinsured(item.getPrevinsured());							
							
						entity.setRelationship(item.getRelationship());							
							
						entity.setState(item.getState());							
							
						entity.setTicketsaccidents(item.getTicketsaccidents());							

						//entity.setIncidentClaims(item.getIncidentClaims());							
						
						parent.getDrivers().add(entity);

						try {
							pm.makePersistent(parent);
						} catch (Exception e) {
						    pm.close();
							
							DataMessage cause = new DataMessage();
		                	Map<String, String> identity = new HashMap<String, String>();
							
							identity.put("id", item.getId() + "");
							
		                	cause.setIdentity(identity);
		                    cause.setOperation(DataMessage.UPDATE_OPERATION);                       	
		                    cause.setBody(new Object[]{null,item, null});
		                    cause.setDestination("net.insolutions.rtquote.service.IQuoteService");
		                    cause.setHeader("method", "getDrivers");

		                	DataSyncException dse =  new DataSyncException(co);
		                	dse.setConflictCause(cause); 
		                	dse.setDetails(e.getMessage());
		                    throw dse;
						}
					}
				}
				list.add(co);
			}
		}
	    pm.close();
		
		return list;	
	} 	

	public List<ChangeObject> getDrivers_insertItems(List<ChangeObject> items) throws Exception {
 		List<ChangeObject> list = null;
 		
		PersistenceManager pm = pmf.getPersistenceManager(); 
//		Transaction tx = pm.currentTransaction();
 		
		ChangeObject co = null;
		Iterator<ChangeObject> iterator = items.iterator();
		list = new ArrayList<ChangeObject>();
		while (iterator.hasNext()) {
			co = (ChangeObject) iterator.next();
			if (co.isCreate()) {
				net.insolutions.rtquote.entity.Driver item = (net.insolutions.rtquote.entity.Driver) co.getNewVersion();
				if (item!=null) {
					
					if (item.getApplicant() != null) {
						Object parentValue1 = PropertyRack.getEntity("net.insolutions.rtquote.entity.Applicant", "id", item.getApplicant().getId());
						
						item.getApplicant().setId((java.lang.Long) parentValue1);
					}
								
					net.insolutions.rtquote.entity.Driver entity = new net.insolutions.rtquote.entity.Driver();
					
					
					//Key key = KeyFactory.createKey(Vehicle.class.getSimpleName(), item.getId());
					//entity.setId(KeyFactory.keyToString(key));

					entity.setId(null);
							
					entity.setDob(item.getDob());							
					
					entity.setFirstname(item.getFirstname());							
						
					entity.setGender(item.getGender());							
						
					entity.setIsdriver(item.getIsdriver());							
						
					entity.setLastname(item.getLastname());							
						
					entity.setLicensenumber(item.getLicensenumber());							
						
					entity.setMaritalstatus(item.getMaritalstatus());							
						
					entity.setPrevinsured(item.getPrevinsured());							
						
					entity.setRelationship(item.getRelationship());							
						
					entity.setState(item.getState());							
						
					entity.setTicketsaccidents(item.getTicketsaccidents());							

					try {
						Applicant parent = (Applicant)pm.getObjectById(Applicant.class, item.getApplicant().getId());
						entity.setApplicant(parent);
						parent.getDrivers().add(entity);
					} catch (Exception e) {
					    pm.close();
						
						DataMessage cause = new DataMessage();
	                	Map<String, String> identity = new HashMap<String, String>();
						
						identity.put("id", item.getId() + "");
						
	                	cause.setIdentity(identity);
	                    cause.setOperation(DataMessage.CREATE_OPERATION);                       	
	                    cause.setBody(new Object[]{null,item, null});
	                    cause.setDestination("net.insolutions.rtquote.service.IQuoteService");
	                    cause.setHeader("method", "getDrivers");

	                	DataSyncException dse =  new DataSyncException(co);
	                	dse.setConflictCause(cause); 
	                	dse.setDetails(e.getMessage());
	                    throw dse;
					}
					
					Object oldValue1 = item.getId();
					item.setId(entity.getId());
					Object newValue1 = item.getId();
					if (oldValue1!=null && !oldValue1.equals(newValue1))
					      co.addChangedPropertyName("id");
					PropertyRack.setEntity("net.insolutions.rtquote.entity.Driver", "id", oldValue1, newValue1);
					
					co.setNewVersion(item);
				}
				list.add(co);
			}
		}
	    pm.close();
			
		return list;
	} 	

	public java.util.List<net.insolutions.rtquote.entity.IncidentClaim> getIncidentClaims(java.lang.String driverId) {
		PersistenceManager pm = pmf.getPersistenceManager();
//		Transaction tx = pm.currentTransaction();
		
		Query query = pm.newQuery("SELECT FROM " + IncidentClaim.class.getName() +
			" WHERE driver == driverObj parameters Driver driverObj");
		
		try {
//			tx.begin();
			
			Driver driverObj = pm.getObjectById(Driver.class, driverId);
			@SuppressWarnings("unchecked")
			List<IncidentClaim> results = (List<IncidentClaim>)query.execute(driverObj);
			results.size();
//			tx.commit();

			return results;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		finally
		{
			query.closeAll();
			pm.close();
		}	
	}
	
 	public List<ChangeObject> getIncidentClaims_sync(List<ChangeObject> items) throws Throwable {
		try {
	 		getIncidentClaims_deleteItems(items);
	 		getIncidentClaims_updateItems(items);
	 		getIncidentClaims_insertItems(items);
		} catch (Throwable e) {
			try {
			} catch (Throwable th) {
				throw new RuntimeException(th);
			}
			throw new RuntimeException(e);
		}
			
 		return items;
 	}

	public List<ChangeObject> getIncidentClaims_deleteItems(List<ChangeObject> items) throws Exception {
 		List<ChangeObject> list = null;
		PersistenceManager pm = pmf.getPersistenceManager(); 

		Query query = pm.newQuery(IncidentClaim.class);
		query.setFilter("id==incidentClaimId");
		query.declareParameters("String incidentClaimId");
		
		ChangeObject co = null;
		Iterator<ChangeObject> iterator = items.iterator();
		list = new ArrayList<ChangeObject>();
		while (iterator.hasNext()) {
			co = (ChangeObject) iterator.next();
			if (co.isDelete()) {
				net.insolutions.rtquote.entity.IncidentClaim item = (net.insolutions.rtquote.entity.IncidentClaim) co.getPreviousVersion();
				if (item!=null) {
					try {
						query.deletePersistentAll(item.getId());
					} catch (Exception e) {
						query.closeAll();
					    pm.close();
						
						DataMessage cause = new DataMessage();
               			Map<String, String> identity = new HashMap<String, String>();
						
						identity.put("id", item.getId() + "");
						
               			cause.setIdentity(identity);
                   		cause.setOperation(DataMessage.DELETE_OPERATION);                       	
                   		cause.setBody(new Object[]{null,item, null});
                   		cause.setDestination("net.insolutions.rtquote.service.IQuoteService");
                   		cause.setHeader("method", "getIncidentClaims");

               			DataSyncException dse =  new DataSyncException(co);
               			dse.setConflictCause(cause); 
	                	dse.setDetails(e.getMessage());
                   		throw dse;
					}
				}
				list.add(co);
			}
		}
		query.closeAll();
	    pm.close();
			
		return list;
	} 	

	public List<ChangeObject> getIncidentClaims_updateItems(List<ChangeObject> items) throws Exception {
 		List<ChangeObject> list = null;
 		
		PersistenceManager pm = pmf.getPersistenceManager(); 
 		
		ChangeObject co = null;
		Iterator<ChangeObject> iterator = items.iterator();
		list = new ArrayList<ChangeObject>();

		Driver parent = null;
		IncidentClaim entity = null;
		
		while (iterator.hasNext()) {
			co = (ChangeObject) iterator.next();
			if (co.isUpdate()) {
				net.insolutions.rtquote.entity.IncidentClaim item = (net.insolutions.rtquote.entity.IncidentClaim) co.getNewVersion();
				if (item != null) {
					
					parent = (Driver)pm.getObjectById(Driver.class, item.getDriver().getId());
					
					for(IncidentClaim v : parent.getIncidentClaims()) {
						if (v.getId().contains(item.getId())) {
							entity = v;
							break;
						}
					}
					
					if (entity != null) {					
						entity.setIncidentDate(item.getIncidentDate());							
						
						entity.setIncidentType(item.getIncidentType());							
						
						parent.getIncidentClaims().add(entity);

						try {
							pm.makePersistent(parent);
						} catch (Exception e) {
						    pm.close();
							
							DataMessage cause = new DataMessage();
		                	Map<String, String> identity = new HashMap<String, String>();
							
							identity.put("id", item.getId() + "");
							
		                	cause.setIdentity(identity);
		                    cause.setOperation(DataMessage.UPDATE_OPERATION);                       	
		                    cause.setBody(new Object[]{null,item, null});
		                    cause.setDestination("net.insolutions.rtquote.service.IQuoteService");
		                    cause.setHeader("method", "getIncidentClaims");

		                	DataSyncException dse =  new DataSyncException(co);
		                	dse.setConflictCause(cause); 
		                	dse.setDetails(e.getMessage());
		                    throw dse;
						}
						
					}
				}
				list.add(co);
			}
		}
	    pm.close();
		
		return list;	
	} 	

	public List<ChangeObject> getIncidentClaims_insertItems(List<ChangeObject> items) throws Exception {
 		List<ChangeObject> list = null;
 		
		PersistenceManager pm = pmf.getPersistenceManager(); 
//		Transaction tx = pm.currentTransaction();
 		
		ChangeObject co = null;
		Iterator<ChangeObject> iterator = items.iterator();
		list = new ArrayList<ChangeObject>();
		while (iterator.hasNext()) {
			co = (ChangeObject) iterator.next();
			if (co.isCreate()) {
				net.insolutions.rtquote.entity.IncidentClaim item = (net.insolutions.rtquote.entity.IncidentClaim) co.getNewVersion();
				if (item!=null) {
					
					if (item.getDriver() != null) {
						Object parentValue1 = PropertyRack.getEntity("net.insolutions.rtquote.entity.Driver", "id", item.getDriver().getId());
						
						item.getDriver().setId((java.lang.String) parentValue1);
					}
								
					net.insolutions.rtquote.entity.IncidentClaim entity = new net.insolutions.rtquote.entity.IncidentClaim();
					
					//Key key = KeyFactory.createKey(Vehicle.class.getSimpleName(), item.getId());
					//entity.setId(KeyFactory.keyToString(key));

					entity.setId(null);
					//entity.setId(item.getId());							

					entity.setIncidentDate(item.getIncidentDate());							
					
					entity.setIncidentType(item.getIncidentType());							

					try {
						Driver parent = (Driver)pm.getObjectById(Driver.class, item.getDriver().getId());
						entity.setDriver(parent);
						parent.getIncidentClaims().add(entity);
					} catch (Exception e) {
					    pm.close();
						
						DataMessage cause = new DataMessage();
	                	Map<String, String> identity = new HashMap<String, String>();
						
						identity.put("id", item.getId() + "");
						
	                	cause.setIdentity(identity);
	                    cause.setOperation(DataMessage.CREATE_OPERATION);                       	
	                    cause.setBody(new Object[]{null,item, null});
	                    cause.setDestination("net.insolutions.rtquote.service.IQuoteService");
	                    cause.setHeader("method", "getIncidentClaims");

	                	DataSyncException dse =  new DataSyncException(co);
	                	dse.setConflictCause(cause); 
	                	dse.setDetails(e.getMessage());
	                    throw dse;
					}
					
					Object oldValue1 = item.getId();
					item.setId(entity.getId());
					Object newValue1 = item.getId();
					if (oldValue1!=null && !oldValue1.equals(newValue1))
					      co.addChangedPropertyName("id");
						PropertyRack.setEntity("net.insolutions.rtquote.entity.IncidentClaim", "id", oldValue1, newValue1);
					
					co.setNewVersion(item);
				}
				list.add(co);
			}
		}
	    pm.close();
			
		return list;
	} 	
}
