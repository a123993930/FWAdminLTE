package com.guy.${packageName}.${objectNameLower}.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
* 类名称：${objectName}Entity
* 创建人：blank
* 创建时间：${nowDate?string("yyyy-MM-dd")}
*/
@Entity
@Table(name = "${packageName}_${objectNameLower}")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate @DynamicInsert
public class ${objectName} implements java.io.Serializable {

// Fields
private static final long serialVersionUID = 1L;
private Integer id;

// Constructors

/** default constructor */
public ${objectName}() {
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
}