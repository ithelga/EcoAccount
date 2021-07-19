package com.ecoaccount;

/**
 * Created by Helga on
 */
public interface Platform {
    void showScanner();
    void hideScanner();
    void setAutofocus(boolean focus);
    void setFlashLight(boolean flash);
    void setScannerBounds(int x, int y, int width, int height);
}
