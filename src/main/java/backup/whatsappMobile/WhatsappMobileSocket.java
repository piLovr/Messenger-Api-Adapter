package backup.whatsappMobile;

import backup.whatsapp.WhatsappSocket;

//import it.auties.whatsapp.api.WebHistorySetting;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.api.WhatsappVerificationHandler;
import it.auties.whatsapp.model.companion.CompanionDevice;


import java.util.Scanner;

public class WhatsappMobileSocket extends WhatsappSocket {
    private long phoneNumber;

    public WhatsappMobileSocket(String alias) {
        super(alias);
        this.sock = Whatsapp.builder().mobileClient()
                .newConnection() //lastConnection
                // .proxy(URI.create("http://username:password@host:port/")) Remember to set an HTTP proxy
                .device(CompanionDevice.ios(true)) // Make sure to select the correct account type(business or personal) or you'll get error 401
                .register(phoneNumber, WhatsappVerificationHandler.Mobile.sms(() -> {
                    System.out.println("Enter the verification code: ");
                    return new Scanner(System.in)
                            .nextLine()
                            .trim()
                            .replace("-", "");
                }));
    }
}