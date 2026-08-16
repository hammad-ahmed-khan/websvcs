package com.logicinfo.oms.ejb;



import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsResUnresvCustOrderLog.findAll",
                             query = "select o from OmsResUnresvCustOrderLog o") })
@Table(name = "OMS_RES_UNRESV_CUST_ORDER_LOG")
@IdClass(OmsResUnresvCustOrderLogPK.class)
public class OmsResUnresvCustOrderLog implements Serializable {
    @Column(name = "ADJ_QTY", nullable = false)
    private BigDecimal adjQty;
    @Column(name = "CREATE_TIMESTAMP", nullable = false)
    private Timestamp createTimestamp;
    @Column(nullable = false, length = 25)
    private String item;
    @Column(name = "LAST_UPDATE_TIMESTAMP")
    private Timestamp lastUpdateTimestamp;
    @Column(nullable = false)
    private BigDecimal location;
    @Id
    @Column(name = "LOG_ID", nullable = false)
    @SequenceGenerator(name ="omscustordlogseq", sequenceName ="OMS_CUST_ORD_LOG_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "omscustordlogseq" )
    private BigDecimal logId;
    @Id
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Column(nullable = false, length = 1)
    private String status;

    public OmsResUnresvCustOrderLog() {
    }

    public OmsResUnresvCustOrderLog(BigDecimal adjQty, Timestamp createTimestamp, String item,
                                    Timestamp lastUpdateTimestamp, BigDecimal location, BigDecimal logId,
                                    BigDecimal omsCustOrdNo, String status) {
        this.adjQty = adjQty;
        this.createTimestamp = createTimestamp;
        this.item = item;
        this.lastUpdateTimestamp = lastUpdateTimestamp;
        this.location = location;
        this.logId = logId;
        this.omsCustOrdNo = omsCustOrdNo;
        this.status = status;
    }

    public BigDecimal getAdjQty() {
        return adjQty;
    }

    public void setAdjQty(BigDecimal adjQty) {
        this.adjQty = adjQty;
    }

    public Timestamp getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Timestamp getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(Timestamp lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public BigDecimal getLocation() {
        return location;
    }

    public void setLocation(BigDecimal location) {
        this.location = location;
    }

    public BigDecimal getLogId() {
        return logId;
    }

    public void setLogId(BigDecimal logId) {
        this.logId = logId;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}