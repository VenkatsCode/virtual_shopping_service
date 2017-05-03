package com.sap.cdom.vrshopping.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import com.sap.cdom.vrshopping.jpa.MultiTenantJpaTransactionManager;

@Entity
@Table(name = "Order_product")
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = "TENANT_ID",
	contextProperty = MultiTenantJpaTransactionManager.TENANT_DESCRIMINATOR_NAME)
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "ORDER_TOTAL")
	private Float total;
	
	@Column(name = "ORDER_QTY")
	private Long quantity;
	
	@Column(name = "ORDER_DATE")
	private Date date;
	
	public Order() {
	}

	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}
	
	public Long getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", total=" + total + ", quantity=" + quantity + ", date=" + date + "]";
	}
	
}
