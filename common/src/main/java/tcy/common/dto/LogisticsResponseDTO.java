package tcy.common.dto;

import java.util.List;

/**
 *
 */
public class LogisticsResponseDTO {
    private String LogisticCode;
    private String ShipperCode;
    private String State;
    private String Success;
    private List<LogisticsTracesResponseDTO> Traces;
    private String Reason;

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        LogisticCode = logisticCode;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String shipperCode) {
        ShipperCode = shipperCode;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public List<LogisticsTracesResponseDTO> getTraces() {
        return Traces;
    }

    public void setTraces(List<LogisticsTracesResponseDTO> traces) {
        Traces = traces;
    }
}
