package entities;

/**
 * Created by LA-173 on 28.04.2016.
 */
public class AdvertList {
    public int advtID;
    public String advtDescription;
    public String advtDateTime;
    public int advtPrice;

    public int getAdvtID() {
        return advtID;
    }

    public void setAdvtID(int advtID) {
        this.advtID = advtID;
    }

    public String getAdvtDescription() {
        return advtDescription;
    }

    public void setAdvtDescription(String advtDescription) {
        this.advtDescription = advtDescription;
    }

    public String getAdvtDateTime() {
        return advtDateTime;
    }

    public void setAdvtDateTime(String advtDateTime) {
        this.advtDateTime = advtDateTime;
    }

    public int getAdvtPrice() {
        return advtPrice;
    }

    public void setAdvtPrice(int advtPrice) {
        this.advtPrice = advtPrice;
    }
}
