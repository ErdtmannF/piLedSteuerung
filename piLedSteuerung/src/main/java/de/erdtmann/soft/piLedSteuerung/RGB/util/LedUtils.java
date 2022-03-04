package de.erdtmann.soft.piLedSteuerung.RGB.util;

import java.awt.Color;

import org.springframework.stereotype.Component;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

@Component
public class LedUtils {

	private static int PIN_RED = 21;
    private static int PIN_GREEN = 22;
    private static int PIN_BLUE = 23;

    private static int MAX_RGB_VALUE = 255;
    private static int MAX_PIN_VALUE = 100;

	public void initLed() {
		Gpio.wiringPiSetup();

	    SoftPwm.softPwmCreate(PIN_RED, 0, MAX_PIN_VALUE);
	    SoftPwm.softPwmCreate(PIN_GREEN, 0, MAX_PIN_VALUE);
	    SoftPwm.softPwmCreate(PIN_BLUE, 0, MAX_PIN_VALUE);

	    System.out.println("RGB LED initialisiert!");
	}
	
	public void setLedColor(Color c) {
		int r = convertRGBFarbe(c.getRed());
		SoftPwm.softPwmWrite(PIN_RED, r);
		int g = convertRGBFarbe(c.getGreen());
	    SoftPwm.softPwmWrite(PIN_GREEN, g);
	    int b = convertRGBFarbe(c.getBlue());
	    SoftPwm.softPwmWrite(PIN_BLUE, b);
	    
//	    System.out.println("R: " + r + " G: " + g + " B: " + b);
	}

	private int convertRGBFarbe(int farbTeil) {
		return farbTeil * MAX_PIN_VALUE / MAX_RGB_VALUE;
	}

}
