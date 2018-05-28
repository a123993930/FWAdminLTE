package com.guy.system.user.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.guy.system.organization.entity.Organization;

/**
 * 用户机构entity
 * @author blank
 * @date 2015年5月9日 
 */
@Entity
@Table(name = "sys_user_org")
public class UserOrg implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private User user;
	private Organization organization;

	// Constructors

	/** default constructor */
	public UserOrg() {
	}
	// Fields

	public UserOrg(Integer id) {
		this.id = id;
	}


	public UserOrg(User user, Organization organization) {
		this.user=user;
		this.organization=organization;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID", nullable = false)
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	

}