class CardException extends Exception {
    /**
     * Raise this Exception when the users' card has a negative balance.
     */
    public CardException(){
        super("Not enough balance, please charge your card to continue your trip.");
    }
}
