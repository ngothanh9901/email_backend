package com.example.mailbackend.support;

import com.example.mailbackend.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt", "hibernateLazyInitializer", "handler"},
        allowGetters = true
)

public abstract class DateAudit implements Serializable {

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }


    public String getCreatedDate() {
        return DateUtils.getDate(getCreatedAt());
    }

    public String getCreatedDateTime() {
        return DateUtils.getDateTime(getCreatedAt());
    }

    public String getUpdatedDate() {
        return DateUtils.getDate(getUpdatedAt());
    }

    public String getUpdatedDateTime() {
        return DateUtils.getDateTime(getUpdatedAt());
    }
}
