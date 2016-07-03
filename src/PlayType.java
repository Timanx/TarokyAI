public enum PlayType {
    FIRST_DUTY, SECOND_DUTY, PREFERANC, SOLO, WARSAW;

    @Override
    public String toString() {
        switch(this) {
            case FIRST_DUTY: return "First duty";
            case SECOND_DUTY: return "Second duty";
            case PREFERANC: return "Preferanc";
            case SOLO: return "Solo";
            case WARSAW: return "Warsaw";
            default: throw new IllegalArgumentException();
        }
    }

}
