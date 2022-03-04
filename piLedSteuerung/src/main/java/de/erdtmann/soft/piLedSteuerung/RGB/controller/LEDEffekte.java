package de.erdtmann.soft.piLedSteuerung.RGB.controller;

import java.awt.Color;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import de.erdtmann.soft.lichtUtils.Colors;
import de.erdtmann.soft.piLedSteuerung.RGB.util.LedUtils;


@Component
public class LEDEffekte {

	@Autowired
	LedUtils ledUtils;

	private boolean effektAn = false;
	
	@Async
	public void colorFading(Color eingangsFarbe, int speed) {
		
		effektAn = true;
		
		try {				
			
			ledUtils.setLedColor(Color.BLACK);
			
			while(effektAn) {
				for (int i = 1; i <= 100; i++) {
					double alpha = 1.0 * i / 100;
					ledUtils.setLedColor(combine(Color.BLACK, eingangsFarbe, alpha));
					Thread.sleep(speed);		
				}
				for (int i = 1; i <= 100; i++) {
					double alpha = 1.0 * i / 100;
					ledUtils.setLedColor(combine(eingangsFarbe, Color.BLACK, alpha));
					Thread.sleep(speed);						
				}
			};
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	@Async
	public void colorStrobo(Color farbe, int speed) {
		effektAn = true;
		try {
			while(effektAn) {
				ledUtils.setLedColor(farbe);
				Thread.sleep(speed);
				ledUtils.setLedColor(Color.BLACK);
				Thread.sleep(speed);
			};
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void farbwechsel(int speed) {
		effektAn = true;
		
		List<Colors> farben = Colors.alleMultiFarben();
		
		for (Colors colors : farben) {
			System.out.println(colors.name());
		}
		
		try {
			while(effektAn) {
				for (int f = 0; f < farben.size()-1; f++) {

					ledUtils.setLedColor(farben.get(f).getFarbe());
					Thread.sleep(speed);
					for (int i = 1; i <= 100; i++) {
						double alpha = 1.0 * i / 100;
						ledUtils.setLedColor(combine(farben.get(f).getFarbe(), farben.get(f+1).getFarbe(), alpha));
						Thread.sleep(speed);		
					}
				}
			};
			
		} catch (Exception e) {
			
		}
	}
	
	 private static Color combine(Color start, Color ende, double alpha) {
	        int r = (int) (alpha * ende.getRed()   + (1 - alpha) * start.getRed());
	        int g = (int) (alpha * ende.getGreen() + (1 - alpha) * start.getGreen());
	        int b = (int) (alpha * ende.getBlue()  + (1 - alpha) * start.getBlue());
	        return new Color(r, g, b);
	    }


	public boolean isEffektAn() {
		return effektAn;
	}
	public void setEffektAn(boolean effektAn) {
		this.effektAn = effektAn;
	}
}
