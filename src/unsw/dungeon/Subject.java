package unsw.dungeon;

public interface Subject {
    public void addObserver(Observer o, String observerName);
    public void removeObserver(Observer o, String observerName);
    public void notifyObservers(Entity e, String observerName, String request);
}
