package com.example.springbootecommerceapi.service;

import com.example.springbootecommerceapi.model.Email;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class HtmlEmailSenderService implements EmailSenderService{

    private final JavaMailSender mailSender;
    private final Configuration fmConfiguration;

    @Autowired
    public HtmlEmailSenderService(JavaMailSender mailSender, Configuration fmConfiguration) {
        this.mailSender = mailSender;
        this.fmConfiguration = fmConfiguration;
    }

    @Override
    public void sendEmail(Email email, String template) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setSubject(email.getSubject());
            mimeMessageHelper.setFrom(email.getFrom());
            mimeMessageHelper.setTo(email.getTo());

            email.setContent(getContentFromTemplate(email.getModel(), template));
            mimeMessageHelper.setText(email.getContent(), true);

            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException("Something went Wrong");
        }
    }

    @Override
    public String getContentFromTemplate(Map<String, Object> model, String template) {
        StringBuffer content = new StringBuffer();

        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(
                    fmConfiguration.getTemplate(template + ".flth"), model));
        } catch (Exception e) {
            throw new RuntimeException("Something went Wrong");
        }
        return  content.toString();
    }

}
