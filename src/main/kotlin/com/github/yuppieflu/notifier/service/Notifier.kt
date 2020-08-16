package com.github.yuppieflu.notifier.service

import com.github.yuppieflu.notifier.domain.NotificationPackage
import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Email
import com.sendgrid.helpers.mail.objects.MailSettings
import com.sendgrid.helpers.mail.objects.Personalization
import com.sendgrid.helpers.mail.objects.Setting
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Service

interface Notifier {
    fun notify(aPackage: NotificationPackage)
}

@Service
class SendgridNotifier(
    private val sendgridProperties: SendgridProperties
) : Notifier {

    private val log = LoggerFactory.getLogger(SendgridNotifier::class.java)

    private val sendGrid = SendGrid(sendgridProperties.apiKey)

    override fun notify(aPackage: NotificationPackage) {
        val personalization = Personalization().apply {
            addDynamicTemplateData("username", aPackage.username)
            addTo(Email(aPackage.email))
        }

        val mail = Mail().apply {
            setFrom(Email(sendgridProperties.fromEmail))
            setTemplateId(sendgridProperties.templateId)
            addPersonalization(personalization)
            if (sendgridProperties.sandboxMode) {
                setMailSettings(MailSettings())
                val sandBoxMode = Setting()
                sandBoxMode.enable = true
                mailSettings.setSandboxMode(sandBoxMode)
            }
        }

        runCatching {
            val request = Request().apply {
                method = Method.POST
                endpoint = "mail/send"
                body = mail.build()
            }
            sendGrid.api(request)
        }.fold(
            onFailure = {
                log.error("Error sending email via Sendgrid", it)
            },
            onSuccess = {
                log.info("Sendgrid response: status=${it.statusCode}, body=${it.body}")
            }
        )
    }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "notifier.sendgrid")
data class SendgridProperties(
    val apiKey: String,
    val fromEmail: String,
    val templateId: String,
    val sandboxMode: Boolean
)
