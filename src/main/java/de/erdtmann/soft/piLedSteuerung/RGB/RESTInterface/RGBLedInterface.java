package de.erdtmann.soft.piLedSteuerung.RGB.RESTInterface;


import java.awt.Color;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.Logger;
import de.erdtmann.soft.utilsPackage.licht.Colors;
import de.erdtmann.soft.piLedSteuerung.RGB.controller.RGBLedController;
import de.erdtmann.soft.piLedSteuerung.ws281x.controller.WS2811Controller;


@RestController
@EnableAsync
public class RGBLedInterface {

    Logger log = (Logger) LoggerFactory.getLogger(RGBLedInterface.class);

    @Autowired
    RGBLedController ledController;
    
    @PostConstruct
	public void init() {
		ledController.init();
	}
    
	@RequestMapping("/ledAn/{farbe}")
	public void ledAn(@PathVariable String farbe) {

		try {
			Color c = Color.decode(farbe);
			ledController.setLedColor(c);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info("RGB LED an!");
	}
	
	@RequestMapping("/ledAnString/{farbe}")
	public void ledAnString(@PathVariable String farbe) {
		Color c = Colors.getFarbeByName(farbe).getFarbe();

		System.out.println(farbe);
		System.out.println(c.toString());
		System.out.println(c.getRed() + " / " + c.getGreen() + " / " + c.getBlue());
		
//		ledUtils.setLedColor(c);
	}
	
	@RequestMapping("/ledAus")
	public String ledAus() {
		
		try {
			ledController.setLedColor(Color.BLACK);
		} catch (Exception e) {
			e.printStackTrace();
		}

	    return "LED aus!";
	}

	
	@RequestMapping("/colorfading/{farbe}/{speed}")
	public String colorFading(@PathVariable String farbe, @PathVariable String speed) {
		
		Color c = Color.decode(farbe);
		
//		ledEffekte.colorFading(c, Integer.parseInt(speed));
		
		return "Color Fading!";
	}
	
	@RequestMapping("/colorstrobo/{farbe}/{speed}")
	public String colorStrobo(@PathVariable String farbe, @PathVariable String speed) {

		Color c = Color.decode(farbe);
		
//		ledEffekte.colorStrobo(c, Integer.parseInt(speed));
		
		return "Color Strobo!";
		
	}
	
	@RequestMapping("/farbwechsel/{speed}")
	public String farbWchsel(@PathVariable String speed) {
		
//		ledEffekte.farbwechsel(Integer.parseInt(speed));
		
		return "Farbwechsel!";
	}
}
