public class Announcement {

    private AnnouncementName name;
    private int points;
    private int multiplication;


    public Announcement(AnnouncementName name, int points) {
        this.name = name;
        this.points = points;
    }

    public Announcement(int multiplication, AnnouncementName name) {
        this.name = name;
        this.multiplication = multiplication;
    }

    public int getPoints() {
        return points;
    }

    public AnnouncementName getName() {
        return name;
    }

    public String toString() {
       return translateName(name) + " (" + points + " points)";
    }

    private String translateName(AnnouncementName name) {
        switch (name) {
            case TAROKY:
                return "Taroky";
            case TAROCKY:
                return "Tarocky";
            case BARVA:
                return "Barva";
            case BARVICKA:
                return "Barvicka";
            case TRUL:
                return "Trul";
            case HONERY:
                return "Honery";
            case TRULHONERY:
                return "Trulhonery";
            case KRALOVSKE_HONERY:
                return "Kralovske honery";
            default:
                return "Unknown";
        }
    }
}
