package com.guy.system.organization.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guy.system.user.entity.UserOrg;

/**
 * 机构entity
 * @author blank
 * @date 2015年5月9日 
 */
@Entity
@Table(name = "sys_organization",indexes={ @Index(columnList = "pid"), @Index(columnList = "org_code"), @Index(columnList = "org_name")})
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate @DynamicInsert
public class Organization implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private Integer id;
	private String orgName;
	private String pid;
	private String orgType;
	private Integer orgSort;
	private Integer orgLevel;
	private String orgCode;
	private Integer areaId;
	private String state;//树默认 关闭
	@JsonIgnore
	private Set<UserOrg> userOrgs = new HashSet<UserOrg>(0);
	// Constructors

	/** default constructor */
	public Organization() {
	}

	/** minimal constructor */
	public Organization(Integer id) {
		this.id = id;
	}
	/** minimal constructor */
	public Organization(Integer id, String orgName) {
		this.id = id;
		this.orgName = orgName;
	}

	/** full constructor */
	public Organization(Integer id, String orgName, String pid,
			String orgType, Integer orgSort, Integer orgLevel, String orgCode,
			Integer areaId, Set<UserOrg> userOrgs) {
		this.id = id;
		this.orgName = orgName;
		this.pid = pid;
		this.orgType = orgType;
		this.orgSort = orgSort;
		this.orgLevel = orgLevel;
		this.orgCode = orgCode;
		this.areaId = areaId;
		this.userOrgs = userOrgs;
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

	@Column(name = "org_name", nullable = false)
	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Column(name = "pid")
	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@Column(name = "org_type")
	public String getOrgType() {
		return this.orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	@Column(name = "org_sort")
	public Integer getOrgSort() {
		return this.orgSort;
	}

	public void setOrgSort(Integer orgSort) {
		this.orgSort = orgSort;
	}

	@Column(name = "org_level")
	public Integer getOrgLevel() {
		return this.orgLevel;
	}

	public void setOrgLevel(Integer orgLevel) {
		this.orgLevel = orgLevel;
	}
	@Column(name = "org_code")
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	@Column(name = "area_id")
	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "organization")
	public Set<UserOrg> getUserOrgs() {
		return userOrgs;
	}

	public void setUserOrgs(Set<UserOrg> userOrgs) {
		this.userOrgs = userOrgs;
	}
	
	@Transient//不作持久化
    public String getState() {//关于树状结构的级别显示问题
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}