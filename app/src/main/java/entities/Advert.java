package entities;

/**
 * Created by LA-173 on 28.04.2016.
 */
public class Advert {
    public int advtID;
    public String advtDescription;
    public String advtDateTime;
    public String advtPhone;
    public String advtMail;
    public String advtCategoryCode;
    public String advtImageLink;
    public int advtPrice;
    public Boolean advtIsOpen;

    public Boolean getAdvtIsOpen() {
        return advtIsOpen;
    }

    public void setAdvtIsOpen(Boolean advtIsOpen) {
        this.advtIsOpen = advtIsOpen;
    }

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

    public String getAdvtPhone() {
        return advtPhone;
    }

    public void setAdvtPhone(String advtPhone) {
        this.advtPhone = advtPhone;
    }

    public String getAdvtMail() {
        return advtMail;
    }

    public void setAdvtMail(String advtMail) {
        this.advtMail = advtMail;
    }

    public String getAdvtCategoryCode() {
        return advtCategoryCode;
    }

    public void setAdvtCategoryCode(String advtCategoryCode) {
        this.advtCategoryCode = advtCategoryCode;
    }

    public String getAdvtImageLink() {
        return advtImageLink;
    }

    public void setAdvtImageLink(String advtImageLink) {
        this.advtImageLink = advtImageLink;
    }

    public Advert() {
    }
}
