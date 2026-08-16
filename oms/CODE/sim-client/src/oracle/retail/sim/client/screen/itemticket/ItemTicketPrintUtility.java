package oracle.retail.sim.client.screen.itemticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.itemticket.ItemTicket;
import oracle.retail.sim.common.itemticket.TicketTypeId;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.reportrequest.ItemTicketReportRequest;
import oracle.retail.sim.common.reportrequest.UinReportRequest;

/********************************************************************************************************
 * Item Ticket Print Utility (for the PC GUI).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemTicketPrintUtility {

    /****************************************************************************************************
     * Private Constructor
     ***************************************************************************************************/
    private ItemTicketPrintUtility() {
    }

    /****************************************************************************************************
     * Print ticket for a printer
     ***************************************************************************************************/
    public static ReportResponse printTicket(ItemTicket itemTicket, StorePrinter printer) throws Exception {
        ItemTicketReportRequest reportRequest = buildRequest(itemTicket);

        List<ReportRequest> requests = Collections.singletonList((ReportRequest) reportRequest);
        List<ReportResponse> responses = SimClientPrintUtility.callRequestReports(requests, printer);
        ReportResponse responseFailure = SimClientPrintUtility.checkResponseFailure(responses);

        if (responseFailure != null) {
            return responseFailure;
        }
        return createSuccessResponse();

    }

    public static ReportResponse printUinLabels(String itemId, List<String> uins, String tempalteUrl, StorePrinter printer, Long storeId) throws Exception {
        List<ReportRequest> requests = new ArrayList<ReportRequest>(uins.size());
        for (String uin : uins) {
            UinReportRequest reportRequest = BOFactory.createUinReportRequest();
            reportRequest.setUrl(tempalteUrl);
            reportRequest.setCopies(1);
            reportRequest.setItemId(itemId);
            reportRequest.setStoreId(storeId);
            reportRequest.setUin(uin);
            requests.add(reportRequest);
        }
        List<ReportResponse> responses = SimClientPrintUtility.callRequestReports(requests, printer);
        ReportResponse responseFailure = SimClientPrintUtility.checkResponseFailure(responses);

        if (responseFailure != null) {
            return responseFailure;
        }
        return createSuccessResponse();

    }

    public static ReportResponse createSuccessResponse() {
        ReportResponse response = BOFactory.createReportResponse();
        return response;

    }

    /****************************************************************************************************
     * Print tickets for a printer
     ***************************************************************************************************/
    public static ReportResponse printTickets(List<ItemTicket> itemTickets, StorePrinter printer) throws Exception {
        ReportResponse response = createSuccessResponse();
        List<ReportRequest> requests = new ArrayList<ReportRequest>();

        if (itemTickets == null || itemTickets.size() == 0) {
            response.setMessage(ReportMessageText.NO_ROWS_SELECTED_PRINT);
            return response;
        }
        for (ItemTicket itemTicket : itemTickets) {
            ItemTicketReportRequest reportRequest = buildRequest(itemTicket);
            requests.add(reportRequest);

        }

        List<ReportResponse> responses = SimClientPrintUtility.callRequestReports(requests, printer);
        ReportResponse responseFailure = SimClientPrintUtility.checkResponseFailure(responses);
        if (responseFailure != null) {
            response = responseFailure;
        }

        return response;

    }

    private static ItemTicketReportRequest buildRequest(ItemTicket itemTicket) {
        ItemTicketReportRequest reportRequest = BOFactory.createItemTicketReportRequest();

        String templateUrl = itemTicket.getTicketTypeFormat().getTemplateURL();
        String storeUrl = SimConfigManager.getStoreString(templateUrl, itemTicket.getStoreId());

        reportRequest.setUrl(StringHelper.isNullOrEmpty(storeUrl) ? templateUrl : storeUrl);
        reportRequest.setCopies(itemTicket.getQuantity());
        // reportRequest.setTicketId(String.valueOf(itemTicket.getId()));

        if (itemTicket.getRetailItem().getId() != null) {
            reportRequest.setItemId(itemTicket.getRetailItem().getId());
        }
        if (itemTicket.getRetailItem().getShortDescription() != null) {
            reportRequest.setDescription(itemTicket.getRetailItem().getShortDescription());
        }
        if (itemTicket.getUserId() != null) {
            reportRequest.setUsername(itemTicket.getUserId());
        }
        if (itemTicket.getCountryManufacture() != null) {
            String countryName = LocaleManager.getDisplayCountry(itemTicket.getCountryManufacture(), true);
            reportRequest.setCountryOfManufactureName(countryName);
        }
        if (itemTicket.getPricePerUom() != null && itemTicket.getTicketTypeFormat().getTicketType().getId().equals(TicketTypeId.SHELF_LABEL_ID)) {
            reportRequest.setPricePerUom(itemTicket.getPricePerUom());
            reportRequest.setFormattedPricePerUom(getPricePerUomDisplayText(itemTicket));
        }
        if (itemTicket.getRetailItem().getStoreId() != null) {
            reportRequest.setStoreId(itemTicket.getRetailItem().getStoreId());
        }

        if (itemTicket.getLabelPrice() != null) {
            reportRequest.setPrice(itemTicket.getLabelPrice());
            reportRequest.setFormattedPrice(formatMoney(itemTicket.getLabelPrice()));
        } else if (itemTicket.getOverridePrice() != null) {
            reportRequest.setPrice(itemTicket.getOverridePrice());
            reportRequest.setFormattedPrice(formatMoney(itemTicket.getOverridePrice()));
        } else if (itemTicket.getRetailItem().getRetailPrice() != null) {
            reportRequest.setPrice(itemTicket.getRetailItem().getRetailPrice());
            reportRequest.setFormattedPrice(formatMoney(itemTicket.getRetailItem().getRetailPrice()));
        }

        if ((itemTicket.getTicketTypeFormat().getTicketType().getId().equals(TicketTypeId.ITEM_TICKET_ID)
        		|| itemTicket.getTicketTypeFormat().getTicketType().getId().equals(TicketTypeId.SHELF_LABEL_ID)) && itemTicket.getQuantity() != null) {
            reportRequest.setTicketQuantity(itemTicket.getQuantity());
        }
        return reportRequest;
    }

    private static String getPricePerUomDisplayText(ItemTicket itemTicket) {
        if (itemTicket == null) {
            return StringConstants.EMPTY;
        }

        String sellingUom = itemTicket.getRetailItem().getSellingUom();
        sellingUom = StringHelper.isNullOrEmpty(sellingUom) ? StringConstants.EMPTY : sellingUom.trim();
        String price = formatMoney(itemTicket.getPricePerUom());
        String uom = StringHelper.isNullOrEmpty(itemTicket.getStandardSellingUom()) ? sellingUom : itemTicket.getStandardSellingUom();

        StringBuilder pricePerUom = new StringBuilder();
        pricePerUom.append(price);
        pricePerUom.append(StringConstants.SPACE);
        pricePerUom.append(StringConstants.FORWARD_SLASH);
        pricePerUom.append(StringConstants.SPACE);
        pricePerUom.append(uom);
        return pricePerUom.toString();
    }

    /****************************************************************************************************
     * Format the money object
     ***************************************************************************************************/
    private static String formatMoney(SimMoney money) {
        return LocaleManager.getCurrencyFormatter(money.getCurrency()).format(money.getAmount());
    }
}
