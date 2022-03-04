package de.erdtmann.soft.piLedSteuerung.ws281x.RESTInterface;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.erdtmann.soft.lichtUtils.WsLedModus;
import de.erdtmann.soft.piLedSteuerung.ws281x.controller.WS2811Controller;

@RestController
@RequestMapping("wsLed")
@EnableAsync
public class WSLedInterface {

	Logger log = (Logger) LoggerFactory.getLogger(WSLedInterface.class);

	@Autowired
	WS2811Controller ws2811Controller;

	@PostConstruct
	public void init() {
		ws2811Controller.init();
	}

	@RequestMapping("/{mode}")
	public void rufeModusAuf(@PathVariable("mode") WsLedModus mode) {
		switch (mode) {
			case ALLE_AN: {
				log.debug("Aufruf: " + mode.getParameter());
				ws2811Controller.alleAn();
				break;
			}
			case ALLE_AUS: {
				log.debug("Aufruf: " + mode.getParameter());
				ws2811Controller.alleAus();
				break;
			}
			case HIN_HER: {
				log.debug("Aufruf: " + mode.getParameter());
				ws2811Controller.hinher();
				break;
			}
			case AN_AUS: {
				log.debug("Aufruf: " + mode.getParameter());
				ws2811Controller.anaus();
				break;
			}
			case FADE: {
				log.debug("Aufruf: " + mode.getParameter());
				ws2811Controller.fade();
				break;
			}
			case COLOR_FADE: {
				log.debug("Aufruf: " + mode.getParameter());
				ws2811Controller.colorFade();
				break;
			}
		default:
			break;
		}
	}

	@RequestMapping("/{mode}/{wert}")
	public void rufeModusMitWertAuf(@PathVariable("mode") WsLedModus mode, @PathVariable String wert) {
		switch (mode) {
		case FARBE: {
			log.debug("Aufruf: " + mode.getParameter() + " mit Wert: " + wert);
			ws2811Controller.setzeFarbe(wert);
			
			break;
		}
		case HELLIGKEIT: {
			log.debug("Aufruf: " + mode.getParameter() + " mit Wert: " + wert);
			ws2811Controller.setzeHelligkeit(wert);
			break;
		}
		case V1: {
			log.debug("Aufruf: " + mode.getParameter() + " mit Wert: " + wert);
			ws2811Controller.setzeV1(wert);
			break;
		}
		case V2: {
			log.debug("Aufruf: " + mode.getParameter() + " mit Wert: " + wert);
			ws2811Controller.setzeV2(wert);
			break;
		}
		default:
			break;
		}
	}


}
