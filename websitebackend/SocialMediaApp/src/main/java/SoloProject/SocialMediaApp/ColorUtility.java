package SoloProject.SocialMediaApp;

import java.util.Random;

public class ColorUtility {

    public static String getRandomColor() {
        Random random = new Random();
        int nextInt = random.nextInt(0xffffff + 1);
        return String.format("#%06x", nextInt);
    }
}