package com.Pokemon.pokemon.Service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String toEmail, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true para HTML

            helper.setTo(toEmail);
            helper.setSubject("Verificación de Correo");
            String verificationUrl = "http://localhost:8080/api/verify?token=" + token;
            String htmlMsg
                    = "<!DOCTYPE html>"
                    + "<html>"
                    + "<body style='margin:0; padding:0; background-color:#e0e0d0;'>"
                    + "<table width='100%' cellpadding='0' cellspacing='0' style='padding:20px; background-color:#e0e0d0;'>"
                    + "<tr><td align='center'>"
                    + "<table width='600' cellpadding='0' cellspacing='0' style='background-color:#f8f8f0; border:4px solid #2b2b2b; font-family:Arial, sans-serif;'>"
                    + "<tr>"
                    + "<td style='background-color:#ffcb05; color:#2b2b2b; padding:16px; text-align:center; border-bottom:4px solid #2b2b2b;'>"
                    + "<h1 style='margin:0; font-size:20px;'>Centro Pokémon</h1>"
                    + "<p style='margin:5px 0 0 0; font-size:12px;'>Verificación de Entrenador</p>"
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td style='padding:25px; text-align:center; color:#2b2b2b;'>"
                    + "<p style='font-size:14px;'>¡Hola, Entrenador!</p>"
                    + "<p style='font-size:13px;'>Tu cuenta está casi lista. Para comenzar tu aventura Pokémon, primero debes verificar tu correo.</p>"
                    + "<a href='" + verificationUrl + "' "
                    + "style='display:inline-block; margin:20px 0; padding:14px 24px; "
                    + "background-color:#ff3b3b; color:#ffffff; text-decoration:none; "
                    + "font-size:13px; border:3px solid #2b2b2b; border-radius:6px; "
                    + "box-shadow:3px 3px 0 #2b2b2b;'>"
                    + "▶ Verificar Cuenta"
                    + "</a>"
                    + "<p style='font-size:11px; margin-top:18px;'>Si no verificas tu cuenta, no podrás iniciar sesión.</p>"
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td style='background-color:#ffcb05; padding:12px; text-align:center; border-top:4px solid #2b2b2b; font-size:10px;'>"
                    + "&copy; 2026 Centro Pokémon Digital"
                    + "</td>"
                    + "</tr>"
                    + "</table>"
                    + "</td></tr></table>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlMsg, true); // true indica contenido HTML

            mailSender.send(message);
            System.out.println("Correo de verificación enviado a: " + toEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPasswordResetMail(String toEmail, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Recuperación de contraseña");

            String resetUrl = "http://localhost:8080/usuario/reset-password?token=" + token;

            String htmlMsg
                    = "<!DOCTYPE html>"
                    + "<html>"
                    + "<body style='margin:0; padding:0; background-color:#f4f4f4;'>"
                    + "<table width='100%' cellpadding='0' cellspacing='0' style='background-color:#f4f4f4; padding:20px;'>"
                    + "<tr>"
                    + "<td align='center'>"
                    + "<table width='600' cellpadding='0' cellspacing='0' style='background-color:#ffffff; font-family:Arial, sans-serif;'>"
                    + "<tr>"
                    + "<td style='background-color:#dc3545; color:#ffffff; padding:15px; text-align:center;'>"
                    + "<h1 style='margin:0; font-size:22px;'>Recuperación de contraseña</h1>"
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td style='padding:20px; text-align:center; color:#333333;'>"
                    + "<p style='font-size:16px;'>Hemos recibido una solicitud para restablecer tu contraseña.</p>"
                    + "<p style='font-size:15px;'>Haz clic en el botón para continuar:</p>"
                    + "<a href='" + resetUrl + "' "
                    + "style='display:inline-block; margin-top:20px; padding:12px 25px; "
                    + "background-color:#dc3545; color:#ffffff; text-decoration:none; "
                    + "font-size:16px; border-radius:4px;'>"
                    + "Restablecer contraseña"
                    + "</a>"
                    + "<p style='margin-top:20px; font-size:14px; color:#555555;'>"
                    + "Este enlace expirará en 15 minutos.</p>"
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td style='padding:15px; text-align:center; font-size:12px; color:#777777;'>"
                    + "&copy; 2025 Digis01 Soluciones Digitales"
                    + "</td>"
                    + "</tr>"
                    + "</table>"
                    + "</td>"
                    + "</tr>"
                    + "</table>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlMsg, true);
            mailSender.send(message);

            System.out.println("Correo de recuperación enviado a: " + toEmail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
