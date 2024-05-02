package com.visara.transportationweb.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visara.transportationweb.Service.WhatsAppService;

@RestController
public class WhatsAppController {

    private final WhatsAppService whatsAppService;

    public WhatsAppController(WhatsAppService whatsAppService) {
        this.whatsAppService = whatsAppService;
    }

    @PostMapping("/send-whatsapp-message")
    public String sendWhatsAppMessage(
            @RequestParam("to") String to,
            @RequestParam("message") String message) {
        whatsAppService.sendWhatsAppMessage(to, message);
        return "Message sent successfully!";
    }
}

