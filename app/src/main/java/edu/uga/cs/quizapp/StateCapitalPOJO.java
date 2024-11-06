package edu.uga.cs.quizapp;

/**
 * Represents a data class for storing information about a state's capital.
 */
public class StateCapitalPOJO {

    private long id;
    private String state;
    private String capitalCity;
    private String secondCity;
    private String thirdCity;
    private long stateHood;
    private long capitalSince;
    private long sizeRank;

    public StateCapitalPOJO(long id, String state, String capitalCity, String secondCity,
                            String thirdCity, long stateHood, long capitalSince, long sizeRank) {
        this.id = id;
        this.state = state;
        this.capitalCity = capitalCity;
        this.secondCity = secondCity;
        this.thirdCity = thirdCity;
        this.stateHood = stateHood;
        this.capitalSince = capitalSince;
        this.sizeRank = sizeRank;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCapitalCity(String capitalCity) {
        this.capitalCity = capitalCity;
    }

    public void setSecondCity(String secondCity) {
        this.secondCity = secondCity;
    }

    public void setThirdCity(String thirdCity) {
        this.thirdCity = thirdCity;
    }

    public void setStateHood(long stateHood) {
        this.stateHood = stateHood;
    }

    public void setCapitalSince(long capitalSince) {
        this.capitalSince = capitalSince;
    }

    public void setSizeRank(long sizeRank) {
        this.sizeRank = sizeRank;
    }

    public String getState() {
        return state;
    }

    public String getCapitalCity() {
        return capitalCity;
    }

    public String getSecondCity() {
        return secondCity;
    }

    public String getThirdCity() {
        return thirdCity;
    }

    public long getStateHood() {
        return stateHood;
    }

    public long getCapitalSince() {
        return capitalSince;
    }

    public long getSizeRank() {
        return sizeRank;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "StateCapitalPOJO{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", capitalCity='" + capitalCity + '\'' +
                ", secondCity='" + secondCity + '\'' +
                ", thirdCity='" + thirdCity + '\'' +
                ", stateHood=" + stateHood +
                ", capitalSince=" + capitalSince +
                ", sizeRank=" + sizeRank +
                '}';
    }




}
