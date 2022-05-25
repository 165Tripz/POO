package Model;

import java.io.Serializable;
import java.time.LocalDate;

import static Model.Orders.OrderType.Company;

public class Orders implements Serializable {
    private final LocalDate dayOfUse;

    private final OrderType target;

    private final String[] details;

    public enum OrderType {
        Company,
        SmartDevice,
        Division
    }

    /**
     *
     * @param dayOfUse date of order
     * @param target target action
     * @param details details format (TypeLocation,TypeLocation?,TypeLocation?,action,details
     */

    public Orders(LocalDate dayOfUse,OrderType target,String[] details) {
        this.dayOfUse = dayOfUse;
        this.target = target;
        this.details = details;
    }

    public LocalDate getDayOfUse() {
        return dayOfUse;
    }

    public String[] getDetails() {
        return details;
    }

    public OrderType getTarget() {
        return target;
    }
}
