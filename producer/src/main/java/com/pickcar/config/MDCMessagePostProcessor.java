package com.pickcar.config;

import com.pickcar.constants.GlobalStatic.MDCConstants;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

@Component
public class MDCMessagePostProcessor implements MessagePostProcessor {

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        String traceId = MDC.get(MDCConstants.TRACE_ID_KEY);

        if(traceId != null) {
            message.getMessageProperties().setHeader(MDCConstants.TRACE_ID_HEADER_KEY, traceId);
        }
        return message;
    }
}
