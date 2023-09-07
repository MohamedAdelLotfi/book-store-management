package com.digitalfactory.bookstore.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableEntity<ID> extends AbstractPersistableEntity<ID> implements Serializable {
    
    @CreatedDate
    LocalDate createdDate;
    
    @LastModifiedDate
    LocalDate lastModifiedDate;
    
    @CreatedBy
    @AttributeOverride(name = "username", column = @Column(name = "created_by"))
    Username createdBy;
    
    @LastModifiedBy
    @AttributeOverride(name = "username", column = @Column(name = "last_modified_by"))
    Username lastModifiedBy;
}
