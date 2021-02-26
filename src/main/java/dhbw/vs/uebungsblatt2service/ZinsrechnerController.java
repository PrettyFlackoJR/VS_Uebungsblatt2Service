package dhbw.vs.uebungsblatt2service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/zinsen")
public class ZinsrechnerController {

    @GetMapping
    public int berechneLaufzeitInMonaten(@RequestParam double kreditbetrag,
                                         @RequestParam double zinssatz,
                                         @RequestParam double rueckzahlung) {
        return laufzeit(kreditbetrag, zinssatz, rueckzahlung);
    }

    @GetMapping
    public Collection<Double> rueckzahlungsplan(@RequestParam double kreditbetrag,
                                                @RequestParam double zinssatz,
                                                @RequestParam double rueckzahlung) {
        return plan(kreditbetrag, zinssatz, rueckzahlung);
    }

    public int laufzeit(double kreditbetrag, double zinssatz, double rueckzahlung) {
        zinssatz = zinssatz / 100;
        double t = ((rueckzahlung * 12) / kreditbetrag) - zinssatz;
        double lnOben = Math.log(1 + (zinssatz / t));
        double lnUnten = Math.log(1 + (zinssatz / 12));
        double ergebnis = lnOben / lnUnten;
        int laufzeit = (int) Math.ceil(ergebnis);

        return laufzeit;
    }

    public Collection<Double> plan(double kreditbetrag, double zinssatz, double rueckzahlung) {
        Collection<Double> restbetraege = null;
        double q = 1 + (zinssatz / 12);
        int laufzeit = laufzeit(kreditbetrag, zinssatz, rueckzahlung);
        restbetraege.add(kreditbetrag);
        for (int i = 1; i < laufzeit; i++) {
            double restschuld = kreditbetrag * Math.pow(q, i) - rueckzahlung * ((Math.pow(q, i) - 1) / (zinssatz / 12));
            restbetraege.add(restschuld);
        }
        restbetraege.add(0.0);
        return restbetraege;
    }
}
