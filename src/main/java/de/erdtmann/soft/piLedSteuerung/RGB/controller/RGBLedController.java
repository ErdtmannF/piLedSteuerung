package de.erdtmann.soft.piLedSteuerung.RGB.controller;

import java.awt.Color;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;


import ch.qos.logback.classic.Logger;

@Service
public class RGBLedController {

	Logger log = (Logger) LoggerFactory.getLogger(RGBLedController.class);

	private static int PIN_RED = 21;
	private static int PIN_GREEN = 22;
	private static int PIN_BLUE = 23;

	private static int MAX_RGB_VALUE = 255;
	private static int MAX_PIN_VALUE = 100;

	public void init() {
		Gpio.wiringPiSetup();

		SoftPwm.softPwmCreate(PIN_RED, 0, MAX_PIN_VALUE);
		SoftPwm.softPwmCreate(PIN_GREEN, 0, MAX_PIN_VALUE);
		SoftPwm.softPwmCreate(PIN_BLUE, 0, MAX_PIN_VALUE);

		log.info("RGB LED initialisiert!");
	}

	public void setLedColor(Color c) {
		SoftPwm.softPwmWrite(PIN_RED, convertRGBFarbe(c.getRed()));
	    SoftPwm.softPwmWrite(PIN_GREEN, convertRGBFarbe(c.getGreen()));
	    SoftPwm.softPwmWrite(PIN_BLUE, convertRGBFarbe(c.getBlue()));
	}

	private int convertRGBFarbe(int farbTeil) {
		return farbTeil * MAX_PIN_VALUE / MAX_RGB_VALUE;
	}

}
