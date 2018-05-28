package com.guy.system.city.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/** 
 * 类名称：CityEntity
 * 创建人：blank 
 * 创建时间：2016-12-13
 */
@Entity
@Table(name = "sys_city")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate @DynamicInsert
public class City implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String code;//行政区划代码
	private String name;
	private Integer parentId;//父ID
	private String firstLetter;//首字母
	private Integer level;//城市等级 0 省 1 市 2 区
	private String state;//树默认 关闭

	// Constructors

	/** default constructor */
	public City() {
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

	 @Column(name = "CODE", nullable = false, length = 6)
    public String getCode() {
        return code;
    }
	 

    public void setCode(String code) {
        this.code = code;
    }
    @Column(name = "NAME", nullable = false, length = 255)
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "PARENT_ID", nullable = false, length = 2)
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Column(name = "FIRST_LETTER", nullable = false, length = 10)
    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    @Column(name = "LEVEL", nullable = false, length = 1)
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    
    @Transient//不作持久化
    public String getState() {//关于树状结构的级别显示问题
        if(level==2){
            return state;
        }else{
            return "closed";
        }
    }

    public void setState(String state) {
        this.state = state;
    }
    
    
}