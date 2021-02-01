package exceptins;

public class CanNotDeleteFile extends Exception{
    public CanNotDeleteFile() {
    }

    public CanNotDeleteFile(String message) {
        super(message);
    }
}
