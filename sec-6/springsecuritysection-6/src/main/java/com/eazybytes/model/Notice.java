package com.eazybytes.model;

import java.sql.Date;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "notice_details")
@Data
public class Notice {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "notice_id")
    private int noticeId;

    @Column(name = "notice_summary")
    private String noticeSummary;

    @Column(name = "notice_details")
    private String noticeDetails;

    @Column(name = "notic_beg_dt")
    private Date noticBegDt;

    @Column(name = "notic_end_dt")
    private Date noticEndDt;

    @Column(name = "create_dt")
    private Date createDt;

    @Column(name = "update_dt")
    private Date updateDt;

}