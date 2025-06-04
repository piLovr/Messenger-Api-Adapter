package com.piLovr.messengerAdapters.whatsappMobile;

import com.piLovr.messengerAdapters.whatsapp.WhatsappListener;
import com.piLovr.messengerAdapters.whatsapp.WhatsappSocket;
import it.auties.whatsapp.api.MobileOptionsBuilder;
import it.auties.whatsapp.api.QrHandler;
//import it.auties.whatsapp.api.WebHistorySetting;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.companion.CompanionDevice;
import it.auties.whatsapp.model.mobile.VerificationCodeMethod;

import java.util.Scanner;

public class WhatsappMobileSocket extends WhatsappSocket {
    private long phoneNumber;

    public WhatsappMobileSocket(String alias) {
        super(alias);
        this.sock = Whatsapp.mobileBuilder()
                .newConnection() //lastConnection
                // .proxy(URI.create("http://username:password@host:port/")) Remember to set an HTTP proxy
                .device(CompanionDevice.ios(true)) // Make sure to select the correct account type(business or personal) or you'll get error 401
                .unregistered()
                .verificationCodeSupplier(() -> {
                    System.out.println("Enter the verification code: ");
                    return new Scanner(System.in)
                            .nextLine()
                            .trim()
                            .replace("-", "");
                })
                .verificationCodeMethod(VerificationCodeMethod.NONE)
                .register(phoneNumber)
                .join()
                .whatsapp();
    }
}