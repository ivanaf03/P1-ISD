package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.util.compra.Sale;
import es.udc.ws.app.model.util.service.ServiceFactory;
import es.udc.ws.app.model.util.service.exceptions.GameCelebratedException;
import es.udc.ws.app.model.util.service.exceptions.InvalidCreditCardException;
import es.udc.ws.app.model.util.service.exceptions.NoTicketsException;
import es.udc.ws.app.model.util.service.exceptions.TicketsAlreadyDeliveredException;
import es.udc.ws.app.restservice.dto.RestSaleDto;
import es.udc.ws.app.restservice.dto.SaleToRestSaleDtoConversor;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestSaleDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesServlet extends RestHttpServletTemplate {
    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        Long saleId = null;
        boolean buy = false;
        Sale sale;

        try {
            saleId = ServletUtils.getIdFromPath(req, "saleId");
        } catch (Exception e) {
            buy = true;
        }
        String creditCardNumber = ServletUtils.getMandatoryParameter(req, "cardNum");

        if (buy) {
            Long gameId = ServletUtils.getMandatoryParameterAsLong(req, "gameId");
            String userEmail = ServletUtils.getMandatoryParameter(req, "userEmail");
            int ticketNum = Integer.parseInt(ServletUtils.getMandatoryParameter(req, "ticketNum").trim());

            try {
                sale = ServiceFactory.getService().buy(gameId, userEmail, creditCardNumber, ticketNum);
            } catch (NoTicketsException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                        AppExceptionToJsonConversor.toNoTicketsException(e), null);
                return;
            } catch (GameCelebratedException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                        AppExceptionToJsonConversor.toGameCelebratedException(e), null);
                return;
            }
        } else {
            try {
                sale = ServiceFactory.getService().pickUpTickets(saleId, creditCardNumber);
            } catch (InvalidCreditCardException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toInvalidCreditCardException(e), null);
                return;
            } catch (TicketsAlreadyDeliveredException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CONFLICT,
                        AppExceptionToJsonConversor.toTicketsAlreadyDeliveredException(e), null);
                return;
            }
        }

        RestSaleDto saleDto = SaleToRestSaleDtoConversor.toRestSaleDto(sale);
        String saleURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + sale.getSaleId();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", saleURL);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestSaleDto.toObjectNode(saleDto),
                headers);
    }



    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException {
        String userEmailParam = req.getParameter("userEmail");

        if (userEmailParam != null) {
            List<Sale> saleList = ServiceFactory.getService().findSale(userEmailParam);
            List<RestSaleDto> saleDtoList = new ArrayList<>();

            for (Sale sale : saleList) {
                RestSaleDto saleDto = SaleToRestSaleDtoConversor.toRestSaleDto(sale);
                saleDtoList.add(saleDto);
            }

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestSaleDto.toArrayNode(saleDtoList), null);
        } else {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    null, null);
        }
    }

}
