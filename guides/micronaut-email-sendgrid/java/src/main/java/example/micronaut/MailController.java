package example.micronaut;

import com.sendgrid.Request;
import com.sendgrid.Response;
import io.micronaut.email.AsyncEmailSender;
import io.micronaut.email.Email;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import static io.micronaut.email.BodyType.HTML;

@Controller("/mail") // <1>
public class MailController {

    private static final Logger LOG = LoggerFactory.getLogger(MailController.class);

    private final AsyncEmailSender<Request, Response> emailSender;

    public MailController(AsyncEmailSender<Request, Response> emailSender) { // <2>
        this.emailSender = emailSender;
    }

    @Post("/send") // <3>
    public Publisher<HttpResponse<?>> send(@Body("to") String to) { // <4>
        return Mono.from(emailSender.sendAsync(Email.builder()
                        .to(to)
                        .subject("Sending email with Twilio Sendgrid is Fun")
                        .body("and <em>easy</em> to do anywhere with <strong>Micronaut Email</strong>", HTML)))
                .doOnNext(rsp -> {
                    LOG.info("response status {}\nresponse body {}\nresponse headers {}",
                            rsp.getStatusCode(), rsp.getBody(), rsp.getHeaders());
                }).map(rsp -> rsp.getStatusCode() >= 400 ?
                        HttpResponse.unprocessableEntity() :
                        HttpResponse.accepted()); // <5>
    }
}
