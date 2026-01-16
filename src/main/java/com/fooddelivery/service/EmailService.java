package com.fooddelivery.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.from:noreply@fooddelivery.com}")
    private String fromEmail;

    @Async
    public void sendRegistrationConfirmation(String toEmail, String name, String role) {
        String subject = "Welcome to Food Delivery System!";
        String htmlContent = getRegistrationEmailTemplate(name, role);
        sendEmail(toEmail, subject, htmlContent);
    }

    @Async
    public void sendOrderConfirmation(String toEmail, String customerName, Long orderId, 
                                     BigDecimal totalAmount, String deliveryAddress) {
        String subject = "Order Confirmation - Order #" + orderId;
        String htmlContent = getOrderConfirmationTemplate(customerName, orderId, totalAmount, deliveryAddress);
        sendEmail(toEmail, subject, htmlContent);
    }

    @Async
    public void sendOrderStatusUpdate(String toEmail, String customerName, Long orderId, 
                                     String status, String message) {
        String subject = "Order Update - Order #" + orderId;
        String htmlContent = getOrderStatusUpdateTemplate(customerName, orderId, status, message);
        sendEmail(toEmail, subject, htmlContent);
    }

    @Async
    public void sendDeliveryOTP(String toEmail, String customerName, String otp, Long orderId) {
        String subject = "Delivery OTP - Order #" + orderId;
        String htmlContent = getDeliveryOTPTemplate(customerName, otp, orderId);
        sendEmail(toEmail, subject, htmlContent);
    }

    @Async
    public void sendPaymentSuccess(String toEmail, String customerName, Long orderId, 
                                  BigDecimal amount, String transactionId) {
        String subject = "Payment Successful - Order #" + orderId;
        String htmlContent = getPaymentSuccessTemplate(customerName, orderId, amount, transactionId);
        sendEmail(toEmail, subject, htmlContent);
    }

    @Async
    public void sendPaymentFailure(String toEmail, String customerName, Long orderId, 
                                  BigDecimal amount, String reason) {
        String subject = "Payment Failed - Order #" + orderId;
        String htmlContent = getPaymentFailureTemplate(customerName, orderId, amount, reason);
        sendEmail(toEmail, subject, htmlContent);
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            // Log error but don't throw - email sending should not break the main flow
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }

    private String getRegistrationEmailTemplate(String name, String role) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Welcome to Food Delivery System!</h1>
                    </div>
                    <div class="content">
                        <p>Hello <strong>%s</strong>,</p>
                        <p>Thank you for registering with Food Delivery System as a <strong>%s</strong>.</p>
                        <p>Your account has been successfully created. You can now start using our platform to:</p>
                        <ul>
                            <li>Browse restaurants and menus</li>
                            <li>Place orders</li>
                            <li>Track your deliveries</li>
                            <li>Manage your profile</li>
                        </ul>
                        <p>If you have any questions, please don't hesitate to contact our support team.</p>
                        <p>Happy ordering!</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 Food Delivery System. All rights reserved.</p>
                        <p>This is an automated email, please do not reply.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(name, role);
    }

    private String getOrderConfirmationTemplate(String customerName, Long orderId, 
                                               BigDecimal totalAmount, String deliveryAddress) {
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' HH:mm"));
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .order-info { background-color: white; padding: 15px; margin: 15px 0; border-left: 4px solid #4CAF50; }
                    .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Order Confirmed!</h1>
                    </div>
                    <div class="content">
                        <p>Hello <strong>%s</strong>,</p>
                        <p>Your order has been confirmed and is being prepared.</p>
                        <div class="order-info">
                            <p><strong>Order ID:</strong> #%d</p>
                            <p><strong>Order Date:</strong> %s</p>
                            <p><strong>Total Amount:</strong> ₹%.2f</p>
                            <p><strong>Delivery Address:</strong> %s</p>
                        </div>
                        <p>We'll notify you once your order is ready for delivery.</p>
                        <p>Thank you for choosing Food Delivery System!</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 Food Delivery System. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(customerName, orderId, formattedDate, totalAmount, deliveryAddress);
    }

    private String getOrderStatusUpdateTemplate(String customerName, Long orderId, 
                                               String status, String message) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #2196F3; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .status-box { background-color: white; padding: 15px; margin: 15px 0; border-left: 4px solid #2196F3; }
                    .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Order Status Update</h1>
                    </div>
                    <div class="content">
                        <p>Hello <strong>%s</strong>,</p>
                        <div class="status-box">
                            <p><strong>Order ID:</strong> #%d</p>
                            <p><strong>Status:</strong> %s</p>
                            <p><strong>Message:</strong> %s</p>
                        </div>
                        <p>You can track your order in real-time through our app.</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 Food Delivery System. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(customerName, orderId, status, message);
    }

    private String getDeliveryOTPTemplate(String customerName, String otp, Long orderId) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #FF9800; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .otp-box { background-color: white; padding: 20px; margin: 20px 0; text-align: center; border: 2px dashed #FF9800; }
                    .otp-code { font-size: 32px; font-weight: bold; color: #FF9800; letter-spacing: 5px; }
                    .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Delivery OTP</h1>
                    </div>
                    <div class="content">
                        <p>Hello <strong>%s</strong>,</p>
                        <p>Your order #%d is out for delivery!</p>
                        <div class="otp-box">
                            <p>Please provide this OTP to the delivery partner:</p>
                            <div class="otp-code">%s</div>
                            <p style="margin-top: 15px; color: #666;">This OTP is valid for 10 minutes.</p>
                        </div>
                        <p><strong>Important:</strong> Do not share this OTP with anyone except the delivery partner.</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 Food Delivery System. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(customerName, orderId, otp);
    }

    private String getPaymentSuccessTemplate(String customerName, Long orderId, 
                                            BigDecimal amount, String transactionId) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .payment-info { background-color: white; padding: 15px; margin: 15px 0; border-left: 4px solid #4CAF50; }
                    .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Payment Successful!</h1>
                    </div>
                    <div class="content">
                        <p>Hello <strong>%s</strong>,</p>
                        <p>Your payment for Order #%d has been processed successfully.</p>
                        <div class="payment-info">
                            <p><strong>Order ID:</strong> #%d</p>
                            <p><strong>Amount Paid:</strong> ₹%.2f</p>
                            <p><strong>Transaction ID:</strong> %s</p>
                        </div>
                        <p>Your order is now confirmed and will be prepared shortly.</p>
                        <p>Thank you for your payment!</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 Food Delivery System. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(customerName, orderId, orderId, amount, transactionId);
    }

    private String getPaymentFailureTemplate(String customerName, Long orderId, 
                                            BigDecimal amount, String reason) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #f44336; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .payment-info { background-color: white; padding: 15px; margin: 15px 0; border-left: 4px solid #f44336; }
                    .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Payment Failed</h1>
                    </div>
                    <div class="content">
                        <p>Hello <strong>%s</strong>,</p>
                        <p>Unfortunately, your payment for Order #%d could not be processed.</p>
                        <div class="payment-info">
                            <p><strong>Order ID:</strong> #%d</p>
                            <p><strong>Amount:</strong> ₹%.2f</p>
                            <p><strong>Reason:</strong> %s</p>
                        </div>
                        <p>Please try again or use an alternative payment method.</p>
                        <p>If you continue to experience issues, please contact our support team.</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 Food Delivery System. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(customerName, orderId, orderId, amount, reason != null ? reason : "Payment processing error");
    }
}

