package android.bigplan.lego.model;

import java.io.Serializable;


public class Goods implements Serializable {


    private String Amount;
    private String Explain;
    private String Id;
    private String Image;
    private String MemberId;
    private String Name;
    private String Price;
    private int num;

    /**
     * 这个是增加或减少的数量
     */
    private int jiajiannum=0;

    public int getNum() {
        return num;
    }

    public int getJiajiannum() {
        return jiajiannum;
    }

    public void setJiajiannum(int jiajiannum) {
        this.jiajiannum = jiajiannum;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getExplain() {
        return Explain;
    }

    public void setExplain(String explain) {
        Explain = explain;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
