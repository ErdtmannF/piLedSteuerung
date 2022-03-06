package de.erdtmann.soft.piLedSteuerung.ws281x.controller;

import java.awt.Color;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import de.erdtmann.soft.utilsPackage.licht.Colors;
import de.erdtmann.soft.utilsPackage.licht.LichtKonfig;
import de.erdtmann.soft.utilsPackage.licht.WsLedModus;

import com.github.mbelling.ws281x.*;

@Service
public class WS2811Controller {

	Logger log = (Logger) LoggerFactory.getLogger(WS2811Controller.class);

	private Ws281xLedStrip ledString;

	// Diese Konfiguration wird einmal eingerichtet 
	// und wird nicht zur Laufzeit veraendert
	private static final int FREQUENZ = 800000;
	private static final int DMA = 10;
	private static final int PWM = 0;
	private static final int GPIO = 10;
	private static final int HELLIGKEIT = 200;
	private static final int ANZAHL_LEDS = 100;
	private static final LedStripType LED_TYPE = LedStripType.WS2811_STRIP_BRG;

	// Konfigurationsvariablen
	LichtKonfig konfig;

	public void init() {
		try {
			// Startkonfiguration, kann zur Luafzeit veraendert werden
			konfig = LichtKonfig.builder().withV1(100)
											.withV2(100)
											.withHell(200)
											.withAktiv(false)
											.withFarbe(Colors.WEISS)
											.withAktMode(WsLedModus.ALLE_AUS)
											.build();

			// LED String wird initialisiert
			ledString = new Ws281xLedStrip(ANZAHL_LEDS, GPIO, FREQUENZ, DMA, HELLIGKEIT, PWM, false, LED_TYPE, true);

			log.info("WS281x LEDs initialisiert!");
		} catch (Exception e) {
			log.error("Fehler beim initialisieren der WS2811 LEDs");
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@Async
	public void alleAn() {
		konfig.setAktiv(false);
		konfig.setAktMode(WsLedModus.ALLE_AN);
		ledStripAn();
	}

	@Async
	public void alleAus() {
		konfig.setAktiv(false);
		konfig.setAktMode(WsLedModus.ALLE_AUS);
		ledStripAus();
	}

	@Async
	public void anaus() {
		try {
			konfig.setAktiv(true);
			konfig.setAktMode(WsLedModus.AN_AUS);
			
			ledStripAus();
			
			while (konfig.isAktiv()) {
				ledStripAn();
				Thread.sleep(konfig.getV1());
				ledStripAus();
				Thread.sleep(konfig.getV2());
			}
		} catch (Exception e) {
			log.error("Fehler im An Aus Programm");
			log.error(e.getMessage());
		}
	}

	@Async
	public void fade() {
		try {
			konfig.setAktiv(true);
			konfig.setAktMode(WsLedModus.FADE);

			ledStripAus();
			ledHelligkeitAus();
			ledStripAn();

			while (konfig.isAktiv()) {
				// LEDs heller machen
				for (int i = 0; i < 50; i++) {
					if (konfig.isAktiv()) {
						int h = Math.round(i * 255 / 50);
						setzeHelligkeit(h + "");
						Thread.sleep(konfig.getV1());
					} else {
						break;
					}
				}
				// LEDs dunkler machen
				for (int i = 50; i > 0; i--) {
					if (konfig.isAktiv()) {
						int h = Math.round(i * 255 / 50);
						setzeHelligkeit(h + "");
						Thread.sleep(konfig.getV2());
					} else {
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error("Fehler beim Fade Programm");
			log.error(e.getMessage());
		}
	}

	@Async
	public void colorFade() {
		try {
			konfig.setAktiv(true);
			konfig.setAktMode(WsLedModus.COLOR_FADE);
			
			List<Colors> alleFarben = Colors.alleMultiFarben();
			ledStripAus();

			while (konfig.isAktiv()) {
				for (Colors color : alleFarben) {
					if (konfig.isAktiv()) {

						konfig.setFarbe(color);
						ledStripAn();

						// LEDs heller machen
						for (int i = 0; i < 100; i++) {
							if (konfig.isAktiv()) {
								int h = Math.round(i * 255 / 100);
								setzeHelligkeit(h + "");
								Thread.sleep(konfig.getV1());
							} else {
								break;
							}
						}
						// LEDs dunkler machen
						for (int i = 100; i > 0; i--) {
							if (konfig.isAktiv()) {
								int h = Math.round(i * 255 / 100);
								setzeHelligkeit(h + "");
								Thread.sleep(konfig.getV2());
							} else {
								break;
							}
						}
					} else {
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error("Fehler beim Fade Programm");
			log.error(e.getMessage());
		}
	}

	@Async
	public void hinher() {
		try {
			konfig.setAktiv(true);
			konfig.setAktMode(WsLedModus.HIN_HER);
			
			ledStripAus();
			
			while (konfig.isAktiv()) {
				for (int i = 0; i < ledString.getLedsCount(); i++) {
					if (konfig.isAktiv()) {
						ledPixelAn(i);
						Thread.sleep(konfig.getV1());
					} else {
						break;
					}
				}

				for (int i = ledString.getLedsCount() - 1; i >= 0; i--) {
					if (konfig.isAktiv()) {
						ledPixelAus(i);
						Thread.sleep(konfig.getV2());
					} else {
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error("Fehler im Hin und Her Programm");
			log.error(e.getMessage());
		}
	}

	@Async
	public void setzeFarbe(String farbe) {
		Colors c = Colors.valueOf(farbe);
		konfig.setFarbe(c);
		
		// bei "alle An" müssen die LEDs neu angesprochen werden 
		if (konfig.getAktMode() == WsLedModus.ALLE_AN) {
			alleAn();
		}
	}

	@Async
	public void setzeHelligkeit(String helligkeit) {
		konfig.setHell(Integer.parseInt(helligkeit));
		ledString.setBrightness(konfig.getHell());
		ledString.render();
	}

	@Async
	public void setzeV1(String v) {
		konfig.setV1(Integer.parseInt(v));
	}

	@Async
	public void setzeV2(String v) {
		konfig.setV2(Integer.parseInt(v));
	}

	private void ledHelligkeitAus() {
		ledString.setBrightness(0);
		ledString.render();
	}

	private void ledPixelAus(int i) {
		ledString.setPixel(i, 0, 0, 0);
		ledString.render();
	}

	private void ledPixelAn(int i) {
		ledString.setPixel(i, konfigFarbe().getRed(), konfigFarbe().getGreen(), konfigFarbe().getBlue());
		ledString.render();
	}

	private void ledStripAus() {
		ledString.setStrip(0, 0, 0);
		ledString.render();
	}

	private void ledStripAn() {
		ledString.setStrip(konfigFarbe().getRed(), konfigFarbe().getGreen(), konfigFarbe().getBlue());
		ledString.render();
	}

	private Color konfigFarbe() {
		return konfig.getFarbe().getFarbe();
	}

}
