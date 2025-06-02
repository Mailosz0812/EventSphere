package org.locations.eventspheremvc.services;

import DTOs.ticketDetailsDTO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;

public class QrCodeGenerator {
    public static byte[] generateQRCodeImage(ticketDetailsDTO ticketDetails) throws  Exception{
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String text = String.valueOf(ticketDetails.getTicketID()) + ticketDetails.getPurchaseDate();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE,250,250);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}
