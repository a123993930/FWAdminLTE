package com.guy.system.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guy.system.station.entity.Station;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 用户entity
 *
 * @author blank
 * @date 2015年1月13日
 */
@Entity
@Table(name = "sys_user")
public class User implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String dingId;
    private String loginName;//登录名
    private String areaCode;
    private String name;//名字
    private String password;//密码
    private String plainPassword;//确认密码
    private String salt;//生成随机的salt并经过1024次 sha-1 hash
    private Timestamp birthday;//生日
    private Short gender;//性别
    private Station station;//岗位
    private String email;//电子邮件
    private String phone;//电话
    private String icon;//图标
    private Timestamp createDate;//创建时间
    private String state;//状态 0 在职 1离职
    private String description;//描述
    private Integer loginCount;//登录次数
    private Timestamp previousVisit;//上次访问时间
    private Timestamp lastVisit;//最后一次访问时间
    private String delFlag;//删除标记
    private String loginIP;//登录IP
    private Integer sort;//排序
    @JsonIgnore
    private Set<UserRole> userRoles = new HashSet<UserRole>(0);//角色
    @JsonIgnore
    private Set<UserOrg> userOrgs = new HashSet<UserOrg>(0);//组织


    // Constructors

    /**
     * default constructor
     */
    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    /**
     * minimal constructor
     */
    public User(String loginName, String name, String password) {
        this.loginName = loginName;
        this.name = name;
        this.password = password;
    }


    // Property accessors
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "DING_ID",columnDefinition = "varchar(100) default '0'")
    public String getDingId() {
        return dingId;
    }

    public void setDingId(String dingId) {
        this.dingId = dingId;
    }

    @Column(name = "LOGIN_NAME", nullable = false, length = 20, unique = true)
    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STATION_ID")
    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @Column(name = "AREA_CODE")
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Column(name = "NAME", nullable = false, length = 20)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "PASSWORD", nullable = false)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "SALT")
    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Column(name = "BIRTHDAY", length = 19)
    public Timestamp getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    @Column(name = "GENDER")
    public Short getGender() {
        return this.gender;
    }

    public void setGender(Short gender) {
        this.gender = gender;
    }

    @Column(name = "EMAIL", length = 50)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "PHONE", length = 20)
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "ICON", length = 500)
    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Column(name = "CREATE_DATE", length = 19)
    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Column(name = "STATE", length = 1)
    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "LOGIN_COUNT")
    public Integer getLoginCount() {
        return this.loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    @Column(name = "PREVIOUS_VISIT", length = 19)
    public Timestamp getPreviousVisit() {
        return this.previousVisit;
    }

    public void setPreviousVisit(Timestamp previousVisit) {
        this.previousVisit = previousVisit;
    }

    @Column(name = "LAST_VISIT", length = 19)
    public Timestamp getLastVisit() {
        return this.lastVisit;
    }

    public void setLastVisit(Timestamp lastVisit) {
        this.lastVisit = lastVisit;
    }

    @Column(name = "DEL_FLAG", length = 1)
    public String getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Column(name = "SORT")
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Column(name = "LOGIN_IP", length = 20)
    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    public Set<UserRole> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    public Set<UserOrg> getUserOrgs() {
        return userOrgs;
    }

    public void setUserOrgs(Set<UserOrg> userOrgs) {
        this.userOrgs = userOrgs;
    }

    // 不持久化到数据库，也不显示在Restful接口的属性.
    @Transient
    @JsonIgnore
    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }


}