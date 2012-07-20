package net.insolutions.rtquote.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

//import javax.persistence.*;
import javax.persistence.Entity;

import com.farata.dto2fx.annotations.FXClass;
import com.farata.dto2fx.annotations.FXClassKind;
import com.farata.dto2fx.annotations.FXIgnore;
import com.farata.dto2fx.annotations.FXKeyColumn;
import com.farata.dto2fx.annotations.FXManyToOne;
import com.farata.dto2fx.annotations.FXOneToMany;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension; 
import javax.jdo.annotations.IdGeneratorStrategy; 
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent; 
import javax.jdo.annotations.PrimaryKey; 
import javax.jdo.annotations.PersistenceCapable;

/**
 * The persistent class for the driver database table.
 * 
 */

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
//@Entity
//@Table(name="driver")
@FXClass(kind=FXClassKind.REMOTE)
public class Driver implements Serializable {
	private static final long serialVersionUID = 1L;
	
//	@Transient 
	private String uid;
	@FXIgnore
	public String getUid() {
		if (uid == null) {
			uid = "" + id;
		}
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}

	//@Id // @FXKeyColumn
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String id;

//    @Temporal( TemporalType.DATE)
	private Date dob;

	private String firstname;

	private String gender;

	private int isdriver;

	private String lastname;

	private String licensenumber;

	private String maritalstatus;

	private int previnsured;

	private String relationship;

	private String state;

	private int ticketsaccidents;

	//bi-directional many-to-one association to Driver
//	@OneToMany(mappedBy="driver")
	@Persistent(mappedBy = "driver", defaultFetchGroup = "true") 
	@Element(dependent = "true") 	
	private Set<IncidentClaim> incidentClaims;

	//bi-directional many-to-one association to Applicant
//    @ManyToOne
    @Persistent(defaultFetchGroup = "true")
	private Applicant applicant;

    public Driver() {
    }

    @FXKeyColumn
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDob() {
		return this.dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getIsdriver() {
		return this.isdriver;
	}

	public void setIsdriver(int isdriver) {
		this.isdriver = isdriver;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getLicensenumber() {
		return this.licensenumber;
	}

	public void setLicensenumber(String licensenumber) {
		this.licensenumber = licensenumber;
	}

	public String getMaritalstatus() {
		return this.maritalstatus;
	}

	public void setMaritalstatus(String maritalstatus) {
		this.maritalstatus = maritalstatus;
	}

	public int getPrevinsured() {
		return this.previnsured;
	}

	public void setPrevinsured(int previnsured) {
		this.previnsured = previnsured;
	}

	public String getRelationship() {
		return this.relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getTicketsaccidents() {
		return this.ticketsaccidents;
	}

	public void setTicketsaccidents(int ticketsaccidents) {
		this.ticketsaccidents = ticketsaccidents;
	}

	//Add this to make sure the reverse linkage is set properly for the DataCollection
	@FXManyToOne(parent="net.insolutions.rtquote.entity.Applicant", property="id")
	public Applicant getApplicant() {
		return this.applicant;
	}

	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}
	
	//Add this to force lazy, on demand population of the child DataCollection
	@FXOneToMany(fillArguments="id")
	public Set<IncidentClaim> getIncidentClaims() {
		return this.incidentClaims;
	}

	public void setIncidentClaims(Set<IncidentClaim> incidentClaims) {
		this.incidentClaims = incidentClaims;
	}
}