package project4;

/**
 * Created by markus on 12.04.2016.
 */
public class SpawnedObject {
    private int objectXPos;
    private int objectYPos;
    private int objectSize;

    public SpawnedObject(int objectXPos, int objectYPos, int objectSize) {
        this.objectXPos = objectXPos;
        this.objectYPos = objectYPos;
        this.objectSize = objectSize;
    }

    public int getObjectXPos() {
        return objectXPos;
    }

    public int getObjectYPos() {
        return objectYPos;
    }

    public int getObjectSize() {
        return objectSize;
    }
}
