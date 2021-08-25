package unsw.entity;

import org.javatuples.Pair;

/**
 * a Card in the world which doesn't move
 */
public abstract class Card extends StaticEntity {
    private Boolean path;
    private Boolean wild;
    private Boolean nearPath;

    private String cardName;

    /**
     * Constructor for Card
     *
     * @param position //
     */
    public Card(Pair<Integer, Integer> position) {
        super(position);
    }

    /**
     * Setter for path
     *
     * @param b //
     */
    public void setPath(Boolean b) {
        this.path = b;
    }

    /**
     * Setter for nearPath
     *
     * @param b //
     */
    public void setNearPath(Boolean b) {
        this.nearPath = b;
    }

    /**
     * Setter for wild
     *
     * @param b //
     */
    public void setWild(Boolean b) {
        this.wild = b;
    }

    /**
     * Getter for nearPath
     *
     * @return //
     */
    public Boolean getNearPath() {
        return this.nearPath;
    }

    /**
     * Getter for path
     *
     * @return //
     */
    public Boolean getPath() {
        return path;
    }

    /**
     * Getter for wild
     *
     * @return //
     */
    public Boolean getWild() {
        return wild;
    }

    /**
     * Setter for cardName
     *
     * @param cardName //
     */
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    /**
     * Getter for cardName
     *
     * @return //
     */
    public String getCardName() {
        return this.cardName;
    }

    /**
     * Abstract method that convert card to building
     *
     * @param position //
     * @return //
     */
    public abstract Building convertToBuilding(Pair<Integer, Integer> position);
}
