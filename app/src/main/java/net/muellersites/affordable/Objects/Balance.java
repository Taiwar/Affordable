package net.muellersites.affordable.Objects;

public class Balance {

    private Integer id;
    private Integer current_value;

    Balance(Integer current_value, Integer id){
        this.current_value = current_value;
        this.id = id;
    }

    public Balance(){
        this.current_value = 0;
        this.id = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCurrent_value() {
        return current_value;
    }

    public void setCurrent_value(Integer current_value) {
        this.current_value = current_value;
    }

}
