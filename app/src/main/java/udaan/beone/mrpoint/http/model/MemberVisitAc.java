package udaan.beone.mrpoint.http.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@JsonSerialize
public class MemberVisitAc {

	@JsonIgnore
	public static final JSONRepo JSONRepo = new JSONRepo<MemberVisitAc>() {
		@Override
		protected Class<MemberVisitAc> objectClass() {
			return MemberVisitAc.class;
		}
	};

	private long startTs;
	private long endTs;
	private long duration;
	private int totalVisits;
	private int ownerVisits;
	private int authVisits;
	private int properVisits;

	private String ownerRole;
	private String authRole;

	private long ownerAcNo;
	private long authAcNo;
	private String ownerAcNoDisplay;
	private String ownerName;
	private String authAcNoDisplay;
	private String authName;

	private BigDecimal openingBalanceAm;
	private BigDecimal paidRegistrationAm;
	private BigDecimal paidDepositAm;
	private BigDecimal paidCollectedAm;
	private BigDecimal paidSoldAm;
	private BigDecimal paidInterestPenaltyAm;
	private BigDecimal collectedReturnedDepositAm;

	private BigDecimal collectedRegistrationAm;
	private BigDecimal collectedDepositAm;
	private BigDecimal collectedCollectedAm;
	private BigDecimal collectedSoldAm;
	private BigDecimal collectedInterestPenaltyAm;
	private BigDecimal paidReturnedDepositAm;
	private BigDecimal closingBalanceAm;

	private BigDecimal openingStockAm;
	private BigDecimal givenStockAm;
	private BigDecimal returnStockAm;
	private BigDecimal soldStockAm;
	private BigDecimal soldStockDiscountAm;
	private BigDecimal mrSoldStockAm;
	private BigDecimal givenAsAuthStockAm;
	private BigDecimal returnAsAuthStockAm;
	private BigDecimal soldAsAuthStockAm;
	private BigDecimal closingStockAm;

	private int openingStockNo;
	private int givenStockNo;
	private int returnStockNo;
	private int soldStockNo;
	private int soldStockDiscountNo;
	private int givenAsAuthStockNo;
	private int returnAsAuthStockNo;
	private int soldAsAuthStockNo;
	private int closingStockNo;

	private List<MrVisit> mrVisits;

	public long getStartTs() {
		return startTs;
	}

	public void setStartTs(long startTs) {
		this.startTs = startTs;
	}

	public long getEndTs() {
		return endTs;
	}

	public void setEndTs(long endTs) {
		this.endTs = endTs;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public int getTotalVisits() {
		return totalVisits;
	}

	public void setTotalVisits(int totalVisits) {
		this.totalVisits = totalVisits;
	}

	public int getOwnerVisits() {
		return ownerVisits;
	}

	public void setOwnerVisits(int ownerVisits) {
		this.ownerVisits = ownerVisits;
	}

	public int getAuthVisits() {
		return authVisits;
	}

	public void setAuthVisits(int authVisits) {
		this.authVisits = authVisits;
	}

	public int getProperVisits() {
		return properVisits;
	}

	public void setProperVisits(int properVisits) {
		this.properVisits = properVisits;
	}

	public String getOwnerRole() {
		return ownerRole;
	}

	public void setOwnerRole(String ownerRole) {
		this.ownerRole = ownerRole;
	}

	public String getAuthRole() {
		return authRole;
	}

	public void setAuthRole(String authRole) {
		this.authRole = authRole;
	}

	public long getOwnerAcNo() {
		return ownerAcNo;
	}

	public void setOwnerAcNo(long ownerAcNo) {
		this.ownerAcNo = ownerAcNo;
	}

	public long getAuthAcNo() {
		return authAcNo;
	}

	public void setAuthAcNo(long authAcNo) {
		this.authAcNo = authAcNo;
	}

	public String getOwnerAcNoDisplay() {
		return ownerAcNoDisplay;
	}

	public void setOwnerAcNoDisplay(String ownerAcNoDisplay) {
		this.ownerAcNoDisplay = ownerAcNoDisplay;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getAuthAcNoDisplay() {
		return authAcNoDisplay;
	}

	public void setAuthAcNoDisplay(String authAcNoDisplay) {
		this.authAcNoDisplay = authAcNoDisplay;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public BigDecimal getOpeningBalanceAm() {
		return openingBalanceAm;
	}

	public void setOpeningBalanceAm(BigDecimal openingBalanceAm) {
		this.openingBalanceAm = openingBalanceAm;
	}

	public BigDecimal getPaidRegistrationAm() {
		return paidRegistrationAm;
	}

	public void setPaidRegistrationAm(BigDecimal paidRegistrationAm) {
		this.paidRegistrationAm = paidRegistrationAm;
	}

	public BigDecimal getPaidDepositAm() {
		return paidDepositAm;
	}

	public void setPaidDepositAm(BigDecimal paidDepositAm) {
		this.paidDepositAm = paidDepositAm;
	}

	public BigDecimal getPaidCollectedAm() {
		return paidCollectedAm;
	}

	public void setPaidCollectedAm(BigDecimal paidCollectedAm) {
		this.paidCollectedAm = paidCollectedAm;
	}

	public BigDecimal getPaidSoldAm() {
		return paidSoldAm;
	}

	public void setPaidSoldAm(BigDecimal paidSoldAm) {
		this.paidSoldAm = paidSoldAm;
	}

	public BigDecimal getPaidInterestPenaltyAm() {
		return paidInterestPenaltyAm;
	}

	public void setPaidInterestPenaltyAm(BigDecimal paidInterestPenaltyAm) {
		this.paidInterestPenaltyAm = paidInterestPenaltyAm;
	}

	public BigDecimal getCollectedReturnedDepositAm() {
		return collectedReturnedDepositAm;
	}

	public void setCollectedReturnedDepositAm(BigDecimal collectedReturnedDepositAm) {
		this.collectedReturnedDepositAm = collectedReturnedDepositAm;
	}

	public BigDecimal getCollectedRegistrationAm() {
		return collectedRegistrationAm;
	}

	public void setCollectedRegistrationAm(BigDecimal collectedRegistrationAm) {
		this.collectedRegistrationAm = collectedRegistrationAm;
	}

	public BigDecimal getCollectedDepositAm() {
		return collectedDepositAm;
	}

	public void setCollectedDepositAm(BigDecimal collectedDepositAm) {
		this.collectedDepositAm = collectedDepositAm;
	}

	public BigDecimal getCollectedCollectedAm() {
		return collectedCollectedAm;
	}

	public void setCollectedCollectedAm(BigDecimal collectedCollectedAm) {
		this.collectedCollectedAm = collectedCollectedAm;
	}

	public BigDecimal getCollectedSoldAm() {
		return collectedSoldAm;
	}

	public void setCollectedSoldAm(BigDecimal collectedSoldAm) {
		this.collectedSoldAm = collectedSoldAm;
	}

	public BigDecimal getCollectedInterestPenaltyAm() {
		return collectedInterestPenaltyAm;
	}

	public void setCollectedInterestPenaltyAm(BigDecimal collectedInterestPenaltyAm) {
		this.collectedInterestPenaltyAm = collectedInterestPenaltyAm;
	}

	public BigDecimal getPaidReturnedDepositAm() {
		return paidReturnedDepositAm;
	}

	public void setPaidReturnedDepositAm(BigDecimal paidReturnedDepositAm) {
		this.paidReturnedDepositAm = paidReturnedDepositAm;
	}

	public BigDecimal getClosingBalanceAm() {
		return closingBalanceAm;
	}

	public void setClosingBalanceAm(BigDecimal closingBalanceAm) {
		this.closingBalanceAm = closingBalanceAm;
	}

	public BigDecimal getOpeningStockAm() {
		return openingStockAm;
	}

	public void setOpeningStockAm(BigDecimal openingStockAm) {
		this.openingStockAm = openingStockAm;
	}

	public BigDecimal getGivenStockAm() {
		return givenStockAm;
	}

	public void setGivenStockAm(BigDecimal givenStockAm) {
		this.givenStockAm = givenStockAm;
	}

	public BigDecimal getReturnStockAm() {
		return returnStockAm;
	}

	public void setReturnStockAm(BigDecimal returnStockAm) {
		this.returnStockAm = returnStockAm;
	}

	public BigDecimal getSoldStockAm() {
		return soldStockAm;
	}

	public void setSoldStockAm(BigDecimal soldStockAm) {
		this.soldStockAm = soldStockAm;
	}

	public BigDecimal getSoldStockDiscountAm() {
		return soldStockDiscountAm;
	}

	public void setSoldStockDiscountAm(BigDecimal soldStockDiscountAm) {
		this.soldStockDiscountAm = soldStockDiscountAm;
	}

	public BigDecimal getMrSoldStockAm() {
		return mrSoldStockAm;
	}

	public void setMrSoldStockAm(BigDecimal mrSoldStockAm) {
		this.mrSoldStockAm = mrSoldStockAm;
	}

	public BigDecimal getGivenAsAuthStockAm() {
		return givenAsAuthStockAm;
	}

	public void setGivenAsAuthStockAm(BigDecimal givenAsAuthStockAm) {
		this.givenAsAuthStockAm = givenAsAuthStockAm;
	}

	public BigDecimal getReturnAsAuthStockAm() {
		return returnAsAuthStockAm;
	}

	public void setReturnAsAuthStockAm(BigDecimal returnAsAuthStockAm) {
		this.returnAsAuthStockAm = returnAsAuthStockAm;
	}

	public BigDecimal getSoldAsAuthStockAm() {
		return soldAsAuthStockAm;
	}

	public void setSoldAsAuthStockAm(BigDecimal soldAsAuthStockAm) {
		this.soldAsAuthStockAm = soldAsAuthStockAm;
	}

	public BigDecimal getClosingStockAm() {
		return closingStockAm;
	}

	public void setClosingStockAm(BigDecimal closingStockAm) {
		this.closingStockAm = closingStockAm;
	}

	public int getOpeningStockNo() {
		return openingStockNo;
	}

	public void setOpeningStockNo(int openingStockNo) {
		this.openingStockNo = openingStockNo;
	}

	public int getGivenStockNo() {
		return givenStockNo;
	}

	public void setGivenStockNo(int givenStockNo) {
		this.givenStockNo = givenStockNo;
	}

	public int getReturnStockNo() {
		return returnStockNo;
	}

	public void setReturnStockNo(int returnStockNo) {
		this.returnStockNo = returnStockNo;
	}

	public int getSoldStockNo() {
		return soldStockNo;
	}

	public void setSoldStockNo(int soldStockNo) {
		this.soldStockNo = soldStockNo;
	}

	public int getSoldStockDiscountNo() {
		return soldStockDiscountNo;
	}

	public void setSoldStockDiscountNo(int soldStockDiscountNo) {
		this.soldStockDiscountNo = soldStockDiscountNo;
	}

	public int getGivenAsAuthStockNo() {
		return givenAsAuthStockNo;
	}

	public void setGivenAsAuthStockNo(int givenAsAuthStockNo) {
		this.givenAsAuthStockNo = givenAsAuthStockNo;
	}

	public int getReturnAsAuthStockNo() {
		return returnAsAuthStockNo;
	}

	public void setReturnAsAuthStockNo(int returnAsAuthStockNo) {
		this.returnAsAuthStockNo = returnAsAuthStockNo;
	}

	public int getSoldAsAuthStockNo() {
		return soldAsAuthStockNo;
	}

	public void setSoldAsAuthStockNo(int soldAsAuthStockNo) {
		this.soldAsAuthStockNo = soldAsAuthStockNo;
	}

	public int getClosingStockNo() {
		return closingStockNo;
	}

	public void setClosingStockNo(int closingStockNo) {
		this.closingStockNo = closingStockNo;
	}

	public List<MrVisit> getMrVisits() {
		return mrVisits;
	}
	public void setMrVisits(List<MrVisit> mrVisits) {
		this.mrVisits = mrVisits;
	}
	public void addMrVisit(MrVisit mrVisit) {
		if(this.mrVisits == null) {
			this.mrVisits = new ArrayList<MrVisit>();
		}
		this.mrVisits.add(mrVisit);
	}
}
